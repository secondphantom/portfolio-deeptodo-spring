package net.deeptodo.app.api.plan.dto.response;

import lombok.Builder;
import net.deeptodo.app.domain.SubscriptionPlan;

import java.util.List;

public record GetPlansResponse(
        List<RecordPlan> plans
) {
    @Builder
    public GetPlansResponse {
    }

    public static GetPlansResponse fromPlans(List<SubscriptionPlan> plans) {
        return new GetPlansResponse(plans.stream().map(plan -> RecordPlan.fromPlan(plan)).toList());
    }

    public record RecordPlan(
            Long planId,
            String type,
            String description,
            Double price,
            Integer durationDays,
            Integer maxProjectCount,
            Integer maxTodoCount,
            boolean enabled
    ) {
        @Builder
        public RecordPlan {
        }

        public static RecordPlan fromPlan(SubscriptionPlan plan) {
            return new RecordPlan(
                    plan.getId(),
                    plan.getType().toString(),
                    plan.getDescription(),
                    plan.getPrice(),
                    plan.getDurationDays(),
                    plan.getMaxProjectCount(),
                    plan.getMaxTodoCount(),
                    plan.isEnabled()
            );
        }
    }
}
