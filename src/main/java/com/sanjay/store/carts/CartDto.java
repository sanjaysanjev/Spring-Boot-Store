package com.sanjay.store.carts;

import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

@Data
public class CartDto {

    private UUID id;

    private List<CartItemDto> cartItems=new ArrayList<>();
    private BigDecimal totalPrice=BigDecimal.ZERO;

}
