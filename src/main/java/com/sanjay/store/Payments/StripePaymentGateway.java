package com.sanjay.store.Payments;

import com.sanjay.store.orders.Order;
import com.sanjay.store.orders.OrderItem;
import com.sanjay.store.orders.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements paymentGateway {

    @Value(("${websiteURL}"))
    private String websiteURL;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretkey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {

        try {
            var s="sanjay";
            var builder = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteURL + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteURL + "/checkout-cancel")
                    .putMetadata("order_id",order.getId().toString());

            order.getOrderItems().forEach(item -> {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        }
        catch (StripeException ex)
        {
            throw new PaymentException();
        }

    }

    @Override
    public Optional<PaymentResult> parserWebhookRequest(WebhookRequest request) {
        try {
            var payload= request.getPayload();
            var signature=request.getHeader().get("Stripe-Signature");

            var event = Webhook.constructEvent(payload, signature, webhookSecretkey);

             switch(event.getType())
            {
                case "payment_intent.succeeded" :{
                         return Optional.of(new PaymentResult(extractOrderId(event),OrderStatus.PAID));
               }
                case "payment_intent.payment_failed" :{
                     return Optional.of(new PaymentResult(extractOrderId(event),OrderStatus.FAILED));

                }
                default:
                     return Optional.empty();
            }
        }
        catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid Signature");
        }
    }

    private Long extractOrderId(Event event)
    {
        var stripe_object=event.getDataObjectDeserializer().getObject().orElseThrow(()->
                new PaymentException("Could not deserialize Stripe Event. Check the SDK and API Version"));
        var paymentIntent=(PaymentIntent)stripe_object;

            return Long.valueOf(paymentIntent.getMetadata().get("order_id"));

        }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder().
                setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item)
                ).build();

    }

    private  SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd").setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100))).setProductData(
                        createProductData(item)
                )
                .build();
    }

    private  SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder().setName(item.getProduct().getName())
                .build();
    }
}
