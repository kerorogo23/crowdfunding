package com.fourseasons.crowdfunding.app.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 註冊請求 DTO
 */
@Data
@Schema(description = "註冊請求")
public class RegisterRequest {

    @Schema(description = "使用者名稱", example = "john_doe")
    @NotBlank(message = "使用者名稱不能為空")
    @Size(min = 3, max = 50, message = "使用者名稱長度必須在 3-50 個字元之間")
    private String username;

    @Schema(description = "電子郵件", example = "john@example.com")
    @NotBlank(message = "電子郵件不能為空")
    @Email(message = "請輸入有效的電子郵件格式")
    private String email;

    @Schema(description = "密碼", example = "password123")
    @NotBlank(message = "密碼不能為空")
    @Size(min = 6, max = 100, message = "密碼長度必須在 6-100 個字元之間")
    private String password;
}
