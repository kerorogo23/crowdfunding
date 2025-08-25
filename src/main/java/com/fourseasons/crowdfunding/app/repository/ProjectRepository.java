package com.fourseasons.crowdfunding.app.repository;

import com.fourseasons.crowdfunding.app.entity.Project;
import com.fourseasons.crowdfunding.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 專案資料存取層
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

        /**
         * 根據創建者查詢專案
         */
        List<Project> findByCreator(User creator);

        /**
         * 根據創建者查詢專案（分頁）
         */
        Page<Project> findByCreator(User creator, Pageable pageable);

        /**
         * 根據狀態查詢專案
         */
        List<Project> findByStatus(Project.ProjectStatus status);

        /**
         * 根據狀態查詢專案（分頁）
         */
        Page<Project> findByStatus(Project.ProjectStatus status, Pageable pageable);

        /**
         * 根據創建者和狀態查詢專案
         */
        List<Project> findByCreatorAndStatus(User creator, Project.ProjectStatus status);

        /**
         * 根據創建者和狀態查詢專案（分頁）
         */
        Page<Project> findByCreatorAndStatus(User creator, Project.ProjectStatus status, Pageable pageable);

        /**
         * 搜尋專案（標題或描述包含關鍵字）
         */
        @Query("SELECT p FROM Project p WHERE " +
                        "(:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
                        "(:status IS NULL OR p.status = :status)")
        Page<Project> searchProjects(@Param("keyword") String keyword,
                        @Param("status") Project.ProjectStatus status,
                        Pageable pageable);

        /**
         * 進階搜尋專案（包含分類篩選）
         */
        @Query("SELECT p FROM Project p WHERE " +
                        "(:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
                        "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
                        "(:status IS NULL OR p.status = :status)")
        Page<Project> searchProjectsAdvanced(@Param("keyword") String keyword,
                        @Param("categoryId") Long categoryId,
                        @Param("status") Project.ProjectStatus status,
                        Pageable pageable);

        /**
         * 查詢已核准的專案（公開查詢）
         */
        @Query("SELECT p FROM Project p WHERE p.status = 'APPROVED' AND " +
                        "(:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        Page<Project> findApprovedProjects(@Param("keyword") String keyword, Pageable pageable);

        /**
         * 檢查專案是否屬於指定使用者
         */
        boolean existsByIdAndCreator(Long id, User creator);

        /**
         * 根據 ID 查詢專案（包含創建者資訊）
         */
        @Query("SELECT p FROM Project p LEFT JOIN FETCH p.creator WHERE p.id = :id")
        Optional<Project> findByIdWithCreator(@Param("id") Long id);

        /**
         * 根據分類查詢專案（使用下劃線屬性導航）
         */
        List<Project> findByCategory_Id(Long categoryId);

        /**
         * 查詢正在進行中的專案
         */
        @Query("SELECT p FROM Project p WHERE p.status = 'APPROVED' AND " +
                        "CURRENT_TIMESTAMP BETWEEN p.startDate AND p.endDate")
        List<Project> findActiveProjects();
}
