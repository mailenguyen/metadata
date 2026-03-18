package com.group1.app.common.exception;

import com.group1.app.common.response.ApiError;
import com.group1.app.common.response.ApiResponse;
import com.group1.app.shift.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ====== UTIL ======
    private String getTraceId(HttpServletRequest request) {
        String traceId = request.getHeader("X-Request-Id");
        return traceId != null ? traceId : UUID.randomUUID().toString();
    }

    private ApiResponse<Object> buildResponse(boolean success, String message, ApiError error) {
        return ApiResponse.builder()
                .success(success)
                .message(message)
                .error(error)
                .timestamp(Instant.now())
                .build();
    }

    // ====== METADATA EXCEPTION ======
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException ex, HttpServletRequest req) {

        String traceId = getTraceId(req);

        log.warn("[{}] Business exception at {}: {}", traceId, req.getRequestURI(), ex.getMessage());

        ApiError error = ApiError.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(ex.getErrorCode().getStatus())
                .body(buildResponse(false, ex.getMessage(), error));
    }

    // ====== SHIFT EXCEPTION ======
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex, HttpServletRequest req) {

        String traceId = getTraceId(req);

        String detail = ex.getErrors() != null && !ex.getErrors().isEmpty()
                ? ex.getErrors().entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("; "))
                : ex.getMessage();

        log.warn("[{}] App exception at {}: {}", traceId, req.getRequestURI(), detail);

        ApiError error = ApiError.builder()
                .code(String.valueOf(ex.getErrorCode().getCode()))
                .message(detail)
                .path(req.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(buildResponse(false, ex.getMessage(), error));
    }

    // ====== VALIDATION ======
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {

        String traceId = getTraceId(req);

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("[{}] Validation failed at {}: {}", traceId, req.getRequestURI(), message);

        ApiError error = ApiError.builder()
                .code("VALIDATION_ERROR")
                .message(message)
                .path(req.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(false, message, error));
    }

    // ====== NOT FOUND (fix favicon) ======
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(NoResourceFoundException ex, HttpServletRequest req) {

        String traceId = getTraceId(req);

        log.debug("[{}] Resource not found: {}", traceId, req.getRequestURI());

        ApiError error = ApiError.builder()
                .code("NOT_FOUND")
                .message("Resource not found")
                .path(req.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildResponse(false, "Resource not found", error));
    }

    // ====== FALLBACK ======
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnhandled(Exception ex, HttpServletRequest req) {

        String traceId = getTraceId(req);

        log.error("[{}] Unexpected error at {}", traceId, req.getRequestURI(), ex);

        String message = ex.getMessage() != null
                ? ex.getMessage()
                : ErrorCode.INTERNAL_ERROR.getMessage();

        ApiError error = ApiError.builder()
                .code(ErrorCode.INTERNAL_ERROR.getCode())
                .message(message)
                .path(req.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(false, message, error));
    }
}