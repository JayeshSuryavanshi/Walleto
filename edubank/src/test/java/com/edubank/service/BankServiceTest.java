package com.edubank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.edubank.dto.CreditRequest;
import com.edubank.dto.MoneyMoveResponse;
import com.edubank.dto.NetBankingDebitRequest;
import com.edubank.entity.AccountCustomerMappingEntity;
import com.edubank.entity.AccountEntity;
import com.edubank.entity.BranchEntity;
import com.edubank.entity.CustomerLoginEntity;
import com.edubank.entity.TransactionEntity;
import com.edubank.exception.BankException;
import com.edubank.model.AccountCustomerMappingStatus;
import com.edubank.model.AccountStatus;
import com.edubank.model.CustomerLoginLockedStatus;
import com.edubank.model.TransactionType;
import com.edubank.repository.AccountCustomerMappingRepository;
import com.edubank.repository.AccountRepository;
import com.edubank.repository.BranchRepository;
import com.edubank.repository.CustomerLoginRepository;
import com.edubank.repository.CustomerRepository;
import com.edubank.repository.DebitCardRepository;
import com.edubank.repository.TransactionRepository;

/**
 * Core bank money logic with all repositories mocked (no real DB): amount
 * validation, the funds check on the net-banking debit path (available =
 * balance - lockedBalance; the old overdraft is gone), exact BigDecimal
 * credit/debit arithmetic, and that a ledger transaction is written.
 */
@ExtendWith(MockitoExtension.class)
class BankServiceTest {

	@Mock private AccountRepository accountRepository;
	@Mock private TransactionRepository transactionRepository;
	@Mock private DebitCardRepository debitCardRepository;
	@Mock private CustomerRepository customerRepository;
	@Mock private CustomerLoginRepository customerLoginRepository;
	@Mock private AccountCustomerMappingRepository mappingRepository;
	@Mock private BranchRepository branchRepository;
	@Mock private PasswordEncoder passwordEncoder;

	private BankService bankService() {
		return new BankService(accountRepository, transactionRepository, debitCardRepository, customerRepository,
				customerLoginRepository, mappingRepository, branchRepository, passwordEncoder);
	}

	private AccountEntity account(String number, String balance, String locked, Integer branchId) {
		AccountEntity a = new AccountEntity();
		a.setAccountNumber(number);
		a.setBalance(new BigDecimal(balance));
		a.setLockedBalance(locked == null ? null : new BigDecimal(locked));
		a.setBranchId(branchId);
		a.setAccountStatus(AccountStatus.ACTIVE);
		return a;
	}

	private BranchEntity branch(Integer id, String ifsc) {
		BranchEntity b = new BranchEntity();
		b.setBranchId(id);
		b.setIfsc(ifsc);
		return b;
	}

	private CustomerLoginEntity login(Integer customerId) {
		CustomerLoginEntity l = new CustomerLoginEntity();
		l.setLoginName("martin");
		l.setPassword("80f161f02043c73f49ae7032f46d2741efc909c195b38272fb8400a2d72d9a30");
		l.setCustomerId(customerId);
		l.setLockedStatus(CustomerLoginLockedStatus.UNLOCKED);
		return l;
	}

	private AccountCustomerMappingEntity mapping(String accountNumber, Integer customerId) {
		AccountCustomerMappingEntity m = new AccountCustomerMappingEntity();
		m.setAccountNumber(accountNumber);
		m.setCustomerId(customerId);
		m.setMappingStatus(AccountCustomerMappingStatus.ACTIVE);
		return m;
	}

	private NetBankingDebitRequest nbReq(String amount) {
		return new NetBankingDebitRequest("martin", "demoPass#1", new BigDecimal(amount));
	}

	private void stubNetBankingPathTo(AccountEntity account) {
		when(customerLoginRepository.findByLoginName("martin")).thenReturn(Optional.of(login(552092)));
		when(passwordEncoder.matches(any(), any())).thenReturn(true);
		when(mappingRepository.findByCustomerId(552092)).thenReturn(Optional.of(mapping(account.getAccountNumber(), 552092)));
		when(accountRepository.findByAccountNumberForUpdate(account.getAccountNumber())).thenReturn(Optional.of(account));
	}

	// --- amount validation ---

