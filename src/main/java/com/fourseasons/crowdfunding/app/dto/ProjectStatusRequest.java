package com.fourseasons.crowdfunding.app.dto;

import com.fourseasons.crowdfunding.app.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 專案狀態更新請求 DTO
 */
@Data
@Schema(description = "專案狀態更新請求")
public class ProjectStatusRequest {

    @NotNull(message = "專案狀態不能為空")
    @Schema(description = "專案狀態", example = "APPROVED", required = true)
    private Project.ProjectStatus status;
}
