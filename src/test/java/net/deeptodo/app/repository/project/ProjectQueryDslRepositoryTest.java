package net.deeptodo.app.repository.project;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.OauthServerType;
import net.deeptodo.app.domain.Project;
import net.deeptodo.app.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    public void findVersionById() throws Exception {
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
    public void findIdByIdAndUserId() throws Exception {
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


}