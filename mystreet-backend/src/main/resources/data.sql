-- Insert Admin User (password: admin123)
INSERT INTO users (id, email, password_hash, is_admin, first_name, last_name, created_at, updated_at)
VALUES (RANDOM_UUID(), 'admin@mystreet.com', '$2a$10$KIjVXwGKVvOx1XvFYqvBr.dZ2K0h4YqQvJ3jOJqZQXjKxVQqGqGJu', true, 'Admin', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Regular User (password: user123)
INSERT INTO users (id, email, password_hash, is_admin, first_name, last_name, created_at, updated_at)
VALUES (RANDOM_UUID(), 'user@mystreet.com', '$2a$10$8cjYvD7Z8X9Z8kZ8Z8Z8ZuqJ3jOJqZQXjKxVQqGqGJu', false, 'Test', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Nike Products
INSERT INTO products (id, name, brand, description, price, image_url, sizes_csv, stock_qty, created_at, updated_at)
VALUES
(RANDOM_UUID(), 'Air Max 90', 'Nike', 'Classic retro vibe with iconic visible Air cushioning. Perfect for everyday wear with style and comfort.', 119.99, 'https://images.unsplash.com/photo-1554995207-c18c203602cb?w=400', '7,8,9,10,11', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(RANDOM_UUID(), 'Air Jordan 1 Retro High', 'Nike', 'Iconic basketball sneaker reimagined with premium leather and classic colorways.', 169.99, 'https://images.unsplash.com/photo-1556906781-9a412961c28c?w=400', '7,8,9,10,11,12', 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(RANDOM_UUID(), 'Nike Dunk Low', 'Nike', 'Versatile and stylish sneaker with a heritage basketball design.', 99.99, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', '7,8,9,10,11', 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(RANDOM_UUID(), 'Nike Air Force 1', 'Nike', 'The legendary basketball shoe that revolutionized sneaker culture. Clean, classic, and timeless.', 109.99, 'https://images.unsplash.com/photo-1600185365926-3a2ce3cdb9eb?w=400', '6,7,8,9,10,11,12', 45, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Adidas Products
INSERT INTO products (id, name, brand, description, price, image_url, sizes_csv, stock_qty, created_at, updated_at)
VALUES
(RANDOM_UUID(), 'Ultraboost 22', 'Adidas', 'Responsive Boost cushioning for maximum energy return. Perfect for running and all-day comfort.', 139.99, 'https://images.unsplash.com/photo-1584735175315-9d5df23860e6?w=400', '7,8,9,10,11', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(RANDOM_UUID(), 'Stan Smith', 'Adidas', 'Minimalist tennis-inspired sneaker with a clean, timeless design.', 89.99, 'https://images.unsplash.com/photo-1543508282-6319a3e2621f?w=400', '6,7,8,9,10,11', 55, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(RANDOM_UUID(), 'Yeezy Boost 350 V2', 'Adidas', 'Innovative design collaboration with premium materials and distinctive style.', 229.99, 'https://images.unsplash.com/photo-1619893810473-e2b4c7e59c26?w=400', '7,8,9,10,11,12', 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(RANDOM_UUID(), 'Superstar', 'Adidas', 'Iconic shell-toe sneaker with a heritage basketball design. A true classic.', 79.99, 'https://images.unsplash.com/photo-1597045566677-8cf032ed6634?w=400', '6,7,8,9,10,11,12', 70, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Puma Products
INSERT INTO products (id, name, brand, description, price, image_url, sizes_csv, stock_qty, created_at, updated_at)
VALUES
(RANDOM_UUID(), 'RS-X', 'Puma', 'Bold retro running sneaker with chunky silhouette and vibrant colorways.', 99.99, 'https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=400', '7,8,9,10,11', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(RANDOM_UUID(), 'Suede Classic', 'Puma', 'Timeless suede sneaker with vintage appeal and modern comfort.', 69.99, 'https://images.unsplash.com/photo-1552346154-21d32810aba3?w=400', '6,7,8,9,10,11', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert New Balance Products
INSERT INTO products (id, name, brand, description, price, image_url, sizes_csv, stock_qty, created_at, updated_at)
VALUES
(RANDOM_UUID(), '574 Core', 'New Balance', 'Classic lifestyle sneaker with ENCAP midsole cushioning for all-day comfort.', 84.99, 'https://images.unsplash.com/photo-1539185441755-769473a23570?w=400', '7,8,9,10,11,12', 45, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(RANDOM_UUID(), '990v5', 'New Balance', 'Premium Made in USA sneaker with superior craftsmanship and comfort.', 184.99, 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=400', '7,8,9,10,11', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
