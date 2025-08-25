package com.fourseasons.crowdfunding.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 專案分類實體類
 * 用於對專案進行分類管理
 */
@Entity
@Table(name = "project_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCategory {

    @Comment("分類ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("分類名稱")
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Comment("分類描述")
    @Column(length = 200)
    private String description;

    @Comment("是否啟用")
    @Column(nullable = false)
    private boolean active = true;

    @Comment("建立時間")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Comment("更新時間")
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 該分類下的所有專案
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

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
