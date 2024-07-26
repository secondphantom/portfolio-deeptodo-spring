package net.deeptodo.app.api.plan.presentation;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.api.RestDocsIntegration;
import net.deeptodo.app.api.plan.application.PlanService;
import net.deeptodo.app.api.plan.dto.response.GetPlansResponse;
import net.deeptodo.app.domain.PlanType;
import net.deeptodo.app.domain.SubscriptionPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.BDDMockito.given;

@Transactional
class PlanControllerTest extends RestDocsIntegration {

    private final String URL_PATH = "/api/v1/plans";

    @Autowired
    private EntityManager em;

    @MockBean
    private PlanService planService;

    private SubscriptionPlan createPlan(Long planId, Double price, PlanType type) {
        SubscriptionPlan plan = SubscriptionPlan.builder()
                .id(planId)
                .type(type)
                .description("")
                .price(price)
                .durationDays(30)
                .maxProjectCount(5)
                .maxTodoCount(200)
                .enabled(true)
                .build();
        em.persist(plan);
        em.flush();

        return plan;
    }

    @Test
    public void getPlans() throws Exception {
        //given
        SubscriptionPlan freePlan = createPlan(1L, 0.0, PlanType.FREE);
        SubscriptionPlan proPlan = createPlan(2L, 3.0, PlanType.PRO);

        GetPlansResponse response = GetPlansResponse.fromPlans(List.of(freePlan, proPlan));

        given(planService.getPlans()).willReturn(response);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.plans[0].planId").value(response.plans().get(0).planId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plans[0].type").value(response.plans().get(0).type()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plans[0].description").value(response.plans().get(0).description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plans[0].price").value(response.plans().get(0).price()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plans[0].durationDays").value(response.plans().get(0).durationDays()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plans[0].maxProjectCount").value(response.plans().get(0).maxProjectCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plans[0].maxTodoCount").value(response.plans().get(0).maxTodoCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plans[0].enabled").value(response.plans().get(0).enabled()))
                .andDo(restDocs.document());
    }


}