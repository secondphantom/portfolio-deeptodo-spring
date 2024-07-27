package net.deeptodo.app.api.subscription.application;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.subscription.dto.response.GetSubscriptionResponse;
import net.deeptodo.app.common.exception.UnauthorizedException;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.testutils.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class SubscriptionServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private SubscriptionService subscriptionService;

    @Test
    public void getSubscription_success() throws Exception {
        //given
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);
        User newUser = EntityUtils.createNewUser(plan);
        em.persist(newUser);
        em.flush();
        em.clear();

        //when
        GetSubscriptionResponse response = subscriptionService.getSubscription(new AuthUserInfo(newUser.getId()));

        //then
        assertThat(response.subscriptionId()).isEqualTo(newUser.getSubscription().getId());
    }

    @Test
    public void getSubscription_fail_not_found() throws Exception {
        //when & then
        assertThatThrownBy(
                () -> subscriptionService.getSubscription(new AuthUserInfo(2L))
        ).isInstanceOf(UnauthorizedException.class);
    }


}