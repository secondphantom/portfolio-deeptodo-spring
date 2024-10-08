package net.deeptodo.app.repository.project;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.api.project.dto.GetProjectsByQueryDto;
import net.deeptodo.app.api.project.dto.QueryProjectDto;
import net.deeptodo.app.domain.Project;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.repository.project.dto.ProjectIdAndVersionAndEnabledDto;
import net.deeptodo.app.testutils.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);
        User newUser = EntityUtils.createNewUser(plan);
        em.persist(newUser);
        Project newProject = EntityUtils.createDefaultProject(Project.builder().build(), newUser);
        em.persist(newProject);
        em.flush();
        em.clear();

        //when
        Optional<Integer> version = projectQueryDslRepository.findVersionById(newProject.getId());

        //then
        assertThat(version.get()).isEqualTo(newProject.getVersion());
    }

    @Test
    public void findIdByIdAndUserId() {
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
        Optional<Long> id = projectQueryDslRepository.findIdByIdAndUserId(newProject.getId(), newUser.getId());

        //then

        assertThat(id.get()).isEqualTo(newProject.getId());
    }

    @Test
    public void findProjectsByQuery() {
        //given
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);
        User newUser = EntityUtils.createNewUser(plan);
        em.persist(newUser);
        Project project1 = EntityUtils.createDefaultProject(Project.builder()
                        .title("first")
                        .version(1)
                        .enabled(true)
                        .root(List.of())
                        .boards(Map.of())
                        .todos(Map.of())
                        .build(),
                newUser);
        em.persist(project1);
        Project project2 = EntityUtils.createDefaultProject(Project.builder()
                        .user(newUser)
                        .title("second")
                        .version(1)
                        .enabled(false)
                        .root(List.of())
                        .boards(Map.of())
                        .todos(Map.of())
                        .build(),
                newUser);
        em.persist(project2);
        em.flush();
        em.clear();

        //when & then
        GetProjectsByQueryDto recentQuery = new GetProjectsByQueryDto(1, "recent", null, null);
        List<QueryProjectDto> recentProjects = projectQueryDslRepository.findProjectsByQuery(recentQuery, newUser.getId());

        assertThat(recentProjects).hasSize(2);
        assertThat(recentProjects.get(0).getTitle()).isEqualTo("second");
        assertThat(recentProjects.get(1).getTitle()).isEqualTo("first");
        em.clear();

        GetProjectsByQueryDto oldQuery = new GetProjectsByQueryDto(1, "old", null, null);
        List<QueryProjectDto> oldProjects = projectQueryDslRepository.findProjectsByQuery(oldQuery, newUser.getId());

        assertThat(oldProjects).hasSize(2);
        assertThat(oldProjects.get(0).getTitle()).isEqualTo("first");
        assertThat(oldProjects.get(1).getTitle()).isEqualTo("second");
        em.clear();

        GetProjectsByQueryDto searchQuery = new GetProjectsByQueryDto(1, "recent", null, "first");
        List<QueryProjectDto> searchProjects = projectQueryDslRepository.findProjectsByQuery(searchQuery, newUser.getId());

        assertThat(searchProjects).hasSize(1);
        assertThat(searchProjects.get(0).getTitle()).isEqualTo("first");
        em.clear();

        GetProjectsByQueryDto enabledQuery = new GetProjectsByQueryDto(1, "recent", true, null);
        List<QueryProjectDto> enabledProjects = projectQueryDslRepository.findProjectsByQuery(enabledQuery, newUser.getId());

        assertThat(enabledProjects).hasSize(1);
        assertThat(enabledProjects.get(0).isEnabled()).isEqualTo(true);
        em.clear();
    }

    @Test
    public void findVersionAndEnabledByIdAndUserId() throws Exception {
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
        ProjectIdAndVersionAndEnabledDto dto = projectQueryDslRepository.findVersionAndEnabledByIdAndUserId(newProject.getId(), newUser.getId()).get();

        //then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(newProject.getId());
        assertThat(dto.version()).isEqualTo(newProject.getVersion());
        assertThat(dto.enabled()).isEqualTo(newProject.isEnabled());
    }

}