package net.deeptodo.app.repository.user;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.testutils.EntityUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void getByEmail() throws Exception {
        //given
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);
        User user = EntityUtils.createNewUser(plan);
        em.persist(user);
        em.flush();
        em.clear();

        //when
        Optional<User> userByEmail = userRepository.getByEmail(user.getEmail());

        //then
        Assertions.assertThat(user.getEmail()).isEqualTo(userByEmail.get().getEmail());

    }

}