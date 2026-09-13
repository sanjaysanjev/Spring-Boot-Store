package com.sanjay.store.carts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel="spring")
public interface Cartmapper {

    @Mapping(target = "cartItems", source="cartItems")
    @Mapping(target="totalPrice",expression="java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target ="totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
