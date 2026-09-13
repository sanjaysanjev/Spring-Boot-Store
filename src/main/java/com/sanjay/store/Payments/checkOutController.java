package com.sanjay.store.Payments;

import com.sanjay.store.carts.CartEmptyException;
import com.sanjay.store.carts.CartNotFoundException;

import com.sanjay.store.common.ErrorDto;

import com.sanjay.store.orders.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class checkOutController  {
    private final checkOutService checkOutService;


    private final OrderRepository orderRepository;

    @PostMapping
    public checkOutResponse checkout(@Valid @RequestBody checkOutRequest request)
    {
            return checkOutService.checkOutResponse(request);


    }

    @PostMapping("/webhook")
    public void heandleWebHook(@RequestHeader Map<String,String> header, @RequestBody String payload)
    {
        checkOutService.handleWebhookEvent(new WebhookRequest(header,payload));
    }
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handleException()
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("Error creating a checkout Session"));
    }
    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex)
    {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

}
