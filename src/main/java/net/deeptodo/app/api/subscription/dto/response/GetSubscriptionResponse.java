package net.deeptodo.app.api.subscription.dto.response;

import lombok.Builder;
import net.deeptodo.app.domain.Subscription;
import net.deeptodo.app.domain.SubscriptionPlan;

import java.time.LocalDateTime;

public record GetSubscriptionResponse(
        Long subscriptionId,
        RecordPlan plan,
        String status,
        LocalDateTime startDate,
        LocalDateTime expiredDate,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {

    @Builder
    public GetSubscriptionResponse {
    }

    public static GetSubscriptionResponse fromSubscription(Subscription subscription) {
        return new GetSubscriptionResponse(
                subscription.getId(),
                RecordPlan.fromPlan(subscription.getPlan()),
                subscription.getStatus().toString(),
                subscription.getStartDate(),
                subscription.getExpiredDate(),
                subscription.getUpdatedAt(),
                subscription.getCreatedAt()
        );
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
