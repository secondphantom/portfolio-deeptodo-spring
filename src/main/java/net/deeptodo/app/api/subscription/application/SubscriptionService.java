package net.deeptodo.app.api.subscription.application;

import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.subscription.dto.response.GetSubscriptionResponse;
import net.deeptodo.app.api.subscription.exception.SubscriptionErrorCode;
import net.deeptodo.app.common.exception.UnauthorizedException;
import net.deeptodo.app.domain.Subscription;
import net.deeptodo.app.repository.subscription.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public GetSubscriptionResponse getSubscription(AuthUserInfo authUserInfo) {
        Subscription subscription = subscriptionRepository.getSubscriptionByUserId(authUserInfo.userId())
                .orElseThrow(() -> new UnauthorizedException(SubscriptionErrorCode.getErrorCode(SubscriptionErrorCode.UNAUTHORIZED_NOT_FOUND_SUBSCRIPTION)));

        return GetSubscriptionResponse.fromSubscription(subscription);
    }


}
