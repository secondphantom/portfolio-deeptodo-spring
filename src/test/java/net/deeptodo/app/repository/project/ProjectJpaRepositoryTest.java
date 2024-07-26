package net.deeptodo.app.repository.project;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.Project;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectJpaRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ProjectJpaRepository projectJpaRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void countByUser_id() throws Exception {
        //given
        User oldUser = User.builder().build();
        em.persist(oldUser);
        User newUser = User.builder().build();
        em.persist(newUser);
        Project newProject = Project.createNewProject(newUser);
        Project newProject2 = Project.createNewProject(newUser);
        em.persist(newProject);
        em.persist(newProject2);
        em.flush();
        em.clear();
        //when
        long count = projectJpaRepository.countByUser_id(newUser.getId());

        //then
        assertThat(count).isEqualTo(2);
    }

}