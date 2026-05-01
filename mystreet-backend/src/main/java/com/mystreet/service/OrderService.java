package com.mystreet.service;

import com.mystreet.dto.*;
import com.mystreet.exception.BadRequestException;
import com.mystreet.exception.ResourceNotFoundException;
import com.mystreet.model.*;
import com.mystreet.repository.OrderRepository;
import com.mystreet.repository.ProductRepository;
import com.mystreet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());

        // Parse and set payment mode
        try {
            order.setPaymentMode(Order.PaymentMode.valueOf(request.getPaymentMode()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid payment mode: " + request.getPaymentMode());
        }

        order.setStatus(Order.OrderStatus.PLACED);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemReq.getProductId()));

            // Check stock
            if (product.getStockQty() < itemReq.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setSize(itemReq.getSize());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPriceAtOrder(product.getPrice());

            order.getItems().add(orderItem);

            // Calculate item total
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(itemReq.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            // Decrement stock
            product.setStockQty(product.getStockQty() - itemReq.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        return convertToResponse(order);
    }

    public List<OrderResponse> getMyOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);

        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(UUID orderId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Ensure user can only view their own orders (unless admin)
        if (!order.getUser().getId().equals(user.getId()) && !user.getIsAdmin()) {
            throw new BadRequestException("Unauthorized to view this order");
        }

        return convertToResponse(order);
    }

    private OrderResponse convertToResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                    item.getId(),
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getSize(),
                    item.getQuantity(),
                    item.getPriceAtOrder()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
            order.getId(),
            order.getUser().getId(),
            items,
            order.getTotalAmount(),
            order.getStatus().name(),
            order.getShippingAddress(),
            order.getPaymentMode().name(),
            order.getCreatedAt()
        );
    }
}
