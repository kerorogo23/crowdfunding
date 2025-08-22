package com.fourseasons.crowdfunding.app.controller;

import com.fourseasons.crowdfunding.app.dto.ProjectRequest;
import com.fourseasons.crowdfunding.app.dto.ProjectResponse;
import com.fourseasons.crowdfunding.app.dto.ProjectStatusRequest;
import com.fourseasons.crowdfunding.app.entity.Project;
import com.fourseasons.crowdfunding.app.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 專案控制器
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "專案管理", description = "專案相關 API")
@SecurityRequirement(name = "Bearer Authentication")
public class ProjectController {

        private final ProjectService projectService;

        /**
         * 創建專案
         * 
         * @param request 專案請求
         * @return 專案回應
         */
        @PostMapping
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
        @Operation(summary = "創建專案", description = "創建新的專案")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "專案創建成功", content = @Content(schema = @Schema(implementation = ProjectResponse.class))) })
        public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest request) {
                ProjectResponse response = projectService.createProject(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        /**
         * 更新專案
         * 
         * @param id      專案 ID
         * @param request 專案請求
         * @return 專案回應
         */
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
        @Operation(summary = "更新專案", description = "更新指定專案的資訊")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "專案更新成功", content = @Content(schema = @Schema(implementation = ProjectResponse.class))) })
        public ResponseEntity<ProjectResponse> updateProject(
                        @Parameter(name = "id", description = "專案 ID", example = "1", required = true) @PathVariable Long id,
                        @Valid @RequestBody ProjectRequest request) {
                ProjectResponse response = projectService.updateProject(id, request);
                return ResponseEntity.ok(response);
        }

        /**
         * 刪除專案
         * 
         * @param id 專案 ID
         * @return 無內容回應
         */
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
        @Operation(summary = "刪除專案", description = "刪除指定的專案")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "專案刪除成功") })
        public ResponseEntity<Void> deleteProject(
                        @Parameter(name = "id", description = "專案 ID", example = "1", required = true) @PathVariable Long id) {
                projectService.deleteProject(id);
                return ResponseEntity.noContent().build();
        }

        /**
         * 查詢單一專案
         * 
         * @param id 專案 ID
         * @return 專案回應
         */
        @GetMapping("/{id}")
        @Operation(summary = "查詢專案", description = "根據 ID 查詢專案詳細資訊")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "查詢成功", content = @Content(schema = @Schema(implementation = ProjectResponse.class))) })
        public ResponseEntity<ProjectResponse> getProject(
                        @Parameter(name = "id", description = "專案 ID", example = "1", required = true) @PathVariable Long id) {
                ProjectResponse response = projectService.getProjectById(id);
                return ResponseEntity.ok(response);
        }

        /**
         * 查詢專案列表（管理員功能）
         * 
         * @param keyword 搜尋關鍵字
         * @param status  專案狀態
         * @param page    頁碼
         * @param size    每頁大小
         * @param sortBy  排序欄位
         * @param sortDir 排序方向
         * @return 專案分頁回應
         */
        @GetMapping("/admin")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "查詢所有專案", description = "管理員查詢所有專案列表")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "查詢成功") })
        public ResponseEntity<Page<ProjectResponse>> getAllProjects(
                        @Parameter(description = "搜尋關鍵字") @RequestParam(required = false) String keyword,
                        @Parameter(description = "專案狀態") @RequestParam(required = false) Project.ProjectStatus status,
                        @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "排序欄位") @RequestParam(defaultValue = "createdAt") String sortBy,
                        @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String sortDir) {

                Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();
                Pageable pageable = PageRequest.of(page, size, sort);

                Page<ProjectResponse> projects = projectService.getProjects(keyword, status, pageable);
                return ResponseEntity.ok(projects);
        }

        /**
         * 查詢公開專案列表
         * 
         * @param keyword 搜尋關鍵字
         * @param page    頁碼
         * @param size    每頁大小
         * @param sortBy  排序欄位
         * @param sortDir 排序方向
         * @return 專案分頁回應
         */
        @GetMapping
        @Operation(summary = "查詢公開專案", description = "查詢已核准的公開專案列表")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "查詢成功") })
        public ResponseEntity<Page<ProjectResponse>> getPublicProjects(
                        @Parameter(description = "搜尋關鍵字") @RequestParam(required = false) String keyword,
                        @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "排序欄位") @RequestParam(defaultValue = "createdAt") String sortBy,
                        @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String sortDir) {

                Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();
                Pageable pageable = PageRequest.of(page, size, sort);

                Page<ProjectResponse> projects = projectService.getPublicProjects(keyword, pageable);
                return ResponseEntity.ok(projects);
        }

        /**
         * 查詢使用者的專案
         * 
         * @param page    頁碼
         * @param size    每頁大小
         * @param sortBy  排序欄位
         * @param sortDir 排序方向
         * @return 專案分頁回應
         */
        @GetMapping("/my")
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
        @Operation(summary = "查詢我的專案", description = "查詢當前使用者的專案列表")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "查詢成功") })
        public ResponseEntity<Page<ProjectResponse>> getMyProjects(
                        @Parameter(description = "頁碼") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "每頁大小") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "排序欄位") @RequestParam(defaultValue = "createdAt") String sortBy,
                        @Parameter(description = "排序方向") @RequestParam(defaultValue = "desc") String sortDir) {

                Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();
                Pageable pageable = PageRequest.of(page, size, sort);

                Page<ProjectResponse> projects = projectService.getUserProjects(pageable);
                return ResponseEntity.ok(projects);
        }

        /**
         * 更新專案狀態（管理員功能）
         * 
         * @param id      專案 ID
         * @param request 專案狀態請求
         * @return 專案回應
         */
        @PatchMapping("/{id}/status")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "更新專案狀態", description = "管理員更新專案狀態")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "狀態更新成功", content = @Content(schema = @Schema(implementation = ProjectResponse.class))) })
        public ResponseEntity<ProjectResponse> updateProjectStatus(
                        @Parameter(name = "id", description = "專案 ID", example = "1", required = true) @PathVariable Long id,
                        @Valid @RequestBody ProjectStatusRequest request) {
                ProjectResponse response = projectService.updateProjectStatus(id, request);
                return ResponseEntity.ok(response);
        }

        /**
         * 提交專案審核
         * 
         * @param id 專案 ID
         * @return 專案回應
         */
        @PostMapping("/{id}/submit")
        @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
        @Operation(summary = "提交專案審核", description = "將草稿專案提交給管理員審核")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "提交成功", content = @Content(schema = @Schema(implementation = ProjectResponse.class))) })
        public ResponseEntity<ProjectResponse> submitProjectForReview(
                        @Parameter(name = "id", description = "專案 ID", example = "1", required = true) @PathVariable Long id) {
                ProjectResponse response = projectService.submitProjectForReview(id);
                return ResponseEntity.ok(response);
        }
}
