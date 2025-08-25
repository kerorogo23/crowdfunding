package com.fourseasons.crowdfunding.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.hibernate.annotations.Comment;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * 使用者實體類
 * 實現 UserDetails 介面以支援 Spring Security
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Comment("使用者ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Comment("使用者名稱")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Comment("電子郵件")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Comment("密碼")
    @Column(nullable = false)
    private String password;

    @Comment("使用者角色")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Comment("建立時間")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Comment("更新時間")
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Comment("是否啟用")
    @Column(name = "is_enabled", nullable = false)
    private boolean enabled = true;

    @Comment("帳號是否過期")
    @Column(name = "is_account_non_expired", nullable = false)
    private boolean accountNonExpired = true;

    @Comment("帳號是否被鎖定")
    @Column(name = "is_account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    @Comment("憑證是否過期")
    @Column(name = "is_credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired = true;

    @Comment("登入失敗次數")
    @Column(name = "login_failure_count", nullable = false)
    private int loginFailureCount = 0;

    @Comment("最後登入失敗時間")
    @Column(name = "last_failure_time")
    private LocalDateTime lastFailureTime;

    // 創建的專案 - 使用 PERSIST 和 MERGE，避免刪除使用者時刪除專案
    @OneToMany(mappedBy = "creator", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<Project> createdProjects = new ArrayList<>();

    // 投資的專案 - 多對多關聯
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_project_investments", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> investedProjects = new ArrayList<>();

    // 投資記錄 - 一對多關聯
    // 使用 PERSIST 和 MERGE，避免刪除使用者時刪除投資記錄
    @OneToMany(mappedBy = "investor", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<Investment> investments = new ArrayList<>();

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

    // Spring Security UserDetails 介面實作
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 檢查手動鎖定狀態
        if (!accountNonLocked) {
            return false;
        }

        // 檢查自動鎖定：連續失敗5次後鎖定30分鐘
        if (loginFailureCount >= 5 && lastFailureTime != null) {
            LocalDateTime unlockTime = lastFailureTime.plusMinutes(30);
            if (LocalDateTime.now().isBefore(unlockTime)) {
                return false; // 仍在鎖定期間
            } else {
                // 鎖定時間已過，自動解除
                loginFailureCount = 0;
                lastFailureTime = null;
            }
        }

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // 業務方法
    /**
     * 手動鎖定帳號
     */
    public void lockAccount() {
        this.accountNonLocked = false;
    }

    /**
     * 手動解除帳號鎖定
     */
    public void unlockAccount() {
        this.accountNonLocked = true;
    }

    /**
     * 記錄登入失敗（用於自動鎖定機制）
     */
    public void recordLoginFailure() {
        this.loginFailureCount++;
        this.lastFailureTime = LocalDateTime.now();
    }

    /**
     * 重置登入失敗記錄（登入成功時呼叫）
     */
    public void resetLoginFailures() {
        this.loginFailureCount = 0;
        this.lastFailureTime = null;
    }

}
