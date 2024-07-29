package net.deeptodo.app.repository.project;

import net.deeptodo.app.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdAndUser_Id(Long id, Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Project p WHERE p.id = :id AND p.user.id = :userId")
    void deleteByIdAndUser_id(Long id, Long userId);

    long countByUser_id(Long userId);
}
