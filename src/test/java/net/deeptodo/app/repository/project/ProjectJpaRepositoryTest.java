package net.deeptodo.app.repository.project;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.Project;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.repository.user.UserRepository;
import net.deeptodo.app.testutils.EntityUtils;
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
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);
        User oldUser = EntityUtils.createNewUser(plan);
        em.persist(oldUser);
        User newUser = EntityUtils.createNewUser(plan);
        em.persist(newUser);
        Project newProject = EntityUtils.createDefaultProject(Project.builder().build(), newUser);
        Project newProject2 = EntityUtils.createDefaultProject(Project.builder().build(), newUser);
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