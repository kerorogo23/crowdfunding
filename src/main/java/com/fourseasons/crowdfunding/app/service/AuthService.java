package com.fourseasons.crowdfunding.app.service;

import com.fourseasons.crowdfunding.app.dto.auth.AuthResponse;
import com.fourseasons.crowdfunding.app.dto.auth.LoginRequest;
import com.fourseasons.crowdfunding.app.dto.auth.RegisterRequest;
import com.fourseasons.crowdfunding.app.entity.User;
import com.fourseasons.crowdfunding.app.repository.UserRepository;
import com.fourseasons.crowdfunding.app.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 認證服務類 處理使用者註冊、登入等認證相關業務邏輯
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 使用者註冊
     * 
     * @param registerRequest 註冊請求
     * @return 認證回應
     * @throws RuntimeException 如果註冊失敗
     */
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        // 檢查電子郵件是否已存在
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("電子郵件已被註冊");
        }

        // 檢查使用者名稱是否已存在
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("使用者名稱已被使用");
        }

        // 創建新使用者
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(User.Role.USER);

        // 儲存使用者
        User savedUser = userRepository.save(user);

        // 生成 JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", savedUser.getId());
        claims.put("role", savedUser.getRole().name());

        String token = jwtUtils.generateToken(savedUser, claims);

        return new AuthResponse(token, savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getRole().name());
    }

    /**
     * 使用者登入
     * 
     * @param loginRequest 登入請求
     * @return 認證回應
     * @throws RuntimeException 如果登入失敗
     */
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // 進行認證
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 獲取認證的使用者
            User user = (User) authentication.getPrincipal();

            // 生成 JWT Token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("role", user.getRole().name());

            String token = jwtUtils.generateToken(user, claims);

            return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());

        } catch (Exception e) {
            throw new RuntimeException("登入失敗：電子郵件或密碼錯誤");
        }
    }

    /**
     * 使用者登出 注意：在 JWT 無狀態架構中，登出主要由前端處理 後端可以選擇實作 Token 黑名單機制
     */
    public void logout() {
        SecurityContextHolder.clearContext();
        // 這裡可以實作 Token 黑名單機制
        // 例如將 Token 加入 Redis 黑名單
    }
}
