package com.mystreet.service;

import com.mystreet.dto.ProductRequest;
import com.mystreet.dto.ProductResponse;
import com.mystreet.exception.ResourceNotFoundException;
import com.mystreet.model.Product;
import com.mystreet.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponse> getAllProducts(String brand, String size) {
        List<Product> products;

        if (brand != null || size != null) {
            products = productRepository.findByBrandAndSize(brand, size);
        } else {
            products = productRepository.findAll();
        }

        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setSizesCsv(request.getSizesCsv());
        product.setStockQty(request.getStockQty());

        product = productRepository.save(product);
        return convertToResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setSizesCsv(request.getSizesCsv());
        product.setStockQty(request.getStockQty());

        product = productRepository.save(product);
        return convertToResponse(product);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getBrand(),
            product.getDescription(),
            product.getPrice(),
            product.getImageUrl(),
            product.getSizesCsv(),
            product.getStockQty(),
            product.getCreatedAt()
        );
    }
}
