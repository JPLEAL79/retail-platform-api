package com.jp.testplatformapi.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<String> details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::buildFieldMessage)
                .toList();

        return ResponseEntity.badRequest().body(buildError(
                HttpStatus.BAD_REQUEST,
                "Validation failed.",
                request.getRequestURI(),
                details
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(buildError(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildError(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        ));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(
            ConflictException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildError(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request.getRequestURI(),
                List.of()
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableMessage(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        String message = "Invalid JSON or incorrect data type.";
        List<String> details = List.of();

        if (exception.getCause() instanceof InvalidFormatException invalidFormatException) {
            String fieldPath = invalidFormatException.getPath()
                    .stream()
                    .map(reference -> reference.getFieldName())
                    .filter(fieldName -> fieldName != null && !fieldName.isBlank())
                    .reduce((first, second) -> first + "." + second)
                    .orElse("unknown");

            String expectedType = invalidFormatException.getTargetType() != null
                    ? invalidFormatException.getTargetType().getSimpleName()
                    : "valid type";

            details = List.of("Field '" + fieldPath + "' expects a " + expectedType + " value.");
        }

        return ResponseEntity.badRequest().body(buildError(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI(),
                details
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected server error.",
                request.getRequestURI(),
                List.of()
        ));
    }

    private ApiError buildError(HttpStatus status, String message, String path, List<String> details) {
        ApiError error = new ApiError();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(message);
        error.setPath(path);
        error.setDetails(details);
        return error;
    }

    private String buildFieldMessage(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}
