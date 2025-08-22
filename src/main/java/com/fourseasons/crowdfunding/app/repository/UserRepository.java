package com.fourseasons.crowdfunding.app.repository;

import com.fourseasons.crowdfunding.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 使用者資料存取層
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根據電子郵件查詢使用者
     * @param email 電子郵件
     * @return 使用者選項
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 根據使用者名稱查詢使用者
     * @param username 使用者名稱
     * @return 使用者選項
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 檢查電子郵件是否已存在
     * @param email 電子郵件
     * @return 是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 檢查使用者名稱是否已存在
     * @param username 使用者名稱
     * @return 是否存在
     */
    boolean existsByUsername(String username);
}
