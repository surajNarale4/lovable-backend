package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.subscription.PlanLimitsResponse;
import io.suraj.projects.lovable.dto.subscription.UsageTodayResponse;
import io.suraj.projects.lovable.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getTodayUsageOfUser(Long userId) {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionLimitsOfUser(Long userId) {
        return null;
    }
}
