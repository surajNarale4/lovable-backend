package io.suraj.projects.lovable.service;

import io.suraj.projects.lovable.dto.subscription.CheckoutRequest;
import io.suraj.projects.lovable.dto.subscription.CheckoutResponse;
import io.suraj.projects.lovable.dto.subscription.PortalUrl;

public interface PaymentProcess {

    CheckoutResponse getCheckoutUrl(CheckoutRequest checkoutRequest);
    PortalUrl openCustomerPortal();
}
