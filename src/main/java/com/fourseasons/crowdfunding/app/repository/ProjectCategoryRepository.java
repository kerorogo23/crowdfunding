package com.fourseasons.crowdfunding.app.repository;

import com.fourseasons.crowdfunding.app.entity.ProjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 專案分類資料存取介面
 */
@Repository
public interface ProjectCategoryRepository extends JpaRepository<ProjectCategory, Long> {

    /**
     * 根據名稱查找分類
     * 
     * @param name 分類名稱
     * @return 分類實體
     */
    Optional<ProjectCategory> findByName(String name);

    /**
     * 查找所有啟用的分類
     * 
     * @return 啟用的分類列表
     */
    List<ProjectCategory> findByActiveTrue();

    /**
     * 檢查分類名稱是否存在
     * 
     * @param name 分類名稱
     * @return true 如果存在
     */
    boolean existsByName(String name);
}
