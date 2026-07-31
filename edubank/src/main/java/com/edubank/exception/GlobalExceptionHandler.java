package com.edubank.exception;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.edubank.dto.ApiError;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Translates all exceptions into a structured {@link ApiError} JSON body.
 * Stack traces and framework messages are never leaked to clients.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(BankException.class)
	public ResponseEntity<ApiError> handleBank(BankException ex, HttpServletRequest request) {
		log.info("Bank operation rejected [{}]: {}", ex.getCode(), ex.getMessage());
		return build(ex.getStatus(), ex.getCode(), ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField() + " " + f.getDefaultMessage())
				.collect(Collectors.joining("; "));
		if (message.isBlank()) {
			message = "Request validation failed";
		}
		String path = (request instanceof ServletWebRequest swr) ? swr.getRequest().getRequestURI() : null;
		return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, path);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest request) {
		log.error("Unexpected error handling request {}", request.getRequestURI(), ex);
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
				"An unexpected error occurred", request.getRequestURI());
	}

	private ResponseEntity<ApiError> build(HttpStatus status, String code, String message, String path) {
		ApiError body = ApiError.of(status.value(), status.getReasonPhrase(), code, message, path);
		return ResponseEntity.status(status).body(body);
	}
}
