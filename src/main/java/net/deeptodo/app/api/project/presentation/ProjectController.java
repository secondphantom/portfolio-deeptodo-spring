package net.deeptodo.app.api.project.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.deeptodo.app.aop.auth.AuthUser;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.project.application.ProjectService;
import net.deeptodo.app.api.project.dto.GetProjectsByQueryDto;
import net.deeptodo.app.api.project.dto.request.PartialUpdateProjectRequest;
import net.deeptodo.app.api.project.dto.response.CreateProjectResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectByIdResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectVersionByIdResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectsByQueryResponse;
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


    @GetMapping("/{projectId}")
    public ResponseEntity<GetProjectByIdResponse> getProjectById(
            @AuthUser AuthUserInfo authUserInfo,
            @PathVariable Long projectId
    ) {

        GetProjectByIdResponse getProjectByIdResponse
                = projectService.getProjectById(authUserInfo, projectId);

        return ResponseEntity.ok(getProjectByIdResponse);
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Void> updateProjectById(
            @RequestBody @Valid PartialUpdateProjectRequest request,
            @AuthUser AuthUserInfo authUserInfo,
            @PathVariable Long projectId
    ) {

        projectService.updateProjectById(authUserInfo, projectId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProjectById(
            @AuthUser AuthUserInfo authUserInfo,
            @PathVariable Long projectId
    ) {

        projectService.deleteProjectById(authUserInfo, projectId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/version")
    public ResponseEntity<GetProjectVersionByIdResponse> getProjectVersionById(
            @AuthUser AuthUserInfo authUserInfo,
            @PathVariable Long projectId
    ) {

        GetProjectVersionByIdResponse projectVersionById = projectService.getProjectVersionById(authUserInfo, projectId);

        return ResponseEntity.ok(projectVersionById);
    }

    @GetMapping
    public ResponseEntity<GetProjectsByQueryResponse> getProjectsByQuery(
            @AuthUser AuthUserInfo authUserInfo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "recent") String order,
            @RequestParam String search
    ) {

        GetProjectsByQueryResponse projectsByQuery = projectService.getProjectsByQuery(
                authUserInfo,
                GetProjectsByQueryDto.builder().page(page).order(order).search(search).build()
        );
        return ResponseEntity.ok(projectsByQuery);
    }

}
