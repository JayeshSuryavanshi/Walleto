package com.edubank.dto;

public record MoneyMoveResponse(Long bankTransactionId, String status) {

	public static MoneyMoveResponse success(Long bankTransactionId) {
		return new MoneyMoveResponse(bankTransactionId, "SUCCESS");
	}
}
