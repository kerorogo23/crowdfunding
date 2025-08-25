package com.fourseasons.crowdfunding.app.service;

import com.fourseasons.crowdfunding.app.dto.project.ProjectCategoryResponse;
import com.fourseasons.crowdfunding.app.entity.ProjectCategory;
import com.fourseasons.crowdfunding.app.exception.ResourceNotFoundException;
import com.fourseasons.crowdfunding.app.repository.ProjectCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 專案分類服務類
 */
@Service
public class ProjectCategoryService {

    @Autowired
    private ProjectCategoryRepository categoryRepository;

    /**
     * 獲取所有啟用的分類
     * 
     * @return 分類列表
     */
    public List<ProjectCategoryResponse> getAllActiveCategories() {
        return categoryRepository.findByActiveTrue()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根據ID獲取分類
     * 
     * @param categoryId 分類ID
     * @return 分類資訊
     */
    public ProjectCategoryResponse getCategoryById(Long categoryId) {
        ProjectCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("分類不存在"));
        return convertToResponse(category);
    }

    /**
     * 創建新分類
     * 
     * @param category 分類實體
     * @return 創建的分類
     */
    public ProjectCategoryResponse createCategory(ProjectCategory category) {
        // 檢查名稱是否已存在
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("分類名稱已存在");
        }

        ProjectCategory savedCategory = categoryRepository.save(category);
        return convertToResponse(savedCategory);
    }

    /**
     * 更新分類
     * 
     * @param categoryId 分類ID
     * @param category   更新的分類資訊
     * @return 更新後的分類
     */
    public ProjectCategoryResponse updateCategory(Long categoryId, ProjectCategory category) {
        ProjectCategory existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("分類不存在"));

        // 檢查名稱是否與其他分類重複
        if (!existingCategory.getName().equals(category.getName()) &&
                categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("分類名稱已存在");
        }

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        existingCategory.setActive(category.isActive());

        ProjectCategory updatedCategory = categoryRepository.save(existingCategory);
        return convertToResponse(updatedCategory);
    }

    /**
     * 刪除分類（軟刪除）
     * 
     * @param categoryId 分類ID
     */
    public void deleteCategory(Long categoryId) {
        ProjectCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("分類不存在"));

        category.setActive(false);
        categoryRepository.save(category);
    }

    /**
     * 轉換為回應DTO
     * 
     * @param category 分類實體
     * @return 分類回應DTO
     */
    private ProjectCategoryResponse convertToResponse(ProjectCategory category) {
        return new ProjectCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt());
    }
}
