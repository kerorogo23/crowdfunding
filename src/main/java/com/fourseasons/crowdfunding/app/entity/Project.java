package com.fourseasons.crowdfunding.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 專案實體類
 */
@Entity
@Table(name = "projects", indexes = {
        @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Comment("專案ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Comment("專案標題")
    @Column(nullable = false, length = 200)
    private String title;

    @Comment("專案描述")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Comment("目標金額")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal goalAmount;

    @Comment("目前募資金額")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Comment("專案創建者")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User creator;

    @Comment("專案狀態")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.DRAFT;

    @Comment("建立時間")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Comment("更新時間")
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * 專案狀態列舉
     */
    public enum ProjectStatus {
        /** 草稿 */
        DRAFT,
        /** 待審核 */
        PENDING,
        /** 已核准 */
        APPROVED,
        /** 已拒絕 */
        REJECTED // 已拒絕
    }
}
