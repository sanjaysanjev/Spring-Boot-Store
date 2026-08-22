package com.sanjay.store.Payments;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class checkOutRequest {
    @NotNull(message = "Card Id is required")
    private UUID cartId;
}
