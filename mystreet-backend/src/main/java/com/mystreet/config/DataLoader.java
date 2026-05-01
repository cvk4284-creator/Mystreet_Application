package com.mystreet.config;

import com.mystreet.model.Product;
import com.mystreet.model.User;
import com.mystreet.repository.ProductRepository;
import com.mystreet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataLoader {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, ProductRepository productRepository) {
        return args -> {
            // Only load if database is empty
            if (userRepository.count() == 0) {
                // Create admin user
                User admin = new User();
                admin.setEmail("admin@mystreet.com");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setIsAdmin(true);
                admin.setFirstName("Admin");
                admin.setLastName("User");
                userRepository.save(admin);

                // Create regular user
                User user = new User();
                user.setEmail("user@mystreet.com");
                user.setPasswordHash(passwordEncoder.encode("user123"));
                user.setIsAdmin(false);
                user.setFirstName("Test");
                user.setLastName("User");
                userRepository.save(user);

                System.out.println("✅ Users created: admin@mystreet.com, user@mystreet.com");
            }

            if (productRepository.count() == 0) {
                // Nike products
                productRepository.save(createProduct("Air Max 90", "Nike",
                    "Classic retro vibe with iconic visible Air cushioning. Perfect for everyday wear with style and comfort.",
                    119.99, "https://images.unsplash.com/photo-1554995207-c18c203602cb?w=400", "7,8,9,10,11", 50));

                productRepository.save(createProduct("Air Jordan 1 Retro High", "Nike",
                    "Iconic basketball sneaker reimagined with premium leather and classic colorways.",
                    169.99, "https://images.unsplash.com/photo-1556906781-9a412961c28c?w=400", "7,8,9,10,11,12", 35));

                productRepository.save(createProduct("Nike Dunk Low", "Nike",
                    "Versatile and stylish sneaker with a heritage basketball design.",
                    99.99, "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", "7,8,9,10,11", 60));

                productRepository.save(createProduct("Nike Air Force 1", "Nike",
                    "The legendary basketball shoe that revolutionized sneaker culture. Clean, classic, and timeless.",
                    109.99, "https://images.unsplash.com/photo-1600185365926-3a2ce3cdb9eb?w=400", "6,7,8,9,10,11,12", 45));

                // Adidas products
                productRepository.save(createProduct("Ultraboost 22", "Adidas",
                    "Responsive Boost cushioning for maximum energy return. Perfect for running and all-day comfort.",
                    139.99, "https://images.unsplash.com/photo-1584735175315-9d5df23860e6?w=400", "7,8,9,10,11", 40));

                productRepository.save(createProduct("Stan Smith", "Adidas",
                    "Minimalist tennis-inspired sneaker with a clean, timeless design.",
                    89.99, "https://images.unsplash.com/photo-1543508282-6319a3e2621f?w=400", "6,7,8,9,10,11", 55));

                productRepository.save(createProduct("Yeezy Boost 350 V2", "Adidas",
                    "Innovative design collaboration with premium materials and distinctive style.",
                    229.99, "https://images.unsplash.com/photo-1605408499391-6368c628ef42?w=400", "7,8,9,10,11,12", 25));

                productRepository.save(createProduct("Superstar", "Adidas",
                    "Iconic shell-toe sneaker with a heritage basketball design. A true classic.",
                    79.99, "https://images.unsplash.com/photo-1597045566677-8cf032ed6634?w=400", "6,7,8,9,10,11,12", 70));

                // Puma products
                productRepository.save(createProduct("RS-X", "Puma",
                    "Bold retro running sneaker with chunky silhouette and vibrant colorways.",
                    99.99, "https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=400", "7,8,9,10,11", 30));

                productRepository.save(createProduct("Suede Classic", "Puma",
                    "Timeless suede sneaker with vintage appeal and modern comfort.",
                    69.99, "https://images.unsplash.com/photo-1552346154-21d32810aba3?w=400", "6,7,8,9,10,11", 50));

                // New Balance products
                productRepository.save(createProduct("574 Core", "New Balance",
                    "Classic lifestyle sneaker with ENCAP midsole cushioning for all-day comfort.",
                    84.99, "https://images.unsplash.com/photo-1539185441755-769473a23570?w=400", "7,8,9,10,11,12", 45));

                productRepository.save(createProduct("990v5", "New Balance",
                    "Premium Made in USA sneaker with superior craftsmanship and comfort.",
                    184.99, "https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=400", "7,8,9,10,11", 20));

                System.out.println("✅ 12 products loaded into database");
            }
        };
    }

    private Product createProduct(String name, String brand, String description,
                                   double price, String imageUrl, String sizes, int stock) {
        Product product = new Product();
        product.setName(name);
        product.setBrand(brand);
        product.setDescription(description);
        product.setPrice(new BigDecimal(price));
        product.setImageUrl(imageUrl);
        product.setSizesCsv(sizes);
        product.setStockQty(stock);
        return product;
    }
}
