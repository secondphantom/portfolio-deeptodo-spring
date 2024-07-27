package net.deeptodo.app.repository.subscription;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.Subscription;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.testutils.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SubscriptionJpaRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private SubscriptionJpaRepository subscriptionJpaRepository;

    @Test
    public void findByUser_id() throws Exception {
        //given
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);
        em.flush();

        User newUser = EntityUtils.createNewUser(plan);
        em.persist(newUser);
        em.flush();
        em.clear();

        //when
        Optional<Subscription> response = subscriptionJpaRepository.findByUser_id(newUser.getId());

        //then
        assertThat(response.get().getPlan().getId()).isEqualTo(plan.getId());
    }
}