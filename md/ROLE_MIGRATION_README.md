# 角色系統遷移說明

## 概述

本次更新將原本使用 enum 的角色系統改為使用資料庫表格 `roles`，以提供更靈活的角色管理功能。

## 變更內容

### 1. 新增 Role 實體類別

-   檔案：`src/main/java/com/fourseasons/crowdfunding/app/entity/Role.java`
-   包含 `id` (主鍵) 和 `name` (角色名稱) 欄位
-   預定義角色常量：
    -   `GUEST` - 訪客
    -   `MEMBER` - 一般會員
    -   `CREATOR` - 專案發起人
    -   `ADMIN` - 管理員

### 2. 新增 RoleRepository

-   檔案：`src/main/java/com/fourseasons/crowdfunding/app/repository/RoleRepository.java`
-   提供角色資料庫操作功能
-   包含根據角色名稱查找角色的方法

### 3. 修改 User 實體類別

-   移除原本的 `enum Role`
-   將 `role` 欄位改為與 `Role` 實體的多對一關聯
-   更新 `getAuthorities()` 方法以使用新的角色結構

### 4. 更新相關服務類別

-   **AuthService**: 更新註冊和登入邏輯以使用新的角色系統
-   **UserService**: 更新使用者資訊轉換邏輯
-   新增 **DataInitializer**: 自動初始化角色資料

### 5. 資料庫初始化

-   檔案：`src/main/resources/insert_roles.sql`
-   插入預設角色資料
-   應用程式啟動時會自動創建表格並初始化資料

## 資料庫結構

### roles 表格

```sql
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
```

### users 表格變更

-   移除 `role` 欄位 (ENUM)
-   新增 `role_id` 外鍵欄位
-   建立與 `roles` 表格的外鍵關聯

## 預設角色資料

系統會自動插入以下角色：

1. `GUEST` - 訪客權限
2. `MEMBER` - 一般會員權限
3. `CREATOR` - 專案發起人權限
4. `ADMIN` - 管理員權限

## 新使用者註冊

-   新註冊的使用者預設會被指派 `MEMBER` 角色
-   可以透過管理介面或 API 來變更使用者角色

## 相容性

-   現有使用者的角色會被自動轉換為 `MEMBER`
-   API 回應格式保持不變，角色名稱仍然以字串形式返回
-   Spring Security 的授權機制繼續正常運作

## 部署注意事項

1. 執行資料庫遷移腳本
2. 重新啟動應用程式
3. 檢查角色資料是否正確初始化
4. 驗證現有使用者的角色轉換是否成功
