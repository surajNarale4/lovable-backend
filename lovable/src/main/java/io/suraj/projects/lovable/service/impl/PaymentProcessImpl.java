package io.suraj.projects.lovable.service.impl;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import io.suraj.projects.lovable.dto.subscription.CheckoutRequest;
import io.suraj.projects.lovable.dto.subscription.CheckoutResponse;
import io.suraj.projects.lovable.dto.subscription.PortalUrl;
import io.suraj.projects.lovable.entity.Plan;
import io.suraj.projects.lovable.entity.User;
import io.suraj.projects.lovable.entity.enums.SubscriptionStatus;
import io.suraj.projects.lovable.error.BadRequestException;
import io.suraj.projects.lovable.error.ResourseNotFoundException;
import io.suraj.projects.lovable.repository.PlanRepository;
import io.suraj.projects.lovable.repository.UserRepository;
import io.suraj.projects.lovable.security.SecurityExpressions;
import io.suraj.projects.lovable.service.PaymentProcess;
import io.suraj.projects.lovable.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.time.Instant;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentProcessImpl implements PaymentProcess {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;


    @Override
    public CheckoutResponse getCheckoutUrl(CheckoutRequest checkoutRequest) {
        Plan plan = planRepository.findById(checkoutRequest.planId()).orElseThrow(()->new ResourseNotFoundException("Plan not found with given id "+ checkoutRequest.planId()));
        User user = userRepository.findById(SecurityExpressions.getUserId()).orElseThrow(()->new ResourseNotFoundException("user not found with id "));
        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl("http://localhost:8080" + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8080" + "/cancel.html")
                .putMetadata("user_id", user.getId())
                .putMetadata("plan_id", plan.getId().toString())
                .build();

        try {
            Session session = Session.create(params);
            return new CheckoutResponse(session.getUrl());
        }catch(StripeException r){
            throw new BadRequestException(r.getMessage());
        }
//        Session session = client.v1().checkout().sessions().create(params);
//
//        response.redirect(session.getUrl(), 303);
//        return "";
    }

    @Override
    public PortalUrl openCustomerPortal() {
        return null;
    }

    @Override
    public String webhook(String payload, String header, String secret) {
        try {
            Event event = Webhook.constructEvent(payload,header,secret);
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject = null;
            if (dataObjectDeserializer.getObject().isPresent()) {
                stripeObject = dataObjectDeserializer.getObject().get();
            } else {
                //fallback if version missmatch
                log.error(
                        "Unable to deserialize Stripe webhook event. eventId={}, eventType={}, rawJson={}",
                        event.getId(),
                        event.getType(),
                        dataObjectDeserializer.getRawJson()
                );
            }
            // Handle the event
            log.info("event occured : {}",event.getType());
            switch (event.getType()) {

                case "checkout.session.completed"-> handleCheckoutCompleted((Session)stripeObject);
                case "customer.subscription.updated" -> handleCustomerSubscriptionUpdated((Subscription) stripeObject);
                default-> log.info("Unhandled event type: " + event.getType());

            }

            return "";
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

    }
    /*
    * status (Possible values are)
    *   incomplete,
    *   incomplete_expired,
    *   trialing,
    *   active,
    *   past_due,
    *   canceled,
    *   unpaid,
    *   or paused.
    * */
    private void handleCustomerSubscriptionUpdated(Subscription subscription) {

        SubscriptionStatus status = SubscriptionStatus.valueOf(subscription.getStatus());

        SubscriptionItem item = subscription.getItems().getData().get(0);
        Instant periodStart = toInstant(item.getCurrentPeriodStart());
        Instant periodEnd = toInstant(item.getCurrentPeriodEnd());
        Long planId = resolvePlanId(item.getPrice());
        subscriptionService.updateSubscription(
                subscription.getId(), status, periodStart, periodEnd,
                subscription.getCancelAtPeriodEnd(), planId
        );


    }

    private Long resolvePlanId(Price price) {
        if (price == null || price.getId() == null) return null;
        return planRepository.findByStripePriceId(price.getId())
                .map(Plan::getId)
                .orElse(null);
    }

    private void handleCheckoutCompleted(Session session) {

        if(session == null) {
            log.error("session object was null");
            return;
        }
        Map<String, String> metadata=session.getMetadata();

        String userId =metadata.get("user_id");
        Long planId = Long.parseLong(metadata.get("plan_id"));

        String subscriptionId = session.getSubscription();
        String customerId = session.getCustomer();


        User user = userRepository.findById(userId).orElseThrow(()->new ResourseNotFoundException("no user found with given id"));
        if(user.getStripeCustomerId()==null){
            user.setStripeCustomerId(subscriptionId);
            userRepository.save(user);
        }

        subscriptionService.activateSubscription(userId,planId,subscriptionId);



    }
    public Instant toInstant(Long epoch){
        return epoch!=null? Instant.ofEpochSecond(epoch) :null;
    }
}
