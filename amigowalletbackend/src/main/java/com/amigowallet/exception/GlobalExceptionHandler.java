package com.amigowallet.exception;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Central exception handling. Produces a uniform {@link ApiError} body and
 * guarantees that raw exception messages, keys, and stack traces never leak to
 * clients. Business message keys are resolved through the {@link Environment}
 * (populated from messages.properties) in a null-safe manner.
 *
 * <p>Note: authentication (401) and authorization (403) failures are raised in
 * the Spring Security filter chain, <i>before</i> the DispatcherServlet, so they
 * are handled by the resource-server entry point / access-denied handler and do
 * not reach this advice.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private final Environment environment;

	public GlobalExceptionHandler(Environment environment) {
		this.environment = environment;
	}

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiError> handleApiException(ApiException ex, HttpServletRequest request) {
		return build(ex.getStatus(), resolve(ex.getMessageKey()), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		String fieldErrors = ex.getBindingResult().getFieldErrors().stream()
				.map(this::formatFieldError)
				.collect(Collectors.joining("; "));
		if (fieldErrors.isBlank()) {
			fieldErrors = "Request validation failed";
		}
		return build(HttpStatus.BAD_REQUEST, fieldErrors, request);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiError> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
		if (status == null) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		// The reason on a ResponseStatusException may itself be a key or already-resolved text.
		String message = resolve(ex.getReason());
		return build(status, message, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
		// Never leak ex.getMessage() or a stack trace to the client.
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.", request);
	}

	private String formatFieldError(FieldError error) {
		String message = error.getDefaultMessage();
		return error.getField() + ": " + (message == null ? "invalid value" : message);
	}

	/**
	 * Resolves a message key to human text via the Environment. Falls back to the
	 * key itself (if it resolves to nothing) or a generic message when null.
	 */
	private String resolve(String key) {
		if (key == null || key.isBlank()) {
			return "Request could not be processed.";
		}
		String resolved = environment.getProperty(key);
		return resolved != null ? resolved : key;
	}

	private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest request) {
		ApiError body = new ApiError(
				OffsetDateTime.now(),
				status.value(),
				status.getReasonPhrase(),
				message,
				request != null ? request.getRequestURI() : null);
		return ResponseEntity.status(status).body(body);
	}
}
