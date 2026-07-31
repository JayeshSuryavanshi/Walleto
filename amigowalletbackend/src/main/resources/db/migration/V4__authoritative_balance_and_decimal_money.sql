-- V4: authoritative stored balance + decimal money  (Phase 4 modernization)
--
-- Phase 4 replaces the event-sourced "fold every ledger row on read" balance model
-- with an AUTHORITATIVE, row-locked WALLET_USER.BALANCE column that is updated in the
-- same transaction as each ledger posting. The USER_TRANSACTION ledger remains the
-- append-only audit trail. Money columns move from FLOAT(12,2) to DECIMAL(15,2) to
-- eliminate binary floating-point drift; a VERSION column adds optimistic-lock support
-- alongside the pessimistic row lock used by the money service methods.

-- 1) Add the authoritative balance + optimistic-lock version columns.
ALTER TABLE WALLET_USER
	ADD COLUMN BALANCE DECIMAL(15,2) NOT NULL DEFAULT 0.00,
	ADD COLUMN VERSION BIGINT NOT NULL DEFAULT 0;

-- 2) Convert the money columns to DECIMAL(15,2) FIRST, so the backfill sum in
--    step 3 is exact decimal arithmetic (not accumulated binary-float error). The
--    named CHECK (AMOUNT > 0) constraints added in V1 are preserved by MODIFY COLUMN.
ALTER TABLE USER_TRANSACTION      MODIFY COLUMN AMOUNT DECIMAL(15,2) NOT NULL;
ALTER TABLE MERCHANT_TRANSACTION  MODIFY COLUMN AMOUNT DECIMAL(15,2) NOT NULL;

-- 3) Backfill BALANCE from the existing ledger: SUM(credits) - SUM(debits) over
--    USER_TRANSACTION rows joined to PAYMENT_TYPE, where PAYMENT_TYPE.PAYMENT_TYPE='C'
--    adds and ='D' subtracts, counting ONLY TRANSACTION_STATUS='SUCCESS' rows.
--    (Demo user USER_ID=12121 nets 3413.60 with this fold.)
UPDATE WALLET_USER u
SET u.BALANCE = COALESCE((
		SELECT SUM(CASE WHEN pt.PAYMENT_TYPE = 'C' THEN t.AMOUNT
		                WHEN pt.PAYMENT_TYPE = 'D' THEN -t.AMOUNT
		                ELSE 0 END)
		FROM USER_TRANSACTION t
		JOIN PAYMENT_TYPE pt ON t.PAYMENT_TYPE_ID = pt.PAYMENT_TYPE_ID
		WHERE t.USER_ID = u.USER_ID
		  AND t.TRANSACTION_STATUS = 'SUCCESS'
	), 0.00);

-- 4) Enforce a non-negative authoritative balance at the database level.
--    (Applied AFTER the backfill; no demo user has a negative derived balance.)
ALTER TABLE WALLET_USER
	ADD CONSTRAINT AW_WALLET_USER_BALANCE_CHK CHECK (BALANCE >= 0);
