package net.deeptodo.app.controller.v1.project;

import lombok.RequiredArgsConstructor;
import net.deeptodo.app.aop.auth.AuthUser;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.application.project.ProjectService;
import net.deeptodo.app.application.project.dto.response.CreateProjectResponse;
import net.deeptodo.app.application.project.dto.response.GetProjectByIdResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<CreateProjectResponse> createProject(
            @AuthUser AuthUserInfo authUserInfo
    ) {

        CreateProjectResponse createProjectResponse = projectService.createProject(authUserInfo);

        return ResponseEntity.ok(createProjectResponse);
    }

    @GetMapping
    public ResponseEntity<Void> getProjectsByQuery(
            @AuthUser AuthUserInfo authUserInfo
    ) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<GetProjectByIdResponse> getProjectById(
            @AuthUser AuthUserInfo authUserInfo,
            @PathVariable Long projectId
    ) {

        GetProjectByIdResponse getProjectByIdResponse
        = projectService.getProjectById(authUserInfo,projectId);

        return ResponseEntity.ok(getProjectByIdResponse);
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Void> updateProjectById() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProjectById() {
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{projectId}/version")
    public ResponseEntity<Void> getProjectVersionById() {
        return ResponseEntity.ok().build();
    }


}
