package com.mystreet.repository;

import com.mystreet.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByBrand(String brand);

    @Query("SELECT p FROM Product p WHERE " +
           "(:brand IS NULL OR p.brand = :brand) AND " +
           "(:size IS NULL OR p.sizesCsv LIKE %:size%)")
    List<Product> findByBrandAndSize(
        @Param("brand") String brand,
        @Param("size") String size
    );
}
