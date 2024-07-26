package net.deeptodo.app.repository.subscription;

import net.deeptodo.app.domain.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SubscriptionRepository {

    @Autowired
    private SubscriptionJpaRepository subscriptionJpaRepository;

    public Optional<Subscription> getSubscriptionByUserId(Long userId) {
        return subscriptionJpaRepository.findByUser_id(userId);
    }

}
