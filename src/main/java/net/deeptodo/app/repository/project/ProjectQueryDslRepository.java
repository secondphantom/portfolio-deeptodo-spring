package net.deeptodo.app.repository.project;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static net.deeptodo.app.domain.QProject.project;

@Component
public class ProjectQueryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public Optional<Integer> findVersionById(Long projectId) {
        Integer version = queryFactory.select(project.version)
                .from(project)
                .where(project.id.eq(projectId))
                .fetchOne();
        return Optional.ofNullable(version);
    }

    public Optional<Integer> findVersionByIdAndUserId(Long projectId, Long userId) {
        Integer version = queryFactory.select(project.version)
                .from(project)
                .where(project.id.eq(projectId), project.user.id.eq(userId))
                .fetchOne();
        return Optional.ofNullable(version);
    }

    public Optional<Long> findIdById(Long projectId) {
        Long id = queryFactory.select(project.id)
                .from(project)
                .where(project.id.eq(projectId))
                .fetchOne();
        return Optional.ofNullable(id);
    }

    public Optional<Long> findIdByIdAndUserId(Long projectId, Long userId) {
        Long id = queryFactory.select(project.id)
                .from(project)
                .where(project.id.eq(projectId), project.user.id.eq(userId))
                .fetchOne();
        return Optional.ofNullable(id);
    }


}
