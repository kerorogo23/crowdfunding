# Swagger/OpenAPI 文檔說明

## 概述

本專案已整合 Swagger/OpenAPI 3.0，提供完整的 API 文檔和測試介面。

## 訪問方式

### 本地開發環境

- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/api-docs

### 生產環境

- **Swagger UI**: https://api.fourseasons.com/swagger-ui.html
- **OpenAPI JSON**: https://api.fourseasons.com/api-docs

## API 分組

### 1. 認證管理 (Authentication)

- **POST** `/api/auth/register` - 使用者註冊
- **POST** `/api/auth/login` - 使用者登入
- **POST** `/api/auth/logout` - 使用者登出
- **GET** `/api/auth/test` - 服務健康檢查

### 2. 使用者管理 (User Management)

- **GET** `/api/user/me` - 獲取當前使用者資訊
- **GET** `/api/user/{userId}` - 根據 ID 獲取使用者資訊
- **GET** `/api/user/test` - 測試保護端點

### 3. 公開 API (Public API)

- **GET** `/api/public/health` - 健康檢查
- **GET** `/api/public/welcome` - 歡迎訊息

## 認證方式

### JWT Bearer Token

需要認證的 API 端點使用 JWT Bearer Token 認證：

1. 首先調用登入 API 獲取 Token
2. 在 Swagger UI 中點擊右上角的 "Authorize" 按鈕
3. 輸入 Token 格式：`Bearer {your-jwt-token}`
4. 點擊 "Authorize" 確認

### 範例 Token 格式

```
Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

## 使用步驟

### 1. 啟動應用程式

```bash
./gradlew bootRun
```

### 2. 訪問 Swagger UI

在瀏覽器中打開：http://localhost:8081/swagger-ui.html

### 3. 測試 API

1. 展開想要測試的 API 端點
2. 點擊 "Try it out" 按鈕
3. 填寫必要的參數
4. 點擊 "Execute" 執行

### 4. 認證測試

1. 先調用 `/api/auth/register` 註冊新使用者
2. 調用 `/api/auth/login` 登入獲取 Token
3. 點擊 "Authorize" 輸入 Token
4. 測試需要認證的端點

## 配置說明

### application.properties 配置

```properties
# Swagger/OpenAPI 設定
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.doc-expansion=none
```

### 依賴配置

```gradle
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0'
```

## 注意事項

1. **安全性**: 生產環境中請確保 Swagger UI 不會暴露敏感資訊
2. **CORS**: 已配置跨域支援，可在前端直接調用
3. **驗證**: 所有 API 都包含完整的參數驗證和錯誤處理
4. **文檔**: 每個 API 都有詳細的描述和範例

## 故障排除

### 常見問題

1. **無法訪問 Swagger UI**

   - 確認應用程式已啟動
   - 檢查端口是否被佔用
   - 確認防火牆設定

2. **認證失敗**

   - 確認 Token 格式正確
   - 檢查 Token 是否過期
   - 確認使用者權限

3. **API 調用失敗**
   - 檢查請求參數格式
   - 確認 Content-Type 設定
   - 查看應用程式日誌

## 開發建議

1. **保持文檔更新**: 修改 API 時請同步更新 Swagger 註解
2. **提供範例**: 為每個 API 提供實際可用的範例數據
3. **錯誤處理**: 確保所有可能的錯誤情況都有適當的響應碼
4. **版本控制**: 考慮 API 版本管理策略
