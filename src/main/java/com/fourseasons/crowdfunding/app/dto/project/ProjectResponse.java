package com.fourseasons.crowdfunding.app.dto.project;

import com.fourseasons.crowdfunding.app.dto.user.UserResponse;
import com.fourseasons.crowdfunding.app.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 專案回應 DTO
 */
@Data
@Schema(description = "專案資訊回應")
public class ProjectResponse {

    @Schema(description = "專案 ID", example = "1")
    private Long projectId;

    @Schema(description = "專案標題", example = "環保科技創新專案")
    private String title;

    @Schema(description = "專案描述", example = "這是一個致力於環保科技創新的專案...")
    private String description;

    @Schema(description = "目標金額", example = "100000.00")
    private BigDecimal goalAmount;

    @Schema(description = "目前金額", example = "25000.00")
    private BigDecimal currentAmount;

    @Schema(description = "創建者資訊")
    private UserResponse creator;

    @Schema(description = "專案狀態", example = "APPROVED")
    private String status;

    @Schema(description = "創建時間", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "更新時間", example = "2024-01-01T10:00:00")
    private LocalDateTime updatedAt;

    /**
     * 從 Project 實體轉換為 ProjectResponse
     */
    public static ProjectResponse fromProject(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(project.getProjectId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setGoalAmount(project.getGoalAmount());
        response.setCurrentAmount(project.getCurrentAmount());
        response.setStatus(project.getStatus().name());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        // 轉換創建者資訊
        if (project.getCreator() != null) {
            UserResponse creatorResponse = new UserResponse();
            creatorResponse.setId(project.getCreator().getId());
            creatorResponse.setUsername(project.getCreator().getUsername());
            creatorResponse.setEmail(project.getCreator().getEmail());
            creatorResponse.setRole(project.getCreator().getRole().name());
            creatorResponse.setCreatedAt(project.getCreator().getCreatedAt());
            creatorResponse.setUpdatedAt(project.getCreator().getUpdatedAt());
            response.setCreator(creatorResponse);
        }

        return response;
    }
}
