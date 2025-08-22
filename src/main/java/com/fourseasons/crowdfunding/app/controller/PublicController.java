package com.fourseasons.crowdfunding.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 公開控制器
 * 提供不需要認證的 API 端點
 */
@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "公開 API", description = "不需要認證的公開 API 端點")
public class PublicController {

    /**
     * 健康檢查端點
     * 
     * @return 服務狀態
     */
    @GetMapping("/health")
    @Operation(summary = "健康檢查", description = "檢查服務是否正常運行")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "服務正常運行")
    })
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "服務正常運行");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity.ok(response);
    }

    /**
     * 歡迎端點
     * 
     * @return 歡迎訊息
     */
    @GetMapping("/welcome")
    @Operation(summary = "歡迎訊息", description = "獲取 API 歡迎訊息和版本資訊")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功獲取歡迎訊息")
    })
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "歡迎使用會員系統 API");
        response.put("version", "1.0.0");

        return ResponseEntity.ok(response);
    }
}
