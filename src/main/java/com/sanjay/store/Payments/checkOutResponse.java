package com.sanjay.store.Payments;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class checkOutResponse {

    private Long id;
    private String checkoutUrl;
}
