package com.mystreet.controller;

import com.mystreet.dto.OrderRequest;
import com.mystreet.dto.OrderResponse;
import com.mystreet.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        OrderResponse order = orderService.createOrder(request, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        String userEmail = authentication.getName();
        List<OrderResponse> orders = orderService.getMyOrders(userEmail);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable UUID id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        OrderResponse order = orderService.getOrderById(id, userEmail);
        return ResponseEntity.ok(order);
    }
}
