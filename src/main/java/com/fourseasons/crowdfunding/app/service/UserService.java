package com.fourseasons.crowdfunding.app.service;

import com.fourseasons.crowdfunding.app.dto.UserResponse;
import com.fourseasons.crowdfunding.app.entity.User;
import com.fourseasons.crowdfunding.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 使用者服務類
 * 處理使用者相關業務邏輯
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 獲取當前登入使用者的資訊
     * 
     * @return 使用者回應 DTO
     * @throws RuntimeException 如果找不到使用者
     */
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElse(null));

        if (user == null) {
            throw new RuntimeException("找不到當前使用者");
        }

        return convertToUserResponse(user);
    }

    /**
     * 根據使用者 ID 獲取使用者資訊
     * 
     * @param userId 使用者 ID
     * @return 使用者回應 DTO
     * @throws RuntimeException 如果找不到使用者
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("找不到使用者 ID: " + userId));

        return convertToUserResponse(user);
    }

    /**
     * 將 User 實體轉換為 UserResponse DTO
     * 
     * @param user 使用者實體
     * @return 使用者回應 DTO
     */
    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
