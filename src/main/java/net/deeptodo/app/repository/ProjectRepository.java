package net.deeptodo.app.repository;

import lombok.RequiredArgsConstructor;
import net.deeptodo.app.domain.Project;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {
    private final ProjectJpaRepository projectJpaRepository;

    public Long create(Project project) {
        projectJpaRepository.save(project);
        return project.getId();
    }

    public Optional<Project> getById(Long id) {
        return projectJpaRepository.findById(id);
    }

}
