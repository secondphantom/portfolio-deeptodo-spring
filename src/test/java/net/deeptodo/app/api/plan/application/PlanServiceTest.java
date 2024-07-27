package net.deeptodo.app.api.plan.application;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.api.plan.dto.response.GetPlansResponse;
import net.deeptodo.app.domain.PlanType;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.testutils.EntityUtils;
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
        SubscriptionPlan proPlan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().price(2.0).type(PlanType.PRO).build(), 1L);

        em.persist(proPlan);
        em.flush();
        SubscriptionPlan freePlan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().price(0.0).type(PlanType.FREE).build(), 2L);

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