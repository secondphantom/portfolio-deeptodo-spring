package net.deeptodo.app.api.project.application;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.project.dto.GetProjectsByQueryDto;
import net.deeptodo.app.api.project.dto.request.PartialUpdateProjectRequest;
import net.deeptodo.app.api.project.dto.response.CreateProjectResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectByIdResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectVersionAndEnabledByIdResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectsByQueryResponse;
import net.deeptodo.app.common.exception.ConflictException;
import net.deeptodo.app.common.exception.ForbiddenException;
import net.deeptodo.app.common.exception.NotFoundException;
import net.deeptodo.app.common.exception.UnauthorizedException;
import net.deeptodo.app.domain.PlanType;
import net.deeptodo.app.domain.Project;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.repository.project.ProjectRepository;
import net.deeptodo.app.repository.subscription.SubscriptionPlanRepository;
import net.deeptodo.app.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ProjectServiceTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Test
    public void createProject_success() {
        //given
        SubscriptionPlan plan = SubscriptionPlan.builder()
                .type(PlanType.FREE)
                .durationDays(1000)
                .id(1L)
                .maxProjectCount(10)
                .maxTodoCount(100)
                .build();
        subscriptionPlanRepository.create(plan);
        em.flush();
        em.clear();

        User newUser = User.createNewUser(null, null, null, null, plan);
        Long userId = userRepository.create(newUser);

        //when
        CreateProjectResponse response = projectService.createProject(new AuthUserInfo(userId));

        //then
        assertThat(response.projectId()).isNotNull();
    }

    @Test
    public void createProject_fail_not_found_user() {
        //when & then
        assertThatThrownBy(
                () -> projectService.createProject(new AuthUserInfo(2L))
        ).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    public void createProject_fail_forbidden_exceed_project_count() throws Exception {
        //given
        SubscriptionPlan plan = SubscriptionPlan.builder()
                .type(PlanType.FREE)
                .durationDays(1000)
                .id(1L)
                .maxProjectCount(1)
                .maxTodoCount(100)
                .build();
        subscriptionPlanRepository.create(plan);
        em.flush();
        em.clear();

        User newUser = User.createNewUser(null, null, null, null, plan);
        Long userId = userRepository.create(newUser);
        Project newProject = Project.createNewProject(newUser);
        projectRepository.create(newProject);
        em.flush();

        //when & then
        assertThatThrownBy(
                () -> projectService.createProject(new AuthUserInfo(userId))
        ).isInstanceOf(ForbiddenException.class);
    }


    @Test
    public void getProjectById_success() {
        //given
        User newUser = User.builder().build();
        userRepository.create(newUser);
        Project newProject = Project.createNewProject(newUser);
        projectRepository.create(newProject);

        //when
        GetProjectByIdResponse response = projectService.getProjectById(new AuthUserInfo(newUser.getId()), newProject.getId());

        //then
        assertThat(response.projectId()).isEqualTo(newProject.getId());
        assertThat(response.enabled()).isEqualTo(newProject.isEnabled());
    }

    @Test
    public void getProjectById_fail_not_found() {
        //when & then
        assertThatThrownBy(
                () -> projectService.getProjectById(new AuthUserInfo(1L), 1L)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void deleteProjectById_success() {
        //given
        User newUser = User.builder().build();
        userRepository.create(newUser);
        Project newProject = Project.createNewProject(newUser);
        projectRepository.create(newProject);

        //when & then
        projectService.deleteProjectById(new AuthUserInfo(newUser.getId()), newProject.getId());

        Optional<Project> findProject = projectRepository.getById(newProject.getId());

        assertThat(findProject).isEqualTo(Optional.empty());
    }

    @Test
    public void updateProjectById_success() {
        //given
        User newUser = User.builder().build();
        userRepository.create(newUser);
        Project newProject = Project.builder()
                .user(newUser)
                .version(99)
                .enabled(true)
                .build();
        projectRepository.create(newProject);

        PartialUpdateProjectRequest dto = PartialUpdateProjectRequest.builder()
                .version(99)
                .title("updated")
                .build();

        //when
        projectService.updateProjectById(new AuthUserInfo(newUser.getId()), newProject.getId(), dto);

        em.flush();
        em.clear();
        //then
        Project findProject = projectRepository.getById(newProject.getId()).get();

        assertThat(findProject.getId()).isEqualTo(newProject.getId());
        assertThat(findProject.getVersion()).isEqualTo(0);


    }


    @Test
    public void updateProjectById_fail_version_conflict() {
        //given
        User newUser = User.builder().build();
        userRepository.create(newUser);
        Project newProject = Project.builder()
                .user(newUser)
                .version(99)
                .build();
        projectRepository.create(newProject);

        PartialUpdateProjectRequest dto = PartialUpdateProjectRequest.builder().version(1).build();

        //when & then
        assertThatThrownBy(
                () -> projectService.updateProjectById(
                        new AuthUserInfo(newUser.getId()),
                        newProject.getId(),
                        dto)
        ).isInstanceOf(ConflictException.class);
    }

    @Test
    public void updateProjectById_fail_not_enabled_project() {
        //given
        User newUser = User.builder().build();
        userRepository.create(newUser);
        Project newProject = Project.builder()
                .user(newUser)
                .version(99)
                .enabled(false)
                .build();
        projectRepository.create(newProject);

        PartialUpdateProjectRequest dto = PartialUpdateProjectRequest
                .builder().version(99).build();

        //when & then
        assertThatThrownBy(
                () -> projectService.updateProjectById(
                        new AuthUserInfo(newUser.getId()),
                        newProject.getId(),
                        dto)
        ).isInstanceOf(ForbiddenException.class);
    }


    @Test
    public void getProjectVersionAndEnabledById_success() {
        //given
        User newUser = User.builder().build();
        userRepository.create(newUser);
        Project newProject = Project.createNewProject(newUser);
        projectRepository.create(newProject);

        //when
        GetProjectVersionAndEnabledByIdResponse response = projectService.getProjectVersionAndEnabledById(new AuthUserInfo(newUser.getId()), newProject.getId());

        //then
        assertThat(response.projectId()).isEqualTo(newProject.getId());
        assertThat(response.version()).isEqualTo(0);
    }

    @Test
    public void getProjectVersionAndEnabledById_fail_not_found() {
        //when & then
        assertThatThrownBy(
                () -> projectService.getProjectVersionAndEnabledById(new AuthUserInfo(1L), 1L)
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void getProjectsByQuery_success() {
        //given
        User newUser = User.builder().build();
        userRepository.create(newUser);
        Project newProject1 = Project.createNewProject(newUser);
        projectRepository.create(newProject1);
        Project newProject2 = Project.createNewProject(newUser);
        projectRepository.create(newProject2);

        //when
        GetProjectsByQueryDto queryDto = new GetProjectsByQueryDto(1, "recent", null);
        GetProjectsByQueryResponse response = projectService.getProjectsByQuery(new AuthUserInfo(newUser.getId()), queryDto);

        //then
        assertThat(response.projects()).hasSize(2);
        assertThat(response.pagination().currentPage()).isEqualTo(1);
        assertThat(response.pagination().pageSize()).isEqualTo(10);
    }
}