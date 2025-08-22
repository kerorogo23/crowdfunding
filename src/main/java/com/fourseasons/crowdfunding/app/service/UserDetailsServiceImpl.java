package com.fourseasons.crowdfunding.app.service;

import com.fourseasons.crowdfunding.app.entity.User;
import com.fourseasons.crowdfunding.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDetailsService 實作類
 * 負責載入使用者詳情以支援 Spring Security
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 嘗試根據使用者名稱或電子郵件查找使用者
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username)
                        .orElse(null));

        if (user == null) {
            throw new UsernameNotFoundException("找不到使用者: " + username);
        }

        return user;
    }

    /**
     * 根據使用者 ID 載入使用者詳情
     * 
     * @param userId 使用者 ID
     * @return 使用者詳情
     * @throws UsernameNotFoundException 如果找不到使用者
     */
    @Transactional
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("找不到使用者 ID: " + userId));

        return user;
    }
}
