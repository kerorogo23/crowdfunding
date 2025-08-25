package com.fourseasons.crowdfunding.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import com.fourseasons.crowdfunding.app.entity.Investment;

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
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Comment("專案分類")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProjectCategory category;

    @Comment("專案開始時間")
    @Column(nullable = false)
    private LocalDateTime startDate;

    @Comment("專案結束時間")
    @Column(nullable = false)
    private LocalDateTime endDate;

    @Comment("平台費用比例")
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal platformFee = new BigDecimal("5.00"); // 預設 5% 平台費

    // 投資者列表 - 多對多關聯的另一端
    @ManyToMany(mappedBy = "investedProjects", fetch = FetchType.LAZY)
    private List<User> investors = new ArrayList<>();

    // 投資記錄 - 一對多關聯
    // 使用 PERSIST 和 MERGE，避免刪除專案時刪除投資記錄
    @OneToMany(mappedBy = "project", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<Investment> investments = new ArrayList<>();

    // 專案圖片 - 一對多關聯
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectImage> images = new ArrayList<>();

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
     * 檢查專案是否正在進行中
     * 
     * @return true 如果專案已核准且在有效期間內
     */
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return status == ProjectStatus.APPROVED &&
                now.isAfter(startDate) &&
                now.isBefore(endDate);
    }

    /**
     * 計算專案進度百分比
     * 
     * @return 進度百分比 (0-100)
     */
    public BigDecimal getProgressPercentage() {
        if (goalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentAmount.divide(goalAmount, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    /**
     * 計算剩餘天數
     * 
     * @return 剩餘天數，如果已結束則返回 0
     */
    public long getRemainingDays() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endDate)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(now, endDate);
    }

    /**
     * 檢查專案是否已達到目標金額
     * 
     * @return true 如果已達到或超過目標金額
     */
    public boolean isGoalReached() {
        return currentAmount.compareTo(goalAmount) >= 0;
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
        REJECTED
    }
}
