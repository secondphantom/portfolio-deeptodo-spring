package net.deeptodo.app.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscriptions")
public class Subscription extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan plan;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    private Integer billingCycleDays;
    private LocalDateTime startDate;
    private LocalDateTime expiredDate;

    @Builder
    public Subscription(Long id, User user, SubscriptionPlan plan, SubscriptionStatus status, Integer billingCycleDays, LocalDateTime startDate, LocalDateTime expiredDate) {
        this.id = id;
        this.user = user;
        this.plan = plan;
        this.status = status;
        this.billingCycleDays = billingCycleDays;
        this.startDate = startDate;
        this.expiredDate = expiredDate;
    }
}
