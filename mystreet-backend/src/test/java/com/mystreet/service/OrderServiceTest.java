package com.mystreet.service;

import com.mystreet.dto.OrderItemRequest;
import com.mystreet.dto.OrderRequest;
import com.mystreet.dto.OrderResponse;
import com.mystreet.exception.BadRequestException;
import com.mystreet.exception.ResourceNotFoundException;
import com.mystreet.model.Order;
import com.mystreet.model.Product;
import com.mystreet.model.User;
import com.mystreet.repository.OrderRepository;
import com.mystreet.repository.ProductRepository;
import com.mystreet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;
    private OrderRequest orderRequest;
    private Order order;
    private UUID userId;
    private UUID productId;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        productId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setIsAdmin(false);

        product = new Product();
        product.setId(productId);
        product.setName("Air Max 90");
        product.setPrice(new BigDecimal("119.99"));
        product.setStockQty(50);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(productId);
        itemRequest.setSize("9");
        itemRequest.setQuantity(2);

        orderRequest = new OrderRequest();
        orderRequest.setItems(Arrays.asList(itemRequest));
        orderRequest.setShippingAddress("123 Main St");
        orderRequest.setPaymentMode("COD");

        order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setItems(new ArrayList<>());
        order.setTotalAmount(new BigDecimal("239.98"));
        order.setStatus(Order.OrderStatus.PLACED);
        order.setPaymentMode(Order.PaymentMode.COD);
    }

    @Test
    void createOrder_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.createOrder(orderRequest, "test@example.com");

        assertNotNull(response);
        assertEquals(orderId, response.getId());
        verify(orderRepository).save(any(Order.class));
        verify(productRepository).save(product);
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.createOrder(orderRequest, "test@example.com"));
    }

    @Test
    void createOrder_ProductNotFound_ThrowsException() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.createOrder(orderRequest, "test@example.com"));
    }

    @Test
    void createOrder_InsufficientStock_ThrowsException() {
        product.setStockQty(1);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class,
                () -> orderService.createOrder(orderRequest, "test@example.com"));
    }

    @Test
    void createOrder_InvalidPaymentMode_ThrowsException() {
        orderRequest.setPaymentMode("INVALID");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class,
                () -> orderService.createOrder(orderRequest, "test@example.com"));
    }

    @Test
    void getMyOrders_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findByUserOrderByCreatedAtDesc(user))
                .thenReturn(Arrays.asList(order));

        var orders = orderService.getMyOrders("test@example.com");

        assertNotNull(orders);
        assertEquals(1, orders.size());
    }

    @Test
    void getOrderById_Success() {
        order.getItems().clear();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getOrderById(orderId, "test@example.com");

        assertNotNull(response);
        assertEquals(orderId, response.getId());
    }

    @Test
    void getOrderById_NotFound_ThrowsException() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrderById(orderId, "test@example.com"));
    }
}
