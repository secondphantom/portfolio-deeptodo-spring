package net.deeptodo.app.api.plan.application;

import net.deeptodo.app.api.plan.dto.response.GetPlansResponse;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.repository.subscription.SubscriptionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;


    public GetPlansResponse getPlans() {

        List<SubscriptionPlan> plans = subscriptionPlanRepository.getAll();

        return GetPlansResponse.fromPlans(plans);
    }
}
