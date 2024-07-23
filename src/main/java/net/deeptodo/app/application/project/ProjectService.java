package net.deeptodo.app.application.project;

import lombok.RequiredArgsConstructor;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.application.project.dto.request.PartialUpdateProjectRequest;
import net.deeptodo.app.application.project.dto.response.CreateProjectResponse;
import net.deeptodo.app.application.project.dto.response.GetProjectByIdResponse;
import net.deeptodo.app.application.project.exception.ProjectErrorCode;
import net.deeptodo.app.common.exception.ConflictException;
import net.deeptodo.app.common.exception.NotFoundException;
import net.deeptodo.app.common.exception.UnauthorizedException;
import net.deeptodo.app.domain.Project;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.repository.project.ProjectRepository;
import net.deeptodo.app.repository.project.dto.PartialUpdateProjectByIdAndUserIdDto;
import net.deeptodo.app.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateProjectResponse createProject(AuthUserInfo authUserInfo) {

        User user = userRepository.getById(authUserInfo.userId())
                .orElseThrow(() -> new UnauthorizedException(ProjectErrorCode.getErrorCode(ProjectErrorCode.UNAUTHORIZED_NOT_FOUND_MEMBER)));

        Project newProject = Project.createNewProject(user);

        Long projectId = projectRepository.create(newProject);

        return new CreateProjectResponse(projectId);
    }


    public GetProjectByIdResponse getProjectById(AuthUserInfo authUserInfo, Long projectId) {

        Project findProject = projectRepository.getByIdAndUserId(projectId, authUserInfo.userId())
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.getErrorCode(ProjectErrorCode.NOT_FOUND_PROJECT)));

        return GetProjectByIdResponse.createResponseByProject(findProject);
    }

    public void deleteProjectById(AuthUserInfo authUserInfo, Long projectId) {
        Long id = projectRepository.getIdByIdAndUserId(projectId, authUserInfo.userId())
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.getErrorCode(ProjectErrorCode.NOT_FOUND_PROJECT)));

        projectRepository.deleteByIdAndUserId(id, authUserInfo.userId());
    }

    public void updateProjectById(
            AuthUserInfo authUserInfo,
            Long projectId,
            PartialUpdateProjectRequest partialUpdateProjectRequest
    ) {

        Integer nextProjectVersion = getNextProjectVersion(projectId, authUserInfo.userId(), partialUpdateProjectRequest.version());

        PartialUpdateProjectByIdAndUserIdDto dto = PartialUpdateProjectByIdAndUserIdDto.builder()
                .projectId(projectId)
                .userId(authUserInfo.userId())
                .version(nextProjectVersion)
                .title(partialUpdateProjectRequest.title())
                .root(partialUpdateProjectRequest.root())
                .boards(partialUpdateProjectRequest.boards())
                .todos(partialUpdateProjectRequest.todos())
                .build();

        projectRepository.partialUpdateByIdAndUserId(dto);

    }

    private Integer getNextProjectVersion(Long projectId, Long userId, Integer currentVersion) {
        Integer dbVersion = projectRepository.getVersionByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.getErrorCode(ProjectErrorCode.NOT_FOUND_PROJECT)));

        if (!currentVersion.equals(dbVersion)) {
            throw new ConflictException(ProjectErrorCode.getErrorCode(ProjectErrorCode.CONFLICT_PROJECT_VERSION));
        }

        final Integer VERSION_RANGE = 100;

        Integer nextVersion = (dbVersion + 1) % VERSION_RANGE;

        return nextVersion;
    }


}
