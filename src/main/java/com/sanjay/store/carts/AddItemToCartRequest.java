package com.sanjay.store.carts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddItemToCartRequest {

    private Long product_Id;
}
