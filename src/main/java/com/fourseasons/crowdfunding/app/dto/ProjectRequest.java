package com.fourseasons.crowdfunding.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 專案請求 DTO
 */
@Data
@Schema(description = "專案請求")
public class ProjectRequest {

    @NotBlank(message = "專案標題不能為空")
    @Size(min = 1, max = 200, message = "專案標題長度必須在 1-200 字元之間")
    @Schema(description = "專案標題", example = "環保科技創新專案", required = true)
    private String title;

    @NotBlank(message = "專案描述不能為空")
    @Size(min = 10, max = 5000, message = "專案描述長度必須在 10-5000 字元之間")
    @Schema(description = "專案描述", example = "這是一個致力於環保科技創新的專案...", required = true)
    private String description;

    @NotNull(message = "目標金額不能為空")
    @DecimalMin(value = "0.01", message = "目標金額必須大於 0")
    @Schema(description = "目標金額", example = "100000.00", required = true)
    private BigDecimal goalAmount;
}
