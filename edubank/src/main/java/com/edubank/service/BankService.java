package com.edubank.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dto.AccountVerifyRequest;
import com.edubank.dto.AccountVerifyResponse;
import com.edubank.dto.CardDebitRequest;
import com.edubank.dto.CreditRequest;
import com.edubank.dto.MoneyMoveResponse;
import com.edubank.dto.NetBankingDebitRequest;
import com.edubank.entity.AccountCustomerMappingEntity;
import com.edubank.entity.AccountEntity;
import com.edubank.entity.BranchEntity;
import com.edubank.entity.CustomerEntity;
import com.edubank.entity.CustomerLoginEntity;
import com.edubank.entity.DebitCardEntity;
import com.edubank.entity.TransactionEntity;
import com.edubank.exception.BankException;
import com.edubank.model.AccountCustomerMappingStatus;
import com.edubank.model.AccountStatus;
import com.edubank.model.CustomerLoginLockedStatus;
import com.edubank.model.DebitCardLockedStatus;
import com.edubank.model.DebitCardStatus;
import com.edubank.model.TransactionType;
import com.edubank.repository.AccountCustomerMappingRepository;
import com.edubank.repository.AccountRepository;
import com.edubank.repository.BranchRepository;
import com.edubank.repository.CustomerLoginRepository;
import com.edubank.repository.CustomerRepository;
import com.edubank.repository.DebitCardRepository;
import com.edubank.repository.TransactionRepository;
import com.edubank.util.Masking;

/**
 * Core banking service. Every money movement is a single
 * {@code @Transactional(rollbackFor = Exception.class)} method that validates
 * the amount, loads the account under a pessimistic write lock, applies the
 * {@link BigDecimal} balance change and writes the ledger row atomically, and
 * returns a bank-issued transaction id.
 */
@Service
public class BankService {

	private static final Logger log = LoggerFactory.getLogger(BankService.class);
	private static final int MONEY_SCALE = 4;

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	private final DebitCardRepository debitCardRepository;
	private final CustomerRepository customerRepository;
	private final CustomerLoginRepository customerLoginRepository;
	private final AccountCustomerMappingRepository mappingRepository;
	private final BranchRepository branchRepository;
	private final PasswordEncoder passwordEncoder;

