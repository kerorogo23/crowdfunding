# 專案模組 (Project Module)

## 概述

專案模組是群眾募資平台的核心功能之一，允許使用者創建、管理和瀏覽募資專案。該模組包含完整的 CRUD 操作、權限控制和狀態管理。

## 功能特色

### 1. 專案生命週期管理

- **草稿 (DRAFT)**: 創建者可以編輯和修改
- **待審核 (PENDING)**: 已提交給管理員審核
- **已核准 (APPROVED)**: 公開可見，可以接受捐款
- **已拒絕 (REJECTED)**: 被管理員拒絕

### 2. 權限控制

- **一般使用者**: 可以創建、編輯自己的專案，查看公開專案
- **管理員**: 可以查看所有專案，審核專案狀態，刪除任何專案

### 3. 搜尋和分頁

- 支援關鍵字搜尋（標題和描述）
- 分頁查詢
- 多種排序方式（最新、最熱門等）

## API 端點

### 專案管理

#### 創建專案

```
POST /api/projects
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "title": "環保科技創新專案",
  "description": "這是一個致力於環保科技創新的專案...",
  "goalAmount": 100000.00
}
```

#### 更新專案

```
PUT /api/projects/{id}
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json

{
  "title": "更新的專案標題",
  "description": "更新的專案描述...",
  "goalAmount": 150000.00
}
```

#### 刪除專案

```
DELETE /api/projects/{id}
Authorization: Bearer {JWT_TOKEN}
```

#### 查詢單一專案

```
GET /api/projects/{id}
Authorization: Bearer {JWT_TOKEN} (可選)
```

### 專案列表查詢

#### 查詢公開專案（無需認證）

```
GET /api/projects?keyword=環保&page=0&size=10&sortBy=createdAt&sortDir=desc
```

#### 查詢我的專案

```
GET /api/projects/my?page=0&size=10&sortBy=createdAt&sortDir=desc
Authorization: Bearer {JWT_TOKEN}
```

#### 管理員查詢所有專案

```
GET /api/projects/admin?keyword=環保&status=PENDING&page=0&size=10
Authorization: Bearer {JWT_TOKEN} (需要 ADMIN 權限)
```

### 專案狀態管理

#### 提交專案審核

```
POST /api/projects/{id}/submit
Authorization: Bearer {JWT_TOKEN}
```

#### 更新專案狀態（管理員）

```
PATCH /api/projects/{id}/status
Authorization: Bearer {JWT_TOKEN} (需要 ADMIN 權限)
Content-Type: application/json

{
  "status": "APPROVED"
}
```

## 資料模型

### Project 實體

```java
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "goal_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal goalAmount;

    @Column(name = "current_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.DRAFT;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

### 專案狀態列舉

```java
public enum ProjectStatus {
    DRAFT,      // 草稿
    PENDING,    // 待審核
    APPROVED,   // 已核准
    REJECTED    // 已拒絕
}
```

## 驗證規則

### 專案創建/更新驗證

- **標題**: 必填，長度 1-200 字元
- **描述**: 必填，長度 10-5000 字元
- **目標金額**: 必填，必須大於 0

### 權限驗證

- 只有專案創建者可以編輯自己的專案
- 只有草稿狀態的專案可以編輯
- 只有管理員可以審核專案狀態
- 只有已核准的專案或創建者可以查看專案詳情

## 錯誤處理

### HTTP 狀態碼

- **200**: 操作成功
- **201**: 創建成功
- **204**: 刪除成功
- **400**: 請求參數錯誤
- **401**: 未授權（JWT 無效）
- **403**: 權限不足
- **404**: 資源不存在

### 錯誤回應格式

```json
{
  "message": "錯誤訊息",
  "status": 400,
  "errors": {
    "fieldName": "欄位錯誤訊息"
  }
}
```

## 使用範例

### 1. 創建專案

```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "我的第一個專案",
    "description": "這是一個測試專案",
    "goalAmount": 50000.00
  }'
```

### 2. 查詢公開專案

```bash
curl -X GET "http://localhost:8080/api/projects?page=0&size=5&sortBy=createdAt&sortDir=desc"
```

### 3. 提交專案審核

```bash
curl -X POST http://localhost:8080/api/projects/1/submit \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 注意事項

1. **JWT 認證**: 除了查詢公開專案外，所有操作都需要有效的 JWT Token
2. **權限控制**: 確保使用者只能操作自己有權限的資源
3. **狀態管理**: 專案狀態轉換有嚴格的規則，不能隨意更改
4. **資料驗證**: 所有輸入都會進行驗證，確保資料完整性
5. **分頁查詢**: 建議使用分頁查詢來避免大量資料載入問題

## 未來擴展

- 專案分類和標籤功能
- 專案圖片和媒體檔案上傳
- 專案評論和評分系統
- 專案進度追蹤
- 專案統計和分析功能
