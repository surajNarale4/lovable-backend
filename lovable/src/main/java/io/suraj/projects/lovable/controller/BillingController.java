package io.suraj.projects.lovable.controller;


import com.stripe.Stripe;
import io.suraj.projects.lovable.dto.subscription.*;
import io.suraj.projects.lovable.service.PaymentProcess;
import io.suraj.projects.lovable.service.PlanService;
import io.suraj.projects.lovable.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping
@RequiredArgsConstructor
public class BillingController {

    private final PlanService plansService;
    private final PaymentProcess paymentProcess;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

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

    @PostMapping("/api/payment/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal() {
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.openCustomerPortal(userId));
    }

    @PostMapping("/api/payment/checkout")
    public ResponseEntity<CheckoutResponse> checkoutResponse(@RequestBody CheckoutRequest checkoutRequest){
        return ResponseEntity.ok(paymentProcess.getCheckoutUrl(checkoutRequest));
    }

    @PostMapping("/webhook/payment")
    public ResponseEntity<String> webHook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signature){
        return ResponseEntity.ok(paymentProcess.webhook(payload, signature,webhookSecret));
    }
}
