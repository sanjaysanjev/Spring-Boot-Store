package com.sanjay.store.Payments;

import com.sanjay.store.orders.Order;


import java.util.Optional;

public interface paymentGateway {

    CheckoutSession createCheckoutSession(Order order);

    Optional<PaymentResult> parserWebhookRequest(WebhookRequest request);
}
