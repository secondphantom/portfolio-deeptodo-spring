package net.deeptodo.app.controller.v1.project;

import lombok.RequiredArgsConstructor;
import net.deeptodo.app.aop.auth.AuthUser;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.application.project.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Void> createProject() {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getProjectsByQuery(
            @AuthUser AuthUserInfo authUserInfo
    ) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Void> getProjectById() {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Void> updateProjectById() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProjectById() {
        return ResponseEntity.ok().build();
    }

}
