package com.fourseasons.crowdfunding.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 角色實體類
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Comment("角色ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("角色名稱")
    @Column(unique = true, nullable = false, length = 50)
    private String name;

    // 預定義的角色常量
    public static final String ROLE_GUEST = "GUEST";
    public static final String ROLE_MEMBER = "MEMBER";
    public static final String ROLE_CREATOR = "CREATOR";
    public static final String ROLE_ADMIN = "ADMIN";
}
