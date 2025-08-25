package com.fourseasons.crowdfunding.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 專案圖片實體類
 * 用於管理專案的圖片資源
 */
@Entity
@Table(name = "project_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectImage {

    @Comment("圖片ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Comment("所屬專案")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Comment("圖片URL")
    @Column(nullable = false)
    private String imageUrl;

    @Comment("圖片替代文字")
    @Column(length = 100)
    private String altText;

    @Comment("是否為主圖")
    @Column(nullable = false)
    private boolean isMain = false;

    @Comment("排序順序")
    @Column(nullable = false)
    private int sortOrder = 0;

    @Comment("建立時間")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Comment("更新時間")
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
