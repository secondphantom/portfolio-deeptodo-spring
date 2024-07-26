package net.deeptodo.app.repository.subscription;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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
        SubscriptionPlan plan = SubscriptionPlan.builder().durationDays(1000).id(1L).type(PlanType.FREE).build();
        em.persist(plan);
        em.flush();

        User newUser = User.createNewUser(null, null, null, OauthServerType.GOOGLE, plan);
        em.persist(newUser);
        em.flush();
        em.clear();

        //when
        Optional<Subscription> response = subscriptionJpaRepository.findByUser_id(newUser.getId());

        //then
        assertThat(response.get().getPlan().getId()).isEqualTo(plan.getId());
    }
}