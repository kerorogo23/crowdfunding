# 專案模組完成總結

## 已完成的檔案

### 1. 實體類別 (Entities)

- ✅ `Project.java` - 專案實體類別
- ✅ 更新 `User.java` - 添加與專案的關聯

### 2. 資料存取層 (Repository)

- ✅ `ProjectRepository.java` - 專案資料存取介面

### 3. 資料傳輸物件 (DTOs)

- ✅ `ProjectRequest.java` - 專案創建/更新請求
- ✅ `ProjectResponse.java` - 專案回應物件
- ✅ `ProjectStatusRequest.java` - 專案狀態更新請求

### 4. 服務層 (Service)

- ✅ `ProjectService.java` - 專案業務邏輯服務

### 5. 控制器層 (Controller)

- ✅ `ProjectController.java` - 專案 REST API 控制器

### 6. 異常處理 (Exception)

- ✅ `ResourceNotFoundException.java` - 資源不存在異常
- ✅ `UnauthorizedException.java` - 未授權異常
- ✅ 更新 `GlobalExceptionHandler.java` - 添加新異常處理

### 7. 測試檔案

- ✅ `ProjectServiceTest.java` - 專案服務單元測試

### 8. 文件

- ✅ `PROJECT_MODULE_README.md` - 詳細功能說明
- ✅ `PROJECT_MODULE_SUMMARY.md` - 本總結文件

## 功能實現

### ✅ 核心功能

1. **專案 CRUD 操作**

   - 創建專案 (POST /api/projects)
   - 查詢專案 (GET /api/projects/{id})
   - 更新專案 (PUT /api/projects/{id})
   - 刪除專案 (DELETE /api/projects/{id})

2. **專案列表查詢**

   - 公開專案列表 (GET /api/projects)
   - 我的專案列表 (GET /api/projects/my)
   - 管理員專案列表 (GET /api/projects/admin)

3. **專案狀態管理**
   - 提交審核 (POST /api/projects/{id}/submit)
   - 更新狀態 (PATCH /api/projects/{id}/status)

### ✅ 權限控制

1. **使用者權限**

   - 一般使用者：創建、編輯自己的專案
   - 管理員：查看所有專案、審核狀態

2. **專案狀態控制**
   - DRAFT → PENDING → APPROVED/REJECTED
   - 只有草稿狀態可編輯
   - 只有已核准專案公開可見

### ✅ 驗證與錯誤處理

1. **輸入驗證**

   - 標題：1-200 字元
   - 描述：10-5000 字元
   - 目標金額：大於 0

2. **錯誤處理**
   - 400：請求參數錯誤
   - 401：未授權
   - 403：權限不足
   - 404：資源不存在

### ✅ 搜尋與分頁

1. **搜尋功能**

   - 關鍵字搜尋（標題、描述）
   - 狀態篩選

2. **分頁功能**
   - 支援分頁查詢
   - 多種排序方式

## API 端點總覽

| 方法   | 端點                        | 描述               | 權限              |
| ------ | --------------------------- | ------------------ | ----------------- |
| POST   | `/api/projects`             | 創建專案           | USER/ADMIN        |
| GET    | `/api/projects`             | 查詢公開專案       | 公開              |
| GET    | `/api/projects/{id}`        | 查詢單一專案       | 公開/創建者/ADMIN |
| PUT    | `/api/projects/{id}`        | 更新專案           | 創建者            |
| DELETE | `/api/projects/{id}`        | 刪除專案           | 創建者/ADMIN      |
| GET    | `/api/projects/my`          | 查詢我的專案       | USER/ADMIN        |
| GET    | `/api/projects/admin`       | 管理員查詢所有專案 | ADMIN             |
| POST   | `/api/projects/{id}/submit` | 提交專案審核       | 創建者            |
| PATCH  | `/api/projects/{id}/status` | 更新專案狀態       | ADMIN             |

## 資料庫結構

### projects 表

```sql
CREATE TABLE projects (
    project_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    goal_amount DECIMAL(15,2) NOT NULL,
    current_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
    creator_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(id)
);
```

## 技術特色

### ✅ Spring Boot 整合

- Spring Data JPA
- Spring Security
- Spring Validation
- OpenAPI 3 (Swagger)

### ✅ 安全性

- JWT 認證
- 角色基礎權限控制
- 輸入驗證和清理

### ✅ 可維護性

- 分層架構
- 單元測試
- 完整文件
- 統一異常處理

### ✅ 可擴展性

- 模組化設計
- 鬆耦合架構
- 支援未來功能擴展

## 使用方式

### 1. 啟動應用程式

```bash
./gradlew bootRun
```

### 2. 訪問 Swagger UI

```
http://localhost:8080/swagger-ui.html
```

### 3. 測試 API

```bash
# 創建專案
curl -X POST http://localhost:8080/api/projects \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "測試專案",
    "description": "這是一個測試專案",
    "goalAmount": 50000.00
  }'

# 查詢公開專案
curl -X GET "http://localhost:8080/api/projects?page=0&size=10"
```

## 下一步建議

1. **資料庫遷移**

   - 使用 Flyway 或 Liquibase 管理資料庫版本

2. **快取機制**

   - 添加 Redis 快取提升查詢效能

3. **檔案上傳**

   - 專案圖片和媒體檔案上傳功能

4. **通知系統**

   - 專案狀態變更通知

5. **審計日誌**

   - 記錄專案操作歷史

6. **效能優化**
   - 資料庫索引優化
   - 查詢效能調優

## 總結

專案模組已完整實現，包含：

- ✅ 完整的 CRUD 功能
- ✅ 嚴格的權限控制
- ✅ 完整的驗證機制
- ✅ 統一的錯誤處理
- ✅ 完整的 API 文件
- ✅ 單元測試覆蓋

該模組可以直接整合到現有的群眾募資平台中使用，並為後續功能擴展提供良好的基礎。
