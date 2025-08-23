package com.fourseasons.crowdfunding.app.service;

import com.fourseasons.crowdfunding.app.dto.project.ProjectRequest;
import com.fourseasons.crowdfunding.app.dto.project.ProjectResponse;
import com.fourseasons.crowdfunding.app.dto.project.ProjectStatusRequest;
import com.fourseasons.crowdfunding.app.entity.Project;
import com.fourseasons.crowdfunding.app.entity.User;
import com.fourseasons.crowdfunding.app.exception.ResourceNotFoundException;
import com.fourseasons.crowdfunding.app.exception.UnauthorizedException;
import com.fourseasons.crowdfunding.app.repository.ProjectRepository;
import com.fourseasons.crowdfunding.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 專案服務測試
 */
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ProjectService projectService;

    private User testUser;
    private Project testProject;
    private ProjectRequest projectRequest;

    @BeforeEach
    void setUp() {
        // 設置測試使用者
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setRole(User.Role.USER);

        // 設置測試專案
        testProject = new Project();
        testProject.setProjectId(1L);
        testProject.setTitle("測試專案");
        testProject.setDescription("這是一個測試專案");
        testProject.setGoalAmount(new BigDecimal("100000"));
        testProject.setCurrentAmount(BigDecimal.ZERO);
        testProject.setCreator(testUser);
        testProject.setStatus(Project.ProjectStatus.DRAFT);
        testProject.setCreatedAt(LocalDateTime.now());
        testProject.setUpdatedAt(LocalDateTime.now());

        // 設置測試請求
        projectRequest = new ProjectRequest();
        projectRequest.setTitle("新專案");
        projectRequest.setDescription("這是一個新專案");
        projectRequest.setGoalAmount(new BigDecimal("50000"));

        // 設置 Security Context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testuser");
    }

    @Test
    void createProject_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        // Act
        ProjectResponse response = projectService.createProject(projectRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testProject.getTitle(), response.getTitle());
        assertEquals(testProject.getDescription(), response.getDescription());
        assertEquals(testProject.getGoalAmount(), response.getGoalAmount());
        assertEquals(Project.ProjectStatus.DRAFT.name(), response.getStatus());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void getProjectById_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(projectRepository.findByIdWithCreator(1L)).thenReturn(Optional.of(testProject));

        // Act
        ProjectResponse response = projectService.getProjectById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(testProject.getProjectId(), response.getProjectId());
        assertEquals(testProject.getTitle(), response.getTitle());
    }

    @Test
    void getProjectById_NotFound() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(projectRepository.findByIdWithCreator(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.getProjectById(1L);
        });
    }

    @Test
    void updateProject_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        // Act
        ProjectResponse response = projectService.updateProject(1L, projectRequest);

        // Assert
        assertNotNull(response);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void updateProject_Unauthorized() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("otheruser");
        testProject.setCreator(otherUser);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> {
            projectService.updateProject(1L, projectRequest);
        });
    }

    @Test
    void deleteProject_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));

        // Act
        projectService.deleteProject(1L);

        // Assert
        verify(projectRepository).delete(testProject);
    }

    @Test
    void getPublicProjects_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> projectPage = new PageImpl<>(List.of(testProject), pageable, 1);
        when(projectRepository.findApprovedProjects(anyString(), any(Pageable.class)))
                .thenReturn(projectPage);

        // Act
        Page<ProjectResponse> response = projectService.getPublicProjects("test", pageable);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void updateProjectStatus_Success() {
        // Arrange
        testUser.setRole(User.Role.ADMIN);
        testProject.setStatus(Project.ProjectStatus.PENDING);

        ProjectStatusRequest statusRequest = new ProjectStatusRequest();
        statusRequest.setStatus(Project.ProjectStatus.APPROVED);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        // Act
        ProjectResponse response = projectService.updateProjectStatus(1L, statusRequest);

        // Assert
        assertNotNull(response);
        assertEquals(Project.ProjectStatus.APPROVED.name(), response.getStatus());
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void submitProjectForReview_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        // Act
        ProjectResponse response = projectService.submitProjectForReview(1L);

        // Assert
        assertNotNull(response);
        assertEquals(Project.ProjectStatus.PENDING.name(), response.getStatus());
        verify(projectRepository).save(any(Project.class));
    }
}