	@Test
	void credit_rejectsZeroAmount() {
		assertThatThrownBy(() -> bankService().credit(new CreditRequest("ACC1", "EDUB0001", BigDecimal.ZERO, null)))
				.isInstanceOf(BankException.class)
				.satisfies(e -> {
					BankException be = (BankException) e;
					assertThat(be.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
					assertThat(be.getCode()).isEqualTo("INVALID_AMOUNT");
				});
	}

	@Test
	void credit_rejectsNegativeAmount() {
		assertThatThrownBy(() -> bankService().credit(new CreditRequest("ACC1", "EDUB0001", new BigDecimal("-1.00"), null)))
				.isInstanceOf(BankException.class)
				.satisfies(e -> assertThat(((BankException) e).getCode()).isEqualTo("INVALID_AMOUNT"));
	}

	@Test
	void credit_rejectsTooManyDecimalPlaces() {
		assertThatThrownBy(() -> bankService().credit(new CreditRequest("ACC1", "EDUB0001", new BigDecimal("1.00000"), null)))
				.isInstanceOf(BankException.class)
				.satisfies(e -> assertThat(((BankException) e).getCode()).isEqualTo("INVALID_AMOUNT"));
	}

	// --- credit: exact BigDecimal add + ledger row ---

	@Test
	void credit_addsBalanceExactly_andWritesCreditLedgerRow() {
		AccountEntity acct = account("ACC100", "1000.0000", null, 5123);
		when(accountRepository.findByAccountNumberForUpdate("ACC100")).thenReturn(Optional.of(acct));
		when(branchRepository.findByBranchId(5123)).thenReturn(Optional.of(branch(5123, "EDUB0001")));
		when(transactionRepository.save(any())).thenAnswer(inv -> {
			TransactionEntity t = inv.getArgument(0);
			t.setTransactionId(555L);
			return t;
		});

		MoneyMoveResponse resp = bankService().credit(new CreditRequest("ACC100", "EDUB0001", new BigDecimal("250.5000"), null));

		assertThat(resp.bankTransactionId()).isEqualTo(555L);
		assertThat(resp.status()).isEqualTo("SUCCESS");

		ArgumentCaptor<AccountEntity> acctCap = ArgumentCaptor.forClass(AccountEntity.class);
		verify(accountRepository).save(acctCap.capture());
		assertThat(acctCap.getValue().getBalance()).isEqualByComparingTo("1250.5000");

		ArgumentCaptor<TransactionEntity> txnCap = ArgumentCaptor.forClass(TransactionEntity.class);
		verify(transactionRepository).save(txnCap.capture());
		assertThat(txnCap.getValue().getType()).isEqualTo(TransactionType.CREDIT);
		assertThat(txnCap.getValue().getAmount()).isEqualByComparingTo("250.5000");
	}

	// --- net-banking debit: funds check ---

	@Test
	void netBankingDebit_rejectsWhenBalanceBelowAmount() {
		stubNetBankingPathTo(account("ACC200", "100.0000", "0.0000", 5123));

		assertThatThrownBy(() -> bankService().netBankingDebit(nbReq("200.0000")))
				.isInstanceOf(BankException.class)
				.satisfies(e -> {
					BankException be = (BankException) e;
					assertThat(be.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
					assertThat(be.getCode()).isEqualTo("INSUFFICIENT_FUNDS");
				});
	}

	@Test
	void netBankingDebit_lockedBalanceReducesAvailableFunds() {
		// balance 300 but 200 locked -> only 100 available, so a 150 debit is rejected.
		stubNetBankingPathTo(account("ACC200", "300.0000", "200.0000", 5123));

		assertThatThrownBy(() -> bankService().netBankingDebit(nbReq("150.0000")))
				.isInstanceOf(BankException.class)
				.satisfies(e -> assertThat(((BankException) e).getCode()).isEqualTo("INSUFFICIENT_FUNDS"));
	}

	@Test
	void netBankingDebit_debitsExactly_andWritesDebitLedgerRow() {
		AccountEntity acct = account("ACC200", "500.0000", "0.0000", 5123);
		stubNetBankingPathTo(acct);
		when(transactionRepository.save(any())).thenAnswer(inv -> {
			TransactionEntity t = inv.getArgument(0);
			t.setTransactionId(777L);
			return t;
		});

		MoneyMoveResponse resp = bankService().netBankingDebit(nbReq("100.0000"));

		assertThat(resp.bankTransactionId()).isEqualTo(777L);
		assertThat(resp.status()).isEqualTo("SUCCESS");

		ArgumentCaptor<AccountEntity> acctCap = ArgumentCaptor.forClass(AccountEntity.class);
		verify(accountRepository).save(acctCap.capture());
		assertThat(acctCap.getValue().getBalance()).isEqualByComparingTo("400.0000");

		ArgumentCaptor<TransactionEntity> txnCap = ArgumentCaptor.forClass(TransactionEntity.class);
		verify(transactionRepository).save(txnCap.capture());
		assertThat(txnCap.getValue().getType()).isEqualTo(TransactionType.DEBIT);
		assertThat(txnCap.getValue().getAmount()).isEqualByComparingTo("100.0000");
		assertThat(txnCap.getValue().getTransactionMode()).isEqualTo("Internet-Banking");
	}

	@Test
	void netBankingDebit_rejectsInvalidCredentials() {
		when(customerLoginRepository.findByLoginName("martin")).thenReturn(Optional.of(login(552092)));
		when(passwordEncoder.matches(any(), any())).thenReturn(false);

		assertThatThrownBy(() -> bankService().netBankingDebit(nbReq("100.0000")))
				.isInstanceOf(BankException.class)
				.satisfies(e -> {
					BankException be = (BankException) e;
					assertThat(be.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
					assertThat(be.getCode()).isEqualTo("INVALID_CREDENTIALS");
				});
	}
}
