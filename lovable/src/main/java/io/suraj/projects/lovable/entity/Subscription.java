package io.suraj.projects.lovable.entity;

import io.suraj.projects.lovable.entity.enums.SubscriptionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

public class Subscription {

    private Long id;
    private User user;
    private Plan plan;
    private String stripeSubscriptionId;

    @Enumerated(value= EnumType.STRING)
    private SubscriptionStatus status;

    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;
    private Boolean cancelAtPeriodEnd;
    private Instant createdAt;
    private Instant updatedAt;
}
