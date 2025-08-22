package com.fourseasons.crowdfunding.app.controller;

import com.fourseasons.crowdfunding.app.dto.UserResponse;
import com.fourseasons.crowdfunding.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 使用者控制器 提供使用者相關的 API 端點
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "使用者管理", description = "使用者資訊查詢和管理相關 API")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 獲取當前登入使用者的資訊 需要有效的 JWT Token
     * 
     * @return 使用者資訊
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "獲取當前使用者資訊", description = "根據 JWT Token 獲取當前登入使用者的詳細資訊")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功獲取使用者資訊", content = @Content(schema = @Schema(implementation = UserResponse.class))) })
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    /**
     * 根據使用者 ID 獲取使用者資訊 需要管理員權限
     * 
     * @param userId 使用者 ID
     * @return 使用者資訊
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "根據 ID 獲取使用者資訊", description = "管理員可以根據使用者 ID 查詢特定使用者的資訊")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功獲取使用者資訊", content = @Content(schema = @Schema(implementation = UserResponse.class))) })
    public ResponseEntity<UserResponse> getUserById(
            @io.swagger.v3.oas.annotations.Parameter(name = "userId", description = "使用者 ID", example = "1", required = true) @PathVariable Long userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * 測試保護端點 - 檢查認證是否正常
     * 
     * @return 認證狀態
     */
    @GetMapping("/test")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "測試保護端點", description = "測試 JWT 認證是否正常工作")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "認證成功") })
    public ResponseEntity<String> testProtectedEndpoint() {
        return ResponseEntity.ok("保護端點測試成功！您已通過 JWT 認證。");
    }
}
