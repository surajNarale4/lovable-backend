package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.subscription.PortalResponse;
import io.suraj.projects.lovable.dto.subscription.SubscriptionResponse;
import io.suraj.projects.lovable.service.SubscriptionService;
import org.springframework.stereotype.Service;


@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getMySub(Long userId) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
