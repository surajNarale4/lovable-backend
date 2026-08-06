package io.suraj.projects.lovable.service;

import io.suraj.projects.lovable.dto.subscription.PortalResponse;
import io.suraj.projects.lovable.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {

    SubscriptionResponse getMySub(Long userId);

    PortalResponse openCustomerPortal(Long userId);
}
