# Spring Boot 會員系統

這是一個基於 Spring Boot 3.x 和 JWT 的完整會員系統，提供使用者註冊、登入、登出等功能。

## 技術架構

- **Spring Boot 3.5.4** (Java 17)
- **Spring Security** - 安全框架
- **Spring Data JPA** - 資料存取層
- **MySQL** - 資料庫
- **JWT** - JSON Web Token 認證
- **BCrypt** - 密碼加密
- **Lombok** - 減少樣板程式碼

## 功能特色

### 認證功能

- ✅ 使用者註冊 (POST /api/auth/register)
- ✅ 使用者登入 (POST /api/auth/login)
- ✅ 使用者登出 (POST /api/auth/logout)
- ✅ JWT Token 驗證
- ✅ 密碼 BCrypt 加密

### 使用者管理

- ✅ 獲取當前使用者資訊 (GET /api/user/me)
- ✅ 根據 ID 獲取使用者資訊 (GET /api/user/{userId})
- ✅ 角色基礎權限控制

### 安全特性

- ✅ JWT 無狀態認證
- ✅ 密碼加密儲存
- ✅ 角色基礎授權
- ✅ CORS 支援
- ✅ 全域異常處理

## 快速開始

### 1. 環境需求

- Java 17+
- MySQL 8.0+
- Gradle 7.0+

### 2. 資料庫設定

1. 建立 MySQL 資料庫：

```sql
CREATE DATABASE crowdfunding_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改 `application.properties` 中的資料庫連線設定：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crowdfunding_db?useSSL=false&serverTimezone=Asia/Taipei
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. 啟動應用程式

```bash
# 使用 Gradle
./gradlew bootRun

# 或使用 IDE 直接執行 CrowdfundingApplication.java
```

### 4. 測試 API

應用程式啟動後，可以透過以下端點進行測試：

#### 健康檢查

```bash
curl http://localhost:8080/api/public/health
```

#### 使用者註冊

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### 使用者登入

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### 獲取使用者資訊 (需要 JWT Token)

```bash
curl -X GET http://localhost:8080/api/user/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## API 端點說明

### 認證端點

| 方法 | 端點                 | 描述         | 需要認證 |
| ---- | -------------------- | ------------ | -------- |
| POST | `/api/auth/register` | 使用者註冊   | ❌       |
| POST | `/api/auth/login`    | 使用者登入   | ❌       |
| POST | `/api/auth/logout`   | 使用者登出   | ❌       |
| GET  | `/api/auth/test`     | 測試認證服務 | ❌       |

### 使用者端點

| 方法 | 端點                 | 描述                   | 需要認證 | 需要角色   |
| ---- | -------------------- | ---------------------- | -------- | ---------- |
| GET  | `/api/user/me`       | 獲取當前使用者資訊     | ✅       | USER/ADMIN |
| GET  | `/api/user/{userId}` | 根據 ID 獲取使用者資訊 | ✅       | ADMIN      |
| GET  | `/api/user/test`     | 測試保護端點           | ✅       | USER/ADMIN |

### 公開端點

| 方法 | 端點                  | 描述     |
| ---- | --------------------- | -------- |
| GET  | `/api/public/health`  | 健康檢查 |
| GET  | `/api/public/welcome` | 歡迎訊息 |

## 請求/回應範例

### 註冊請求

```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}
```

### 註冊回應

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "username": "testuser",
  "email": "test@example.com",
  "role": "USER"
}
```

### 登入請求

```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

### 使用者資訊回應

```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "role": "USER",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

## 錯誤處理

系統提供統一的錯誤處理機制，常見錯誤回應：

### 驗證錯誤 (400)

```json
{
  "message": "驗證失敗",
  "errors": {
    "email": "請輸入有效的電子郵件格式",
    "password": "密碼長度必須在 6-100 個字元之間"
  },
  "status": 400
}
```

### 認證錯誤 (401)

```json
{
  "message": "電子郵件或密碼錯誤",
  "status": 401
}
```

### 權限錯誤 (403)

```json
{
  "message": "權限不足",
  "status": 403
}
```

## 安全考量

1. **JWT Secret Key**: 請在生產環境中修改 `application.properties` 中的 `jwt.secret`
2. **密碼加密**: 所有密碼都使用 BCrypt 進行加密
3. **Token 過期**: JWT Token 預設 24 小時過期
4. **CORS 設定**: 已配置跨域請求支援

## 開發指南

### 專案結構

```
src/main/java/com/fourseasons/crowdfunding/app/
├── config/          # 配置類
├── controller/      # 控制器
├── dto/            # 資料傳輸物件
├── entity/         # 實體類
├── exception/      # 異常處理
├── repository/     # 資料存取層
├── security/       # 安全相關
└── service/        # 業務邏輯層
```

### 擴展功能

1. **Token 黑名單**: 可以在 `AuthService.logout()` 中實作 Redis 黑名單機制
2. **密碼重設**: 可以新增密碼重設功能
3. **電子郵件驗證**: 可以新增電子郵件驗證功能
4. **社交登入**: 可以整合 OAuth2 社交登入

## 授權

本專案採用 MIT 授權條款。
