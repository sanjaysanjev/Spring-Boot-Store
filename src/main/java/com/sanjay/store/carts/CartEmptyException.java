package com.sanjay.store.carts;

public class CartEmptyException extends RuntimeException{

    public CartEmptyException()
    {
        super("Cart is Empty");
    }
}
