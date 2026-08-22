package com.sanjay.store.carts;

import com.sanjay.store.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@Tag(name="Carts")
public class CartController {
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder)
    {
        var cartDto=cartService.createCart();
        var uri=uriBuilder.path("/users/{id}").buildAndExpand(cartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add products to the cart")
    public ResponseEntity<CartItemDto> addToCart(@Parameter(description = "The Id of the Cart") @PathVariable UUID cartId, @RequestBody AddItemToCartRequest request)
    {
        var cartItemDto=cartService.addToChart(cartId, request.getProduct_Id());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("{cartId}")
    public CartDto getCart(@PathVariable UUID cartId)
    {
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateItem(@PathVariable UUID cartId, @PathVariable Long productId,
                                                 @Valid @RequestBody UpdateCartItemRequest request)
    {
        return cartService.updateItem(cartId,productId, request.getQuantity());

    }
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?>removeItem(@PathVariable UUID cartId, @PathVariable Long productId)
    {
        cartService.removeItem(cartId,productId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void>clearCart(@PathVariable UUID cartId)
    {
        cartService.clearCart(cartId);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<String>handleCart()
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error:Cart Not Found");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String>handleProduct()
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error:Product Not Found");
    }
}
