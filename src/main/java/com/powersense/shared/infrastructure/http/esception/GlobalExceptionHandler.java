package com.powersense.shared.infrastructure.http.esception;

import com.powersense.shared.domain.exceptions.DomainException;
import com.powersense.shared.domain.exceptions.NotFoundException;
import com.powersense.shared.domain.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(NotFoundException ex) {
		logger.warn("Not found: {}", ex.getMessage());
		ApiError error = new ApiError("NOT_FOUND", ex.getMessage(), HttpStatus.NOT_FOUND.value(), Instant.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ApiError> handleValidation(ValidationException ex) {
		logger.warn("Validation error: {}", ex.getMessage());
		ApiError error = new ApiError("VALIDATION_ERROR", ex.getMessage(), HttpStatus.BAD_REQUEST.value(), Instant.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<ApiError> handleDomain(DomainException ex) {
		logger.error("Domain error: {}", ex.getMessage());
		ApiError error = new ApiError("DOMAIN_ERROR", ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value(), Instant.now());
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining(", "));
		ApiError error = new ApiError("VALIDATION_ERROR", message, HttpStatus.BAD_REQUEST.value(), Instant.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneral(Exception ex) {
		logger.error("Unexpected error", ex);
		ApiError error = new ApiError("INTERNAL_ERROR", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), Instant.now());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
