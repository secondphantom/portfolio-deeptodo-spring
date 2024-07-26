package net.deeptodo.app.repository.subscription;

import net.deeptodo.app.domain.PlanType;
import net.deeptodo.app.domain.SubscriptionPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionPlanRepository {

    @Autowired
    private SubscriptionPlanJpaRepository subscriptionPlanJpaRepository;

    public Long create(SubscriptionPlan plan) {
        subscriptionPlanJpaRepository.save(plan);
        return plan.getId();
    }

    public SubscriptionPlan getByType(PlanType type) {
        return subscriptionPlanJpaRepository.findByType(type).get();
    }




}
