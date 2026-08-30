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
import io.suraj.projects.lovable.error.BadRequestException;
import io.suraj.projects.lovable.error.ResourseNotFoundException;
import io.suraj.projects.lovable.repository.PlanRepository;
import io.suraj.projects.lovable.service.PaymentProcess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Provider;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentProcessImpl implements PaymentProcess {

    private final PlanRepository planRepository;


    @Override
    public CheckoutResponse getCheckoutUrl(CheckoutRequest checkoutRequest) {
        Plan plan = planRepository.findById(checkoutRequest.planId()).orElseThrow(()->new ResourseNotFoundException("Plan not found with given id "+ checkoutRequest.planId()));

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl("http://localhost:8080" + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8080" + "/cancel.html")
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
            }
            // Handle the event
            switch (event.getType()) {
                case "invoice.payment_succeeded": // invoice.payment_succeeded
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                    System.out.println("Payment for " + paymentIntent.getAmount() + " succeeded.");
                    // Then define and call a method to handle the successful payment intent.
                    // handlePaymentIntentSucceeded(paymentIntent);
                    break;
                case "invoice_payment.attached":
                    PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                    // Then define and call a method to handle the successful attachment of a PaymentMethod.
                    // handlePaymentMethodAttached(paymentMethod);
                    break;
                default:
                    System.out.println("Unhandled event type: " + event.getType());
                    break;
            }

            return "";
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

    }
}
