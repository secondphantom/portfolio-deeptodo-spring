package net.deeptodo.app.repository.project;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.api.project.dto.GetProjectsByQueryDto;
import net.deeptodo.app.api.project.dto.QueryProjectDto;
import net.deeptodo.app.domain.OauthServerType;
import net.deeptodo.app.domain.Project;
import net.deeptodo.app.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectQueryDslRepositoryTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private ProjectQueryDslRepository projectQueryDslRepository;

    @Test
    public void findVersionById() {
        //given
        User newUser = User.createNewUser("nickName", "email", "oauthServerId", OauthServerType.GOOGLE);
        em.persist(newUser);
        Project newProject = Project.createNewProject(newUser);
        em.persist(newProject);

        em.flush();

        //when
        Optional<Integer> version = projectQueryDslRepository.findVersionById(newProject.getId());

        //then
        assertThat(version.get()).isEqualTo(newProject.getVersion());
    }

    @Test
    public void findIdByIdAndUserId() {
        //given
        User newUser = User.createNewUser("nickName", "email", "oauthServerId", OauthServerType.GOOGLE);
        em.persist(newUser);
        Project newProject = Project.createNewProject(newUser);
        em.persist(newProject);

        em.flush();

        //when
        Optional<Long> id = projectQueryDslRepository.findIdByIdAndUserId(newProject.getId(), newUser.getId());

        //then

        assertThat(id.get()).isEqualTo(newProject.getId());
    }

    @Test
    public void findProjectsByQuery() {
        //given
        User newUser = User.createNewUser(
                "nickname",
                "email",
                "oauthServiceId",
                OauthServerType.GOOGLE
        );
        em.persist(newUser);
        Project project1 = Project.builder()
                .user(newUser)
                .title("first")
                .version(1)
                .root(List.of())
                .boards(Map.of())
                .todos(Map.of())
                .build();
        em.persist(project1);
        em.flush();
        Project project2 = Project.builder()
                .user(newUser)
                .title("second")
                .version(1)
                .root(List.of())
                .boards(Map.of())
                .todos(Map.of())
                .build();
        em.persist(project2);
        em.flush();
        em.clear();

        //when & then
        GetProjectsByQueryDto recentQuery = new GetProjectsByQueryDto(1, "recent", null);
        List<QueryProjectDto> recentProjects = projectQueryDslRepository.findProjectsByQuery(recentQuery, newUser.getId());

        assertThat(recentProjects).hasSize(2);
        assertThat(recentProjects.get(0).getTitle()).isEqualTo("second");
        assertThat(recentProjects.get(1).getTitle()).isEqualTo("first");
        em.clear();

        GetProjectsByQueryDto oldQuery = new GetProjectsByQueryDto(1, "old", null);
        List<QueryProjectDto> oldProjects = projectQueryDslRepository.findProjectsByQuery(oldQuery, newUser.getId());

        assertThat(oldProjects).hasSize(2);
        assertThat(oldProjects.get(0).getTitle()).isEqualTo("first");
        assertThat(oldProjects.get(1).getTitle()).isEqualTo("second");
        em.clear();

        GetProjectsByQueryDto searchQuery = new GetProjectsByQueryDto(1, "recent", "first");
        List<QueryProjectDto> searchProjects = projectQueryDslRepository.findProjectsByQuery(searchQuery, newUser.getId());

        assertThat(searchProjects).hasSize(1);
        assertThat(searchProjects.get(0).getTitle()).isEqualTo("first");
        em.clear();
    }


}