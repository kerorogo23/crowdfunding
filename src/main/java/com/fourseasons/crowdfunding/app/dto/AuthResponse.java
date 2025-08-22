package com.fourseasons.crowdfunding.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 認證回應 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "認證回應")
public class AuthResponse {
    @Schema(description = "JWT Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Token 類型", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "使用者 ID", example = "1")
    private Long userId;

    @Schema(description = "使用者名稱", example = "john_doe")
    private String username;

    @Schema(description = "電子郵件", example = "john@example.com")
    private String email;

    @Schema(description = "使用者角色", example = "USER")
    private String role;

    public AuthResponse(String token, Long userId, String username, String email, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
