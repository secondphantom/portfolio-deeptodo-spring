package net.deeptodo.app.repository.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import net.deeptodo.app.api.project.dto.GetProjectsByQueryDto;
import net.deeptodo.app.api.project.dto.QueryProjectDto;
import net.deeptodo.app.common.exception.CommonErrorCode;
import net.deeptodo.app.common.exception.ErrorCode;
import net.deeptodo.app.common.exception.InternalSeverErrorException;
import net.deeptodo.app.domain.Project;
import net.deeptodo.app.repository.project.dto.PartialUpdateProjectByIdAndUserIdDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {
    private final ProjectJpaRepository projectJpaRepository;
    private final ProjectQueryDslRepository projectQueryDslRepository;
    private final ProjectNativeQueryRepository projectNativeQueryRepository;

    public Long create(Project project) {
        projectJpaRepository.save(project);
        return project.getId();
    }

    public Optional<Project> getById(Long id) {
        return projectJpaRepository.findById(id);
    }

    public Optional<Project> getByIdAndUserId(Long id, Long userId) {
        return projectJpaRepository.findByIdAndUser_Id(id, userId);
    }

    public void deleteByIdAndUserId(Long id, Long userId) {
        projectJpaRepository.deleteByIdAndUser_id(id, userId);
    }

    public Optional<Integer> getVersionById(Long id) {
        return projectQueryDslRepository.findVersionById(id);
    }

    public Optional<Integer> getVersionByIdAndUserId(Long id, Long userid) {
        return projectQueryDslRepository.findVersionByIdAndUserId(id, userid);
    }

    public Optional<Long> getIdById(Long id) {
        return projectQueryDslRepository.findIdById(id);
    }

    public Optional<Long> getIdByIdAndUserId(Long id, Long userId) {
        return projectQueryDslRepository.findIdByIdAndUserId(id, userId);
    }

    public void partialUpdateByIdAndUserId(
            PartialUpdateProjectByIdAndUserIdDto partialUpdateProjectByIdAndUserIdDto
    ) {
        try {
            projectNativeQueryRepository.partialUpdateByIdAndUserId(partialUpdateProjectByIdAndUserIdDto);
        } catch (JsonProcessingException e) {
            throw new InternalSeverErrorException(ErrorCode.getErrorCode(CommonErrorCode.INTERNAL));
        }
    }

    public List<QueryProjectDto> getProjectsByQuery(GetProjectsByQueryDto dto, Long userId) {
        return projectQueryDslRepository.findProjectsByQuery(dto, userId);
    }

    public Long getCountByUserId(Long userId) {
        return projectJpaRepository.countByUser_id(userId);
    }

}
