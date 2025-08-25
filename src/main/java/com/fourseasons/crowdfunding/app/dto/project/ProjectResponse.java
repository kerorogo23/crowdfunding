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

    @Schema(description = "目標金額", example = "100000")
    private BigDecimal goalAmount;

    @Schema(description = "目前金額", example = "25000")
    private BigDecimal currentAmount;

    @Schema(description = "創建者資訊")
    private UserResponse creator;

    @Schema(description = "專案分類資訊")
    private ProjectCategoryResponse category;

    @Schema(description = "專案狀態", example = "APPROVED")
    private String status;

    @Schema(description = "專案開始時間", example = "2024-01-01T10:00:00")
    private LocalDateTime startDate;

    @Schema(description = "專案結束時間", example = "2024-02-01T10:00:00")
    private LocalDateTime endDate;

    @Schema(description = "進度百分比", example = "25.5")
    private BigDecimal progressPercentage;

    @Schema(description = "剩餘天數", example = "15")
    private Long remainingDays;

    @Schema(description = "是否已達到目標", example = "false")
    private Boolean isGoalReached;

    @Schema(description = "是否正在進行中", example = "true")
    private Boolean isActive;

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
        response.setStartDate(project.getStartDate());
        response.setEndDate(project.getEndDate());
        response.setProgressPercentage(project.getProgressPercentage());
        response.setRemainingDays(project.getRemainingDays());
        response.setIsGoalReached(project.isGoalReached());
        response.setIsActive(project.isActive());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        // 轉換分類資訊
        if (project.getCategory() != null) {
            ProjectCategoryResponse categoryResponse = new ProjectCategoryResponse();
            categoryResponse.setId(project.getCategory().getId());
            categoryResponse.setName(project.getCategory().getName());
            categoryResponse.setDescription(project.getCategory().getDescription());
            categoryResponse.setActive(project.getCategory().isActive());
            categoryResponse.setCreatedAt(project.getCategory().getCreatedAt());
            categoryResponse.setUpdatedAt(project.getCategory().getUpdatedAt());
            response.setCategory(categoryResponse);
        }

        // 轉換創建者資訊
        if (project.getCreator() != null) {
            UserResponse creatorResponse = new UserResponse();
            creatorResponse.setId(project.getCreator().getId());
            creatorResponse.setUsername(project.getCreator().getUsername());
            creatorResponse.setEmail(project.getCreator().getEmail());
            creatorResponse.setRole(project.getCreator().getRole().toString());
            creatorResponse.setCreatedAt(project.getCreator().getCreatedAt());
            creatorResponse.setUpdatedAt(project.getCreator().getUpdatedAt());
            response.setCreator(creatorResponse);
        }

        return response;
    }
}
