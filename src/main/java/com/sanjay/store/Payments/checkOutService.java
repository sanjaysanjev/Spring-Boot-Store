package com.sanjay.store.Payments;

import com.sanjay.store.carts.CartEmptyException;
import com.sanjay.store.carts.CartNotFoundException;
import com.sanjay.store.orders.OrderNotFoundException;
import com.sanjay.store.auth.AuthService;
import com.sanjay.store.carts.CartService;
import com.sanjay.store.orders.Order;
import com.sanjay.store.carts.CartRepository;
import com.sanjay.store.orders.OrderRepository;


import lombok.RequiredArgsConstructor;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class checkOutService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final paymentGateway paymentGateway;


    @Transactional
    public checkOutResponse checkOutResponse(checkOutRequest request)
    {
        var cart=cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if(cart==null)
        {
            throw new CartNotFoundException();
        }
        if(cart.isEmpty())
        {
            throw new CartEmptyException();
        }

        var order= Order.fromCart(cart,authService.getCurrentUser());
        orderRepository.save(order);
        try{

            var session=paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            return new checkOutResponse(order.getId(),session.getCheckoutUrl());
        }
        catch(PaymentException ex)
        {
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request)
    {
        paymentGateway.parserWebhookRequest(request).ifPresent((paymentResult)->
        {
            var order=orderRepository.findById(paymentResult.getOrderId()).orElseThrow(()->new OrderNotFoundException());
            order.setStatus(paymentResult.getPaymentStatus());
            orderRepository.save(order);
        });
    }
}
