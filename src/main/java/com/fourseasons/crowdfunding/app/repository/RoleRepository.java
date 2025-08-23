package com.fourseasons.crowdfunding.app.repository;

import com.fourseasons.crowdfunding.app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色資料庫操作介面
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 根據角色名稱查找角色
     * 
     * @param name 角色名稱
     * @return 角色實體
     */
    Optional<Role> findByName(String name);
}
