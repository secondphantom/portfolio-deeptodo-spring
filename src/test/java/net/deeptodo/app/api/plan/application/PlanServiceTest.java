package net.deeptodo.app.api.plan.application;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.api.plan.dto.response.GetPlansResponse;
import net.deeptodo.app.domain.PlanType;
import net.deeptodo.app.domain.SubscriptionPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PlanServiceTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private PlanService planService;

    @Test
    public void getPlans() throws Exception {
        //given
        SubscriptionPlan proPlan = SubscriptionPlan.builder().price(1.0).durationDays(1000).id(1L).type(PlanType.PRO).build();
        em.persist(proPlan);
        em.flush();
        SubscriptionPlan freePlan = SubscriptionPlan.builder().price(0.0).durationDays(1000).id(2L).type(PlanType.FREE).build();
        em.persist(freePlan);
        em.flush();

        //when
        GetPlansResponse response = planService.getPlans();

        //then
        assertThat(response.plans()).hasSize(2);
        assertThat(response.plans().get(0).type()).isEqualTo(PlanType.FREE.toString());
        assertThat(response.plans().get(1).type()).isEqualTo(PlanType.PRO.toString());
    }
}