package net.deeptodo.app.repository.project;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.deeptodo.app.api.project.ProjectDefaultValue;
import net.deeptodo.app.api.project.dto.GetProjectsByQueryDto;
import net.deeptodo.app.api.project.dto.QueryProjectDto;
import net.deeptodo.app.api.project.dto.request.QueryOrder;
import net.deeptodo.app.repository.project.dto.ProjectIdAndVersionAndEnabledDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static net.deeptodo.app.domain.QProject.project;

@Component
public class ProjectQueryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    private static final int PAGE_SIZE = ProjectDefaultValue.PAGE_SIZE;

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

    public Optional<ProjectIdAndVersionAndEnabledDto> findVersionAndEnabledByIdAndUserId(Long projectId, Long userId) {
        return Optional.ofNullable(
                queryFactory.select(
                                Projections.constructor(ProjectIdAndVersionAndEnabledDto.class,
                                        project.id, project.version, project.enabled)
                        )
                        .from(project)
                        .where(project.id.eq(projectId), project.user.id.eq(userId))
                        .fetchOne());
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


    public List<QueryProjectDto> findProjectsByQuery(GetProjectsByQueryDto dto, Long userId) {

        BooleanExpression searchPredicate = createSearchPredicate(dto.search());
        BooleanExpression enabledPredicate = createEnabledPredicate(dto.enabled());
        OrderSpecifier<?> orderSpecifier = createOrderSpecifier(dto.order());

        return queryFactory.select(Projections.constructor(QueryProjectDto.class,
                        project.id,
                        project.title,
                        project.enabled,
                        project.createdAt,
                        project.updatedAt
                        ))
                .from(project)
                .where(searchPredicate, enabledPredicate,project.user.id.eq(userId))
                .orderBy(orderSpecifier)
                .offset((dto.page() - 1) * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression createSearchPredicate(String search) {
        if (search == null || search.isEmpty()) {
            return null;
        }
        return project.title.containsIgnoreCase(search);
    }

    private BooleanExpression createEnabledPredicate(Boolean enabled) {
        if (enabled == null) {
            return null;
        }
        return project.enabled.eq(enabled);
    }

    private OrderSpecifier<?> createOrderSpecifier(QueryOrder order) {
        switch (order) {
            case RECENT:
                return new OrderSpecifier<>(Order.DESC, project.updatedAt);
            case OLD:
                return new OrderSpecifier<>(Order.ASC, project.id);
            case CREATE:
                return new OrderSpecifier<>(Order.DESC, project.id);
            default:
                throw new IllegalArgumentException("Unknown order: " + order);
        }
    }
}