	public BankService(AccountRepository accountRepository, TransactionRepository transactionRepository,
			DebitCardRepository debitCardRepository, CustomerRepository customerRepository,
			CustomerLoginRepository customerLoginRepository, AccountCustomerMappingRepository mappingRepository,
			BranchRepository branchRepository, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
		this.debitCardRepository = debitCardRepository;
		this.customerRepository = customerRepository;
		this.customerLoginRepository = customerLoginRepository;
		this.mappingRepository = mappingRepository;
		this.branchRepository = branchRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Verify a destination account by number + IFSC + holder name.
	 */
	@Transactional(readOnly = true)
	public AccountVerifyResponse verifyAccount(AccountVerifyRequest request) {
		AccountEntity account = accountRepository.findByAccountNumber(request.accountNumber())
				.orElseThrow(BankException::accountNotFound);
		requireActive(account);
		requireIfscMatches(account, request.ifsc());

		AccountCustomerMappingEntity mapping = activeMapping(account.getAccountNumber());
		CustomerEntity customer = customerRepository.findByCustomerId(mapping.getCustomerId())
				.orElseThrow(BankException::accountNotFound);

		if (!customer.getName().equalsIgnoreCase(request.accountHolderName().trim())) {
			throw BankException.invalidAccountHolderName();
		}
		log.info("Account {} verified", Masking.last4(account.getAccountNumber()));
		return new AccountVerifyResponse(true, customer.getName());
	}

	/**
	 * Credit a bank account (wallet -> bank withdraw landing). Idempotent when an
	 * idempotency key is supplied.
	 */
	@Transactional(rollbackFor = Exception.class)
	public MoneyMoveResponse credit(CreditRequest request) {
		BigDecimal amount = normalizeAmount(request.amount());

		if (request.idempotencyKey() != null && !request.idempotencyKey().isBlank()) {
			var existing = transactionRepository.findByIdempotencyKey(request.idempotencyKey());
			if (existing.isPresent()) {
				log.info("Idempotent credit replay for key {}", request.idempotencyKey());
				return MoneyMoveResponse.success(existing.get().getTransactionId());
			}
		}

		AccountEntity account = accountRepository.findByAccountNumberForUpdate(request.accountNumber())
				.orElseThrow(BankException::accountNotFound);
		requireActive(account);
		requireIfscMatches(account, request.ifsc());

		account.setBalance(account.getBalance().add(amount));
		accountRepository.save(account);

		TransactionEntity txn = ledgerRow(account.getAccountNumber(), amount, TransactionType.CREDIT,
				"AmigoWallet", "From:- AmigoWallet To:- Account, Reason:- transfered",
				emptyToNull(request.idempotencyKey()));
		txn = transactionRepository.save(txn);

		log.info("Credited {} to account {} (txn {})", amount, Masking.last4(account.getAccountNumber()),
				txn.getTransactionId());
		return MoneyMoveResponse.success(txn.getTransactionId());
	}

	/**
	 * Verify a debit card (number / expiry / status / PIN) AND debit the linked
	 * account in one atomic operation (bank -> wallet load via card).
	 */
	@Transactional(rollbackFor = Exception.class)
	public MoneyMoveResponse cardDebit(CardDebitRequest request) {
		BigDecimal amount = normalizeAmount(request.amount());

		DebitCardEntity card = debitCardRepository.findByDebitCardNumber(request.cardNumber())
				.orElseThrow(BankException::cardNotFound);

		if (card.getDebitCardStatus() == DebitCardStatus.INACTIVE) {
			throw BankException.cardInactive();
		}
		if (card.getLockedStatus() == DebitCardLockedStatus.LOCKED) {
			throw BankException.cardLocked();
		}
		if (card.getValidThru() == null || card.getValidThru().isBefore(LocalDate.now())) {
			throw BankException.cardExpired();
		}
		requireExpiryMatches(card, request.expiry());
		if (!passwordEncoder.matches(request.pin(), card.getPin())) {
			throw BankException.invalidPin();
		}

		AccountCustomerMappingEntity mapping = mappingRepository
				.findByAccountCustomerMappingId(card.getAccountCustomerMappingId())
				.orElseThrow(BankException::accountNotFound);

		Long txnId = debitAndRecord(mapping.getAccountNumber(), amount, "Debit-Card",
				"From:- Account To:- AmigoWallet, Reason:- Payment");
		log.info("Card {} debited {} (txn {})", Masking.last4(card.getDebitCardNumber()), amount, txnId);
		return MoneyMoveResponse.success(txnId);
	}

	/**
	 * Authenticate net-banking credentials and debit the customer's account in
	 * one atomic operation (bank -> wallet load via net banking). Replaces the
	 * entire JSP net-banking redirect flow; fully stateless.
	 */
	@Transactional(rollbackFor = Exception.class)
	public MoneyMoveResponse netBankingDebit(NetBankingDebitRequest request) {
		BigDecimal amount = normalizeAmount(request.amount());

		CustomerLoginEntity login = customerLoginRepository.findByLoginName(request.loginName())
				.orElseThrow(BankException::invalidCredentials);
		if (!passwordEncoder.matches(request.password(), login.getPassword())) {
			throw BankException.invalidCredentials();
		}
		if (login.getLockedStatus() == CustomerLoginLockedStatus.LOCKED) {
			throw BankException.accountLocked();
		}

		AccountCustomerMappingEntity mapping = mappingRepository.findByCustomerId(login.getCustomerId())
				.orElseThrow(BankException::accountNotFound);
		if (mapping.getMappingStatus() == AccountCustomerMappingStatus.INACTIVE) {
			throw BankException.accountInactive();
		}

		Long txnId = debitAndRecord(mapping.getAccountNumber(), amount, "Internet-Banking",
				"From:- Account To:- AmigoWallet, Reason:- Payment");
		log.info("Net-banking debit {} for login {} (txn {})", amount, request.loginName(), txnId);
		return MoneyMoveResponse.success(txnId);
	}

	// --- shared atomic debit path ---

	private Long debitAndRecord(String accountNumber, BigDecimal amount, String mode, String info) {
		AccountEntity account = accountRepository.findByAccountNumberForUpdate(accountNumber)
				.orElseThrow(BankException::accountNotFound);
		requireActive(account);

		BigDecimal locked = account.getLockedBalance() == null ? BigDecimal.ZERO : account.getLockedBalance();
		BigDecimal available = account.getBalance().subtract(locked);
		if (available.compareTo(amount) < 0) {
			throw BankException.insufficientFunds();
		}

		account.setBalance(account.getBalance().subtract(amount));
		accountRepository.save(account);

		TransactionEntity txn = ledgerRow(accountNumber, amount, TransactionType.DEBIT, "AmigoWallet", info, null);
		txn.setTransactionMode(mode);
		return transactionRepository.save(txn).getTransactionId();
	}

	private TransactionEntity ledgerRow(String accountNumber, BigDecimal amount, TransactionType type,
			String createdBy, String info, String idempotencyKey) {
		TransactionEntity txn = new TransactionEntity();
		txn.setAccountNumber(accountNumber);
		txn.setAmount(amount);
		txn.setType(type);
		txn.setInfo(info);
		txn.setRemarks(type == TransactionType.CREDIT ? "C" : "D");
		txn.setTransactionMode(type == TransactionType.CREDIT ? "AmigoWallet" : "Debit-Card");
		txn.setCreatedBy(createdBy);
		txn.setIdempotencyKey(idempotencyKey);
		return txn;
	}

	// --- validation helpers ---

	private BigDecimal normalizeAmount(BigDecimal amount) {
		if (amount == null || amount.signum() <= 0 || amount.scale() > MONEY_SCALE) {
			throw BankException.invalidAmount();
		}
		return amount.setScale(MONEY_SCALE);
	}

	private void requireActive(AccountEntity account) {
		if (account.getAccountStatus() == AccountStatus.INACTIVE) {
			throw BankException.accountInactive();
		}
	}

	private void requireIfscMatches(AccountEntity account, String ifsc) {
		BranchEntity branch = branchRepository.findByBranchId(account.getBranchId())
				.orElseThrow(BankException::invalidIfsc);
		if (branch.getIfsc() == null || !branch.getIfsc().equalsIgnoreCase(ifsc.trim())) {
			throw BankException.invalidIfsc();
		}
	}

	private AccountCustomerMappingEntity activeMapping(String accountNumber) {
		AccountCustomerMappingEntity mapping = mappingRepository.findByAccountNumber(accountNumber)
				.orElseThrow(BankException::accountNotFound);
		if (mapping.getMappingStatus() == AccountCustomerMappingStatus.INACTIVE) {
			throw BankException.accountInactive();
		}
		return mapping;
	}

	/**
	 * Verifies the card expiry (month + year) against VALID_THRU. Accepts
	 * "MM/yyyy", "MM/yy" and "yyyy-MM"[-dd].
	 */
	private void requireExpiryMatches(DebitCardEntity card, String expiry) {
		int[] monthYear = parseExpiry(expiry);
		LocalDate validThru = card.getValidThru();
		if (validThru.getMonthValue() != monthYear[0] || validThru.getYear() != monthYear[1]) {
			throw BankException.invalidExpiry();
		}
	}

	private int[] parseExpiry(String expiry) {
		try {
			String value = expiry.trim();
			int month;
			int year;
			if (value.contains("-")) {
				String[] parts = value.split("-");
				year = Integer.parseInt(parts[0]);
				month = Integer.parseInt(parts[1]);
			} else if (value.contains("/")) {
				String[] parts = value.split("/");
				month = Integer.parseInt(parts[0]);
				year = Integer.parseInt(parts[1]);
				if (parts[1].length() == 2) {
					year += 2000;
				}
			} else {
				throw new IllegalArgumentException("Unrecognized expiry format");
			}
			if (month < 1 || month > 12) {
				throw new IllegalArgumentException("Invalid month");
			}
			return new int[] { month, year };
		} catch (RuntimeException e) {
			throw BankException.invalidExpiry();
		}
	}

	private String emptyToNull(String value) {
		return (value == null || value.isBlank()) ? null : value;
	}
}
