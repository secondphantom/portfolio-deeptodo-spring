package net.deeptodo.app.repository.subscription;

import net.deeptodo.app.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionJpaRepository extends JpaRepository<Subscription, Long> {

     Optional<Subscription> findByUser_id(Long userId);
}
