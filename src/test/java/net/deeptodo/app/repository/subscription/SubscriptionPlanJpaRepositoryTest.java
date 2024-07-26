package net.deeptodo.app.repository.subscription;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.PlanType;
import net.deeptodo.app.domain.SubscriptionPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SubscriptionPlanJpaRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private SubscriptionPlanJpaRepository subscriptionPlanJpaRepository;

    @Test
    public void findByType() throws Exception {
        //given
        SubscriptionPlan newPlan = SubscriptionPlan.builder().id(1L).type(PlanType.FREE).build();

        em.persist(newPlan);
        em.flush();
        em.clear();
        //when
        SubscriptionPlan plan = subscriptionPlanJpaRepository.findByType(PlanType.FREE).get();

        //then
        assertThat(plan.getType()).isEqualTo(PlanType.FREE);
    }
}