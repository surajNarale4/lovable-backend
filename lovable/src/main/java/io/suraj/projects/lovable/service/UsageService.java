package io.suraj.projects.lovable.service;

import io.suraj.projects.lovable.dto.subscription.PlanLimitsResponse;
import io.suraj.projects.lovable.dto.subscription.UsageTodayResponse;

public interface UsageService {
    UsageTodayResponse getTodayUsageOfUser(Long userId);

    PlanLimitsResponse getCurrentSubscriptionLimitsOfUser(Long userId);
}
