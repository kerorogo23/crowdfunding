# Spring Data JPA 命名規範重構總結

## 重構目標

按照 Spring Data JPA 命名規範，統一專案中的實體類別和 Repository 命名方式。

## 主要變更

### 1. 實體類別主鍵屬性統一

所有實體類別的主鍵屬性都改為 `id`，並使用 `@Column(name = "xxx_id")` 對應資料庫欄位：

-   **Project**: `projectId` → `id` (`@Column(name = "project_id")`)
-   **User**: `userId` → `id` (`@Column(name = "user_id")`)
-   **ProjectCategory**: `categoryId` → `id` (`@Column(name = "category_id")`)
-   **ProjectImage**: `imageId` → `id` (`@Column(name = "image_id")`)
-   **Investment**: `investmentId` → `id` (`@Column(name = "investment_id")`)
-   **Role**: `roleId` → `id` (`@Column(name = "role_id")`)

### 2. Repository 方法命名優化

使用下劃線屬性導航語法，讓方法名稱更清晰：

-   **ProjectRepository**:

    -   `findByCategoryId(Long categoryId)` → `findByCategory_Id(Long categoryId)`
    -   `existsByProjectIdAndCreator(Long projectId, User creator)` → `existsByIdAndCreator(Long id, User creator)`

-   **ProjectImageRepository**:
    -   `findByProjectIdOrderBySortOrderAsc(Long projectId)` → `findByProject_IdOrderBySortOrderAsc(Long projectId)`
    -   `findByProjectIdAndIsMainTrue(Long projectId)` → `findByProject_IdAndIsMainTrue(Long projectId)`
    -   `deleteByProjectId(Long projectId)` → `deleteByProject_Id(Long projectId)`

### 3. DTO 和 Service 層調整

更新所有相關的 DTO 和 Service 類別，使用新的屬性名稱：

-   **ProjectResponse**: `projectId` → `id`
-   **UserResponse**: 保持 `userId`（API 層面保持一致性）
-   **ProjectCategoryResponse**: 保持 `categoryId`（API 層面保持一致性）

### 4. 測試檔案更新

更新測試檔案中的相關方法調用。

## 重構的好處

1. **符合 Spring Data JPA 最佳實踐**：使用標準的命名規範
2. **提高可維護性**：方法名稱更清晰，避免冗長的命名
3. **保持資料庫命名習慣**：資料庫欄位仍使用 `xxx_id` 格式
4. **自動解析支援**：Spring Data JPA 能順利解析屬性導航

## 注意事項

1. **API 層面保持一致性**：雖然實體類別使用 `id`，但 DTO 中仍保持原有的命名以維持 API 穩定性
2. **資料庫遷移**：如果資料庫已存在，需要相應的遷移腳本
3. **測試覆蓋**：建議重新運行所有測試以確保功能正常

## 後續建議

1. 考慮更新 API 文檔
2. 檢查是否有遺漏的地方需要調整
3. 運行完整的測試套件
4. 更新相關的技術文檔
