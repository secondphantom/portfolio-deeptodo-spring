package net.deeptodo.app.controller.project;

import lombok.RequiredArgsConstructor;
import net.deeptodo.app.application.project.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Void> createProject() {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getProjectsByQuery() {
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
