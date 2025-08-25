package com.fourseasons.crowdfunding.app.service;

import com.fourseasons.crowdfunding.app.dto.project.ProjectRequest;
import com.fourseasons.crowdfunding.app.dto.project.ProjectResponse;
import com.fourseasons.crowdfunding.app.dto.project.ProjectStatusRequest;
import com.fourseasons.crowdfunding.app.dto.project.ProjectCategoryResponse;
import com.fourseasons.crowdfunding.app.entity.Project;
import com.fourseasons.crowdfunding.app.entity.User;
import com.fourseasons.crowdfunding.app.exception.ResourceNotFoundException;
import com.fourseasons.crowdfunding.app.exception.UnauthorizedException;
import com.fourseasons.crowdfunding.app.repository.ProjectRepository;
import com.fourseasons.crowdfunding.app.repository.UserRepository;
import com.fourseasons.crowdfunding.app.repository.ProjectCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 專案服務層
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectCategoryRepository categoryRepository;

    /**
     * 創建專案
     * 
     * @param request 專案請求
     * @return 專案回應
     */
    public ProjectResponse createProject(ProjectRequest request) {
        User currentUser = getCurrentUser();

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setGoalAmount(request.getGoalAmount());
        project.setCreator(currentUser);
        project.setStatus(Project.ProjectStatus.DRAFT);

        Project savedProject = projectRepository.save(project);
        return ProjectResponse.fromProject(savedProject);
    }

    /**
     * 更新專案
     * 
     * @param projectId 專案 ID
     * @param request   專案請求
     * @return 專案回應
     */
    public ProjectResponse updateProject(Long projectId, ProjectRequest request) {
        Project project = getProjectEntityById(projectId);
        User currentUser = getCurrentUser();

        // 檢查權限：只有創建者可以編輯
        if (!project.getCreator().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("您沒有權限編輯此專案");
        }

        // 檢查專案狀態：只有草稿狀態可以編輯
        if (project.getStatus() != Project.ProjectStatus.DRAFT) {
            throw new UnauthorizedException("只有草稿狀態的專案可以編輯");
        }

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setGoalAmount(request.getGoalAmount());

        Project updatedProject = projectRepository.save(project);
        return ProjectResponse.fromProject(updatedProject);
    }

    /**
     * 刪除專案
     * 
     * @param projectId 專案 ID
     */
    public void deleteProject(Long projectId) {
        Project project = getProjectEntityById(projectId);
        User currentUser = getCurrentUser();

        // 檢查權限：只有創建者或管理員可以刪除
        if (!project.getCreator().getId().equals(currentUser.getId())
                && !"ADMIN".equals(currentUser.getRole().getName())) {
            throw new UnauthorizedException("您沒有權限刪除此專案");
        }

        projectRepository.delete(project);
    }

    /**
     * 根據 ID 查詢專案
     * 
     * @param projectId 專案 ID
     * @return 專案回應
     */
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long projectId) {
        Project project = projectRepository.findByIdWithCreator(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("專案不存在"));

        // 檢查權限：只有已核准的專案或創建者可以查看
        User currentUser = getCurrentUser();
        if (project.getStatus() != Project.ProjectStatus.APPROVED
                && !project.getCreator().getId().equals(currentUser.getId())
                && !"ADMIN".equals(currentUser.getRole().getName())) {
            throw new UnauthorizedException("您沒有權限查看此專案");
        }

        return ProjectResponse.fromProject(project);
    }

    /**
     * 查詢專案列表（管理員功能）
     * 
     * @param keyword  搜尋關鍵字
     * @param status   專案狀態
     * @param pageable 分頁參數
     * @return 專案分頁回應
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getProjects(String keyword, Project.ProjectStatus status, Pageable pageable) {
        User currentUser = getCurrentUser();

        // 檢查權限：只有管理員可以查看所有專案
        if (!"ADMIN".equals(currentUser.getRole().getName())) {
            throw new UnauthorizedException("您沒有權限查看所有專案");
        }

        Page<Project> projects = projectRepository.searchProjects(keyword, status, pageable);
        return projects.map(ProjectResponse::fromProject);
    }

    /**
     * 查詢公開專案列表
     * 
     * @param keyword  搜尋關鍵字
     * @param pageable 分頁參數
     * @return 專案分頁回應
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getPublicProjects(String keyword, Pageable pageable) {
        Page<Project> projects = projectRepository.findApprovedProjects(keyword, pageable);
        return projects.map(ProjectResponse::fromProject);
    }

    /**
     * 查詢使用者的專案
     * 
     * @param pageable 分頁參數
     * @return 專案分頁回應
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> getUserProjects(Pageable pageable) {
        User currentUser = getCurrentUser();
        Page<Project> projects = projectRepository.findByCreator(currentUser, pageable);
        return projects.map(ProjectResponse::fromProject);
    }

    /**
     * 更新專案狀態（管理員功能）
     * 
     * @param projectId 專案 ID
     * @param request   專案狀態請求
     * @return 專案回應
     */
    public ProjectResponse updateProjectStatus(Long projectId, ProjectStatusRequest request) {
        Project project = getProjectEntityById(projectId);
        User currentUser = getCurrentUser();

        // 檢查權限：只有管理員可以更新狀態
        if (!"ADMIN".equals(currentUser.getRole().getName())) {
            throw new UnauthorizedException("您沒有權限更新專案狀態");
        }

        // 檢查狀態轉換是否有效
        if (!isValidStatusTransition(project.getStatus(), request.getStatus())) {
            throw new IllegalArgumentException("無效的狀態轉換");
        }

        project.setStatus(request.getStatus());
        Project updatedProject = projectRepository.save(project);
        return ProjectResponse.fromProject(updatedProject);
    }

    /**
     * 提交專案審核
     * 
     * @param projectId 專案 ID
     * @return 專案回應
     */
    public ProjectResponse submitProjectForReview(Long projectId) {
        Project project = getProjectEntityById(projectId);
        User currentUser = getCurrentUser();

        // 檢查權限：只有創建者可以提交審核
        if (!project.getCreator().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("您沒有權限提交此專案");
        }

        // 檢查專案狀態：只有草稿狀態可以提交
        if (project.getStatus() != Project.ProjectStatus.DRAFT) {
            throw new UnauthorizedException("只有草稿狀態的專案可以提交審核");
        }

        project.setStatus(Project.ProjectStatus.PENDING);
        Project updatedProject = projectRepository.save(project);
        return ProjectResponse.fromProject(updatedProject);
    }

    /**
     * 獲取當前使用者
     * 
     * @return 當前使用者
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new UnauthorizedException("使用者不存在"));
    }

    /**
     * 根據 ID 獲取專案實體
     * 
     * @param projectId 專案 ID
     * @return 專案實體
     */
    private Project getProjectEntityById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("專案不存在"));
    }

    /**
     * 進階搜尋專案
     * 
     * @param keyword    關鍵字
     * @param categoryId 分類ID
     * @param status     專案狀態
     * @param pageable   分頁資訊
     * @return 專案分頁結果
     */
    public Page<ProjectResponse> searchProjects(String keyword, Long categoryId, Project.ProjectStatus status,
            Pageable pageable) {
        Page<Project> projects = projectRepository.searchProjectsAdvanced(keyword, categoryId, status, pageable);
        return projects.map(ProjectResponse::fromProject);
    }

    /**
     * 獲取正在進行中的專案
     * 
     * @return 進行中的專案列表
     */
    public List<ProjectResponse> getActiveProjects() {
        List<Project> projects = projectRepository.findActiveProjects();
        return projects.stream()
                .map(ProjectResponse::fromProject)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 根據分類獲取專案
     * 
     * @param categoryId 分類ID
     * @return 專案列表
     */
    public List<ProjectResponse> getProjectsByCategory(Long categoryId) {
        List<Project> projects = projectRepository.findByCategoryId(categoryId);
        return projects.stream()
                .map(ProjectResponse::fromProject)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 檢查狀態轉換是否有效
     * 
     * @param currentStatus 當前狀態
     * @param newStatus     新狀態
     * @return 是否有效的狀態轉換
     */
    private boolean isValidStatusTransition(Project.ProjectStatus currentStatus, Project.ProjectStatus newStatus) {
        switch (currentStatus) {
            case DRAFT:
                return newStatus == Project.ProjectStatus.PENDING;
            case PENDING:
                return newStatus == Project.ProjectStatus.APPROVED || newStatus == Project.ProjectStatus.REJECTED;
            case APPROVED:
            case REJECTED:
                return false; // 已核准或拒絕的專案不能再改變狀態
            default:
                return false;
        }
    }
}
