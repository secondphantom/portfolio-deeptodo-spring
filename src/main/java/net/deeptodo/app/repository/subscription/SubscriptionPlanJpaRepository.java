package net.deeptodo.app.repository.subscription;

import net.deeptodo.app.domain.PlanType;
import net.deeptodo.app.domain.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanJpaRepository extends JpaRepository<SubscriptionPlan, Long> {

    Optional<SubscriptionPlan> findByType(PlanType type);
    List<SubscriptionPlan> findAllByOrderByPriceAsc();
}
