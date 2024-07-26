package net.deeptodo.app.repository.project;

import net.deeptodo.app.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdAndUser_Id(Long id, Long userId);

    void deleteByIdAndUser_id(Long id, Long userId);

    long countByUser_id(Long userId);
}
