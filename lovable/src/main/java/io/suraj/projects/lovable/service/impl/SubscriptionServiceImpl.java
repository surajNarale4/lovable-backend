package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.dto.subscription.PortalResponse;
import io.suraj.projects.lovable.dto.subscription.SubscriptionResponse;
import io.suraj.projects.lovable.entity.Plan;
import io.suraj.projects.lovable.entity.Subscription;
import io.suraj.projects.lovable.entity.User;
import io.suraj.projects.lovable.entity.enums.SubscriptionStatus;
import io.suraj.projects.lovable.error.ResourseNotFoundException;
import io.suraj.projects.lovable.repository.PlanRepository;
import io.suraj.projects.lovable.repository.SubscriptionRepository;
import io.suraj.projects.lovable.repository.UserRepository;
import io.suraj.projects.lovable.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    @Override
    public SubscriptionResponse getMySub(Long userId) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }

    @Override
    public void activateSubscription(String userId, Long planId, String subscriptionId) {
        boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if (exists) return;
        User user = userRepository.findById(userId).orElseThrow(()->new ResourseNotFoundException("user not found with given id "));
        Plan plan = planRepository.findById(planId).orElseThrow(()->new ResourseNotFoundException("Plan not found with givne id "));
        Subscription subscription = Subscription.builder()
                .plan(plan)
                .user(user)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();

        subscriptionRepository.save(subscription);

    }
}
