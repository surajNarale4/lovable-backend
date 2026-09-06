package io.suraj.projects.lovable.service;

import com.stripe.model.Invoice;
import io.suraj.projects.lovable.dto.subscription.PortalResponse;
import io.suraj.projects.lovable.dto.subscription.SubscriptionResponse;
import io.suraj.projects.lovable.entity.enums.SubscriptionStatus;

import java.time.Instant;

public interface SubscriptionService {

    SubscriptionResponse getMySub(Long userId);

    PortalResponse openCustomerPortal(Long userId);

    void activateSubscription(String userId, Long planId, String subscriptionId);

    void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String id);

    void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String subId);
}
