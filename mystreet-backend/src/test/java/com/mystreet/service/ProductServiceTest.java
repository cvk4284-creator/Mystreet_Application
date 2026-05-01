package com.mystreet.service;

import com.mystreet.dto.ProductRequest;
import com.mystreet.dto.ProductResponse;
import com.mystreet.exception.ResourceNotFoundException;
import com.mystreet.model.Product;
import com.mystreet.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequest productRequest;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();

        product = new Product();
        product.setId(productId);
        product.setName("Air Max 90");
        product.setBrand("Nike");
        product.setDescription("Classic sneaker");
        product.setPrice(new BigDecimal("119.99"));
        product.setImageUrl("http://example.com/image.jpg");
        product.setSizesCsv("7,8,9,10");
        product.setStockQty(50);

        productRequest = new ProductRequest();
        productRequest.setName("Air Max 90");
        productRequest.setBrand("Nike");
        productRequest.setDescription("Classic sneaker");
        productRequest.setPrice(new BigDecimal("119.99"));
        productRequest.setImageUrl("http://example.com/image.jpg");
        productRequest.setSizesCsv("7,8,9,10");
        productRequest.setStockQty(50);
    }

    @Test
    void getAllProducts_NoFilters_ReturnsAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<ProductResponse> products = productService.getAllProducts(null, null);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Air Max 90", products.get(0).getName());
    }

    @Test
    void getAllProducts_WithFilters_ReturnsFilteredProducts() {
        when(productRepository.findByBrandAndSize("Nike", "9"))
                .thenReturn(Arrays.asList(product));

        List<ProductResponse> products = productService.getAllProducts("Nike", "9");

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository).findByBrandAndSize("Nike", "9");
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(productId);

        assertNotNull(response);
        assertEquals("Air Max 90", response.getName());
        assertEquals("Nike", response.getBrand());
    }

    @Test
    void getProductById_NotFound_ThrowsException() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(productId));
    }

    @Test
    void createProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(productRequest);

        assertNotNull(response);
        assertEquals("Air Max 90", response.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.updateProduct(productId, productRequest);

        assertNotNull(response);
        assertEquals("Air Max 90", response.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_NotFound_ThrowsException() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(productId, productRequest));
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.existsById(productId)).thenReturn(true);

        productService.deleteProduct(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    void deleteProduct_NotFound_ThrowsException() {
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(productId));
    }
}
