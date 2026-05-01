package com.mystreet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private UUID id;
    private UUID userId;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private String paymentMode;
    private LocalDateTime createdAt;
}
