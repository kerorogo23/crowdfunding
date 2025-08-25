package com.fourseasons.crowdfunding.app.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 使用者回應 DTO
 */
@Data
@Schema(description = "使用者資訊回應")
public class UserResponse {
    @Schema(description = "使用者 ID", example = "1")
    private Long userId;

    @Schema(description = "使用者名稱", example = "john_doe")
    private String username;

    @Schema(description = "電子郵件", example = "john@example.com")
    private String email;

    @Schema(description = "使用者角色", example = "USER")
    private String role;

    @Schema(description = "創建時間", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "更新時間", example = "2024-01-01T10:00:00")
    private LocalDateTime updatedAt;
}
