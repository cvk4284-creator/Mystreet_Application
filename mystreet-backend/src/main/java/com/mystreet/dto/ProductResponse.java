package com.mystreet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private UUID id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String sizesCsv;
    private Integer stockQty;
    private LocalDateTime createdAt;
}
