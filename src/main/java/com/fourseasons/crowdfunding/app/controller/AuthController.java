package com.fourseasons.crowdfunding.app.controller;

import com.fourseasons.crowdfunding.app.dto.auth.AuthResponse;
import com.fourseasons.crowdfunding.app.dto.auth.LoginRequest;
import com.fourseasons.crowdfunding.app.dto.auth.RegisterRequest;
import com.fourseasons.crowdfunding.app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 認證控制器 提供使用者註冊、登入、登出等 API 端點
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "認證管理", description = "使用者註冊、登入、登出等認證相關 API")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 使用者註冊
     * 
     * @param registerRequest 註冊請求
     * @return 認證回應
     */
    @PostMapping("/register")
    @Operation(summary = "使用者註冊", description = "創建新的使用者帳戶")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "註冊成功", content = @Content(schema = @Schema(implementation = AuthResponse.class))) })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * 使用者登入
     * 
     * @param loginRequest 登入請求
     * @return 認證回應
     */
    @PostMapping("/login")
    @Operation(summary = "使用者登入", description = "驗證使用者憑證並返回 JWT Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登入成功", content = @Content(schema = @Schema(implementation = AuthResponse.class))) })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * 使用者登出
     * 
     * @return 登出成功訊息
     */
    @PostMapping("/logout")
    @Operation(summary = "使用者登出", description = "清除使用者的認證狀態")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登出成功") })
    public ResponseEntity<Map<String, String>> logout() {
        authService.logout();

        Map<String, String> response = new HashMap<>();
        response.put("message", "登出成功");

        return ResponseEntity.ok(response);
    }

    /**
     * 測試端點 - 檢查服務是否正常運行
     * 
     * @return 服務狀態
     */
    @GetMapping("/test")
    @Operation(summary = "服務健康檢查", description = "檢查認證服務是否正常運行")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "服務正常") })
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "認證服務正常運行");
        response.put("status", "OK");

        return ResponseEntity.ok(response);
    }
}
