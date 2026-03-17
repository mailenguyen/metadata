package com.group1.app.common.exception;

import com.group1.app.common.response.ApiError;
import com.group1.app.common.response.ApiResponse;
import com.group1.app.shift.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
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

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---- Metadata domain exceptions ----
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException ex, HttpServletRequest req) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiError error = ApiError.builder()
                .code(errorCode.getCode())
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.<Object>builder()
                        .success(false).message(ex.getMessage()).error(error).timestamp(Instant.now()).build());
    }

    // ---- Shift domain exceptions ----
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex, HttpServletRequest req) {
        String detail = ex.getErrors() != null && !ex.getErrors().isEmpty()
                ? ex.getErrors().entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("; "))
                : ex.getMessage();

        ApiError error = ApiError.builder()
                .code(String.valueOf(ex.getErrorCode().getCode()))
                .message(detail)
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(ApiResponse.<Object>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .error(error)
                        .timestamp(Instant.now())
                        .build());
    }

    // ---- Validation (shared) ----
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ApiError error = ApiError.builder().code("VALIDATION_ERROR").message(message).path(req.getRequestURI()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Object>builder()
                        .success(false).message(message).error(error).timestamp(Instant.now()).build());
    }

    // ---- Fallback ----
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnhandled(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception processing request {}", req.getRequestURI(), ex);
        String message = ex.getMessage() != null ? ex.getMessage() : ErrorCode.INTERNAL_ERROR.getMessage();
        ApiError error = ApiError.builder()
                .code(ErrorCode.INTERNAL_ERROR.getCode()).message(message).path(req.getRequestURI()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<Object>builder()
                        .success(false).message(message).error(error).timestamp(Instant.now()).build());
    }
}
