package com.fourseasons.crowdfunding.app.repository;

import com.fourseasons.crowdfunding.app.entity.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 專案圖片資料存取介面
 */
@Repository
public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {

    /**
     * 根據專案ID查找所有圖片
     * 
     * @param projectId 專案ID
     * @return 圖片列表
     */
    List<ProjectImage> findByProjectIdOrderBySortOrderAsc(Long projectId);

    /**
     * 查找專案的主圖
     * 
     * @param projectId 專案ID
     * @return 主圖
     */
    Optional<ProjectImage> findByProjectIdAndIsMainTrue(Long projectId);

    /**
     * 根據專案ID刪除所有圖片
     * 
     * @param projectId 專案ID
     */
    void deleteByProjectId(Long projectId);
}
