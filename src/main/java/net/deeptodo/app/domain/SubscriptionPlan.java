package net.deeptodo.app.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "subscriptionplans")
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer durationDays;
    private Integer maxProjectCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
