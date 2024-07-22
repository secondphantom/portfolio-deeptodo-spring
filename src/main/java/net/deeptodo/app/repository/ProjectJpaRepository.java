package net.deeptodo.app.repository;

import net.deeptodo.app.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectJpaRepository extends JpaRepository<Project,Long> {
}
