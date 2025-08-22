package com.fourseasons.crowdfunding.app.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全域異常處理器
 * 統一處理各種異常並回傳適當的 HTTP 狀態碼
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 處理驗證異常
     * 
     * @param ex 驗證異常
     * @return 錯誤回應
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("驗證失敗 - 錯誤數量: {}", ex.getBindingResult().getErrorCount());

        Map<String, Object> errors = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
            log.debug("驗證錯誤 - 欄位: {}, 訊息: {}", fieldName, errorMessage);
        });

        errors.put("message", "驗證失敗");
        errors.put("errors", fieldErrors);
        errors.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * 處理資源不存在異常
     * 
     * @param ex 資源不存在異常
     * @return 錯誤回應
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("資源不存在 - 錯誤訊息: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * 處理未授權異常
     * 
     * @param ex 未授權異常
     * @return 錯誤回應
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex) {
        log.warn("未授權存取 - 錯誤訊息: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.FORBIDDEN.value());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * 處理業務邏輯異常
     * 
     * @param ex 業務邏輯異常
     * @return 錯誤回應
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(error);
    }

    /**
     * 處理認證異常
     * 
     * @param ex 認證異常
     * @return 錯誤回應
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "電子郵件或密碼錯誤");
        error.put("status", HttpStatus.UNAUTHORIZED.value());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * 處理使用者不存在異常
     * 
     * @param ex 使用者不存在異常
     * @return 錯誤回應
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * 處理權限不足異常
     * 
     * @param ex 權限不足異常
     * @return 錯誤回應
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "權限不足");
        error.put("status", HttpStatus.FORBIDDEN.value());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * 處理一般異常
     * 
     * @param ex 一般異常
     * @return 錯誤回應
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        // 記錄錯誤
        log.error("Internal server error", ex);
        Map<String, Object> error = new HashMap<>();
        error.put("message", "伺服器內部錯誤");
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        // 在生產環境中，不應該暴露詳細的錯誤資訊
        // error.put("detail", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
