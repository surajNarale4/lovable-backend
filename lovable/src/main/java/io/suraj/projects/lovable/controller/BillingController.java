package io.suraj.projects.lovable.controller;


import io.suraj.projects.lovable.dto.subscription.PlanResponse;
import io.suraj.projects.lovable.dto.subscription.PortalResponse;
import io.suraj.projects.lovable.dto.subscription.SubscriptionResponse;
import io.suraj.projects.lovable.service.PlanService;
import io.suraj.projects.lovable.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class BillingController {

    private PlanService plansService;
    private SubscriptionService subscriptionService;
    @GetMapping("/api/plans")
    public ResponseEntity<PlanResponse> getAllPlans(){
        return ResponseEntity.ok(plansService.getAllPlans());
    }

    @PostMapping("/api/me/subsription")
    public ResponseEntity<SubscriptionResponse> getMySub(){
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.getMySub(userId));
    }

    @PostMapping("/api/stripe/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal() {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.openCustomerPortal(userId));
    }

}
