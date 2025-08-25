package com.fourseasons.crowdfunding.app.controller;

import com.fourseasons.crowdfunding.app.dto.project.ProjectCategoryResponse;
import com.fourseasons.crowdfunding.app.entity.ProjectCategory;
import com.fourseasons.crowdfunding.app.service.ProjectCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 專案分類控制器
 */
@RestController
@RequestMapping("/api/categories")
@Tag(name = "專案分類", description = "專案分類相關 API")
public class ProjectCategoryController {

    @Autowired
    private ProjectCategoryService categoryService;

    /**
     * 獲取所有啟用的分類
     */
    @GetMapping
    @Operation(summary = "獲取所有啟用的分類", description = "獲取所有啟用的專案分類")
    public ResponseEntity<List<ProjectCategoryResponse>> getAllCategories() {
        List<ProjectCategoryResponse> categories = categoryService.getAllActiveCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * 根據ID獲取分類
     */
    @GetMapping("/{categoryId}")
    @Operation(summary = "根據ID獲取分類", description = "根據分類ID獲取詳細資訊")
    public ResponseEntity<ProjectCategoryResponse> getCategoryById(@PathVariable Long categoryId) {
        ProjectCategoryResponse category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    /**
     * 創建新分類（管理員功能）
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "創建新分類", description = "創建新的專案分類（僅管理員）")
    public ResponseEntity<ProjectCategoryResponse> createCategory(@RequestBody ProjectCategory category) {
        ProjectCategoryResponse createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(createdCategory);
    }

    /**
     * 更新分類（管理員功能）
     */
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新分類", description = "更新專案分類資訊（僅管理員）")
    public ResponseEntity<ProjectCategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody ProjectCategory category) {
        ProjectCategoryResponse updatedCategory = categoryService.updateCategory(categoryId, category);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * 刪除分類（管理員功能）
     */
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "刪除分類", description = "刪除專案分類（僅管理員）")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }
}
