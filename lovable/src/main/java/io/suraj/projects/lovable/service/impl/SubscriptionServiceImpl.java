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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    @Transactional
    public void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {
        Subscription subscription = subscriptionRepository.findByStripeSubscriptionId(id).orElseThrow(()->new ResourseNotFoundException("no subscription found with given id "+id));
        boolean hasSubscriptionUpdated = false;

        if(status != null && status != subscription.getStatus()) {
            subscription.setStatus(status);
            hasSubscriptionUpdated = true;
        }
        if(periodStart != null && !periodStart.equals(subscription.getCurrentPeriodStart())) {
            subscription.setCurrentPeriodStart(periodStart);
            hasSubscriptionUpdated = true;
        }
        if(periodEnd != null && !periodEnd.equals(subscription.getCurrentPeriodEnd())) {
            subscription.setCurrentPeriodEnd(periodEnd);
            hasSubscriptionUpdated = true;
        }
        if(cancelAtPeriodEnd != null && cancelAtPeriodEnd != subscription.getCancelAtPeriodEnd()) {
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            hasSubscriptionUpdated = true;
        }
        if(planId != null && !planId.equals(subscription.getPlan().getId())) {
            Plan newPlan = getPlan(planId);
            subscription.setPlan(newPlan);
            hasSubscriptionUpdated = true;
        }

        if(hasSubscriptionUpdated) {
            log.debug("Subscription has been updated: {}", id);
            subscriptionRepository.save(subscription);
        }


    }

    @Override
    public void cancelSubscription(String id) {
        Subscription subscription = getSubscription(id);
        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd) {
        Subscription subscription = getSubscription(subId);
        subscription.setCurrentPeriodStart(periodStart);
        subscription.setCurrentPeriodEnd(periodEnd);
        if(subscription.getStatus() != SubscriptionStatus.ACTIVE){
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }
    }

    @Override
    public void markSubscriptionPastDue(String subId) {
        Subscription subscription = getSubscription(subId);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE) {
            log.debug("Subscription is already past due, gatewaySubscriptionId: {}", subId);
            return;
        }
        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);
    }

    private Subscription getSubscription(String gatewaySubscriptionId) {
        return subscriptionRepository.findByStripeSubscriptionId(gatewaySubscriptionId).orElseThrow(() ->
                new ResourseNotFoundException("Subscription"+ gatewaySubscriptionId));

    }

    private Plan getPlan(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new ResourseNotFoundException("Plan"+ planId.toString()));

    }
}
