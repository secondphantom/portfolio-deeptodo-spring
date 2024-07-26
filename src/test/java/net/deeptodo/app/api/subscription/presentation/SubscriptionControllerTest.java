package net.deeptodo.app.api.subscription.presentation;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.aop.auth.AuthArgumentResolver;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.RestDocsIntegration;
import net.deeptodo.app.api.subscription.application.SubscriptionService;
import net.deeptodo.app.api.subscription.dto.response.GetSubscriptionResponse;
import net.deeptodo.app.common.config.JacksonConfig;
import net.deeptodo.app.domain.OauthServerType;
import net.deeptodo.app.domain.PlanType;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Transactional
class SubscriptionControllerTest extends RestDocsIntegration {

    @Autowired
    private EntityManager em;

    @MockBean
    private SubscriptionService subscriptionService;

    @MockBean
    private AuthArgumentResolver authArgumentResolver;

    private final String URL_PATH = "/api/v1/account/subscription";

    @BeforeEach
    public void beforeEach() throws Exception {
        AuthUserInfo authUserInfo = new AuthUserInfo(1L);
        given(authArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(authUserInfo);
    }

    private SubscriptionPlan createPlan(Long planId) {
        SubscriptionPlan plan = SubscriptionPlan.builder()
                .id(planId)
                .type(PlanType.FREE)
                .description("")
                .price(2.0)
                .durationDays(30)
                .maxProjectCount(5)
                .maxTodoCount(200)
                .enabled(true)
                .build();
        em.persist(plan);
        em.flush();

        return plan;
    }

    private User createUser(SubscriptionPlan plan) {
        User user = User.createNewUser(null, null, null, OauthServerType.GOOGLE, plan);

        em.persist(user);
        em.flush();
        return user;
    }

    @Test
    public void getSubscription_success() throws Exception {
        //given
        SubscriptionPlan plan = createPlan(1L);
        User user = createUser(plan);

        GetSubscriptionResponse response = GetSubscriptionResponse.fromSubscription(user.getSubscription());

        given(subscriptionService.getSubscription(any())).willReturn(response);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subscriptionId").value(response.subscriptionId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plan.planId").value(response.plan().planId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plan.type").value(response.plan().type()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plan.description").value(response.plan().description()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plan.price").value(response.plan().price()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plan.durationDays").value(response.plan().durationDays()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plan.maxProjectCount").value(response.plan().maxProjectCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plan.maxTodoCount").value(response.plan().maxTodoCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.plan.enabled").value(response.plan().enabled()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(response.status()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(response.startDate().format(JacksonConfig.dateTimeFormatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiredDate").value(response.expiredDate().format(JacksonConfig.dateTimeFormatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(response.updatedAt().format(JacksonConfig.dateTimeFormatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(response.createdAt().format(JacksonConfig.dateTimeFormatter)))
                .andDo(restDocs.document());
    }

}