package com.sanjay.store.Payments;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentException extends RuntimeException {

    public PaymentException(String msg)
    {
        super(msg);
    }
}
