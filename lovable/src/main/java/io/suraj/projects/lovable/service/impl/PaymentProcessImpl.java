package io.suraj.projects.lovable.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
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
import org.springframework.stereotype.Service;

import java.security.Provider;

@RequiredArgsConstructor
@Service
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
}
