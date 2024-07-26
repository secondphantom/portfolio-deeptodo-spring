package net.deeptodo.app.api.subscription.presentation;

import net.deeptodo.app.aop.auth.AuthUser;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.subscription.application.SubscriptionService;
import net.deeptodo.app.api.subscription.dto.response.GetSubscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<GetSubscriptionResponse> getSubscription(
            @AuthUser AuthUserInfo authUserInfo
    ) {
        GetSubscriptionResponse response = subscriptionService.getSubscription(authUserInfo);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/upgrade")
    public ResponseEntity upgradeSubscription(
            @AuthUser AuthUserInfo authUserInfo
    ) {
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/cancel")
    public ResponseEntity cancelSubscription(
            @AuthUser AuthUserInfo authUserInfo
    ) {
        return ResponseEntity.ok().build();
    }


}
