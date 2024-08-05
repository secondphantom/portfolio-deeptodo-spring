package net.deeptodo.app.api.user.application;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.user.dto.request.UpdateUserProfileRequest;
import net.deeptodo.app.api.user.dto.response.GetUserProfileResponse;
import net.deeptodo.app.common.exception.UnauthorizedException;
import net.deeptodo.app.domain.Project;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.repository.project.ProjectRepository;
import net.deeptodo.app.repository.user.UserRepository;
import net.deeptodo.app.testutils.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void getUserProfile_success() throws Exception {
        //given
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);

        User newUser = EntityUtils.createNewUser(plan);
        em.persist(newUser);
        em.flush();
        em.clear();

        //when
        GetUserProfileResponse userProfile = userService.getUserProfile(new AuthUserInfo(newUser.getId()));

        //then
        assertThat(userProfile.nickName()).isEqualTo(newUser.getNickName());
        assertThat(userProfile.email()).isEqualTo(newUser.getEmail());
        assertThat(userProfile.avatarUrl()).isEqualTo(newUser.getAvatarUrl());
    }

    @Test
    public void getUserProfile_fail_not_found_user() throws Exception {
        //when & then
        assertThatThrownBy(
                () -> userService.getUserProfile(new AuthUserInfo(2L))
        ).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    public void updateUserProfile_success() throws Exception {
        //given
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);

        User newUser = EntityUtils.createNewUser(plan);
        em.persist(newUser);
        em.flush();
        em.clear();

        //when
        UpdateUserProfileRequest request = UpdateUserProfileRequest.builder().nickName("changed").avatarUrl("changed").build();
        userService.updateUserProfile(new AuthUserInfo(newUser.getId()), request);
        em.flush();
        em.clear();
        User user = userRepository.getById(newUser.getId()).get();
        //then
        assertThat(user.getNickName()).isEqualTo(request.nickName());
        assertThat(user.getAvatarUrl()).isEqualTo(request.avatarUrl());
    }

    @Test
    public void updateUserProfile_fail_not_found_user() throws Exception {
        //when & then
        UpdateUserProfileRequest request = UpdateUserProfileRequest.builder().nickName("changed").avatarUrl("changed").build();
        assertThatThrownBy(
                () -> userService.updateUserProfile(new AuthUserInfo(2L), request)
        ).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    public void deleteUser_success() throws Exception {
        //given
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);

        User newUser = EntityUtils.createNewUser(plan);
        em.persist(newUser);
        em.flush();
        em.clear();

        //when
        userService.deleteUser(new AuthUserInfo(newUser.getId()));
        em.flush();
        em.clear();
        Optional<User> user = userRepository.getById(newUser.getId());
        //then
        assertThat(user).isEqualTo(Optional.empty());
    }

    @Test
    public void deleteUser_success_cascade() throws Exception {
        //given
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);

        User newUser = EntityUtils.createNewUser(plan);
        em.persist(newUser);

        Project newProject = EntityUtils.createDefaultProject(Project.builder().build(), newUser);
        em.persist(newProject);
        em.flush();
        em.clear();

        //when
        userService.deleteUser(new AuthUserInfo(newUser.getId()));
        em.flush();
        em.clear();
        Optional<User> user = userRepository.getById(newUser.getId());
        Optional<Project> project = projectRepository.getById(newProject.getId());
        //then
        assertThat(user).isEqualTo(Optional.empty());
        assertThat(project).isEqualTo(Optional.empty());
    }

    @Test
    public void deleteUser_fail_not_found_user() throws Exception {
        //when & then
        assertThatThrownBy(
                () -> userService.deleteUser(new AuthUserInfo(2L))
        ).isInstanceOf(UnauthorizedException.class);
    }
}