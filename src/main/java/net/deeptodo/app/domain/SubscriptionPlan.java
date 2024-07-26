package net.deeptodo.app.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscriptionplans")
public class SubscriptionPlan extends BaseTimeEntity {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlanType type;
    private String description;
    private Double price;
    private Integer durationDays;
    private Integer maxProjectCount;
    private Integer maxTodoCount;
    private boolean enabled;

    @Builder
    public SubscriptionPlan(Long id, PlanType type, String description, Double price, Integer durationDays, Integer maxProjectCount, Integer maxTodoCount, boolean enabled) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.price = price;
        this.durationDays = durationDays;
        this.maxProjectCount = maxProjectCount;
        this.maxTodoCount = maxTodoCount;
        this.enabled = enabled;
    }
}
