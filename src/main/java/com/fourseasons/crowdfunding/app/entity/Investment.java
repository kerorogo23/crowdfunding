package com.fourseasons.crowdfunding.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 投資記錄實體類
 * 展示複雜的關聯關係
 */
@Entity
@Table(name = "investments", indexes = {
        @Index(name = "idx_user_project", columnList = "user_id, project_id"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Investment {

    @Comment("投資記錄ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("投資金額")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Comment("投資者")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User investor;

    @Comment("投資的專案")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Comment("投資狀態")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestmentStatus status = InvestmentStatus.PENDING;

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
     * 投資狀態列舉
     */
    public enum InvestmentStatus {
        /** 待處理 */
        PENDING,
        /** 已確認 */
        CONFIRMED,
        /** 已取消 */
        CANCELLED,
        /** 已退款 */
        REFUNDED
    }
}
