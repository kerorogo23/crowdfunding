package com.fourseasons.crowdfunding.app.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 專案圖片回應 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectImageResponse {

    private Long imageId;
    private String imageUrl;
    private String altText;
    private boolean isMain;
    private int sortOrder;
    private LocalDateTime createdAt;
}
