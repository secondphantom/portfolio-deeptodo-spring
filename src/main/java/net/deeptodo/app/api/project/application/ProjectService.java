package net.deeptodo.app.api.project.application;

import lombok.RequiredArgsConstructor;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.project.ProjectDefaultValue;
import net.deeptodo.app.api.project.dto.GetProjectsByQueryDto;
import net.deeptodo.app.api.project.dto.Pagination;
import net.deeptodo.app.api.project.dto.QueryProjectDto;
import net.deeptodo.app.api.project.dto.request.CreateProjectRequest;
import net.deeptodo.app.api.project.dto.request.PartialUpdateProjectRequest;
import net.deeptodo.app.api.project.dto.response.CreateProjectResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectByIdResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectVersionAndEnabledByIdResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectsByQueryResponse;
import net.deeptodo.app.api.project.exception.ProjectErrorCode;
import net.deeptodo.app.common.exception.ConflictException;
import net.deeptodo.app.common.exception.ForbiddenException;
import net.deeptodo.app.common.exception.NotFoundException;
import net.deeptodo.app.common.exception.UnauthorizedException;
import net.deeptodo.app.domain.Project;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.repository.project.ProjectRepository;
import net.deeptodo.app.repository.project.dto.PartialUpdateProjectByIdAndUserIdDto;
import net.deeptodo.app.repository.project.dto.ProjectIdAndVersionAndEnabledDto;
import net.deeptodo.app.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateProjectResponse createProject(AuthUserInfo authUserInfo, CreateProjectRequest request) {

        User user = userRepository.getById(authUserInfo.userId())
                .orElseThrow(() -> new UnauthorizedException(ProjectErrorCode.getErrorCode(ProjectErrorCode.UNAUTHORIZED_NOT_FOUND_MEMBER)));

        Long projectCount = projectRepository.getCountByUserId(authUserInfo.userId());
        if (!user.canCreateProject(projectCount)) {
            throw new ForbiddenException(ProjectErrorCode.getErrorCode(ProjectErrorCode.FORBIDDEN_CREATE_PROJECT));
        }

        Project newProject = Project.createNewProject(user,request.title());

        Long projectId = projectRepository.create(newProject);

        return new CreateProjectResponse(projectId);
    }


    public GetProjectByIdResponse getProjectById(AuthUserInfo authUserInfo, Long projectId) {

        Project findProject = projectRepository.getByIdAndUserId(projectId, authUserInfo.userId())
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.getErrorCode(ProjectErrorCode.NOT_FOUND_PROJECT)));

        return GetProjectByIdResponse.fromProject(findProject);
    }

    public void deleteProjectById(AuthUserInfo authUserInfo, Long projectId) {
        Long id = projectRepository.getIdByIdAndUserId(projectId, authUserInfo.userId())
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.getErrorCode(ProjectErrorCode.NOT_FOUND_PROJECT)));

        projectRepository.deleteByIdAndUserId(id, authUserInfo.userId());
    }

    public GetProjectVersionAndEnabledByIdResponse updateProjectById(
            AuthUserInfo authUserInfo,
            Long projectId,
            PartialUpdateProjectRequest partialUpdateProjectRequest
    ) {

        ProjectIdAndVersionAndEnabledDto projectIdAndVersionAndEnabledDto =
                getProjectVersionAndEnabledWithCheck(projectId, authUserInfo.userId(), partialUpdateProjectRequest.version());

        PartialUpdateProjectByIdAndUserIdDto dto = PartialUpdateProjectByIdAndUserIdDto.builder()
                .projectId(projectId)
                .userId(authUserInfo.userId())
                .version(getNextVersion(projectIdAndVersionAndEnabledDto.version()))
                .title(partialUpdateProjectRequest.title())
                .root(partialUpdateProjectRequest.root())
                .boards(partialUpdateProjectRequest.boards())
                .todos(partialUpdateProjectRequest.todos())
                .build();

        projectRepository.partialUpdateByIdAndUserId(dto);

        return GetProjectVersionAndEnabledByIdResponse.of(dto.projectId(), dto.version(), projectIdAndVersionAndEnabledDto.enabled());

    }

    private ProjectIdAndVersionAndEnabledDto getProjectVersionAndEnabledWithCheck(Long projectId, Long userId, Integer currentVersion) {
        ProjectIdAndVersionAndEnabledDto dto = projectRepository.getVersionAndEnabledByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.getErrorCode(ProjectErrorCode.NOT_FOUND_PROJECT)));

        if (!currentVersion.equals(dto.version())) {
            throw new ConflictException(ProjectErrorCode.getErrorCode(ProjectErrorCode.CONFLICT_PROJECT_VERSION));
        }
        if (dto.enabled() == false) {
            throw new ForbiddenException(ProjectErrorCode.getErrorCode(ProjectErrorCode.FORBIDDEN_CREATE_PROJECT));
        }

        return dto;
    }

    private Integer getNextVersion(Integer currentVersion) {
        final int VERSION_RANGE = 100;

        Integer nextVersion = (currentVersion + 1) % VERSION_RANGE;

        return nextVersion;
    }

    public GetProjectVersionAndEnabledByIdResponse getProjectVersionAndEnabledById(AuthUserInfo authUserInfo, Long projectId) {
        ProjectIdAndVersionAndEnabledDto dto = projectRepository.getVersionAndEnabledByIdAndUserId(projectId, authUserInfo.userId())
                .orElseThrow(() -> new NotFoundException(ProjectErrorCode.getErrorCode(ProjectErrorCode.NOT_FOUND_PROJECT)));

        return GetProjectVersionAndEnabledByIdResponse.of(projectId, dto.version(), dto.enabled());
    }


    public GetProjectsByQueryResponse getProjectsByQuery(AuthUserInfo authUserInfo, GetProjectsByQueryDto queryDto
    ) {

        List<QueryProjectDto> projects = projectRepository.getProjectsByQuery(queryDto, authUserInfo.userId());

        return GetProjectsByQueryResponse.of(
                projects,
                Pagination.builder()
                        .currentPage(queryDto.page())
                        .pageSize(ProjectDefaultValue.PAGE_SIZE)
                        .build());
    }
}
