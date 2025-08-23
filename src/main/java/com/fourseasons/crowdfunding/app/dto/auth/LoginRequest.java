package com.fourseasons.crowdfunding.app.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登入請求 DTO
 */
@Data
@Schema(description = "登入請求")
public class LoginRequest {

    @Schema(description = "使用者電子郵件", example = "user@example.com")
    @NotBlank(message = "電子郵件不能為空")
    @Email(message = "請輸入有效的電子郵件格式")
    private String email;

    @Schema(description = "使用者密碼", example = "password123")
    @NotBlank(message = "密碼不能為空")
    private String password;
}
