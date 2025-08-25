package com.fourseasons.crowdfunding.app.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 專案分類回應 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCategoryResponse {

    private Long id;
    private String name;
    private String description;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
