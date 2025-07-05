-- Sample data for cart system testing
-- This script adds sample products, variations, and stock for testing the cart functionality

-- Insert sample categories
INSERT INTO categories (CategoryName, Description) VALUES 
('T-Shirts', 'Casual and formal t-shirts'),
('Jeans', 'Denim jeans for all occasions'),
('Shoes', 'Footwear for various activities');

-- Insert sample sizes
INSERT INTO sizes (SizeName, Description) VALUES 
('XS', 'Extra Small'),
('S', 'Small'),
('M', 'Medium'),
('L', 'Large'),
('XL', 'Extra Large'),
('XXL', 'Extra Extra Large');

-- Insert sample colors
INSERT INTO colors (ColorName, HexCode) VALUES 
('Black', '#000000'),
('White', '#FFFFFF'),
('Blue', '#0000FF'),
('Red', '#FF0000'),
('Green', '#00FF00'),
('Gray', '#808080');

-- Insert sample products
INSERT INTO products (ProductName, Description, SKU, Price, ImageURL, StockQuantity, CategoryID) VALUES 
('Classic Cotton T-Shirt', 'Comfortable cotton t-shirt for everyday wear', 'TSHIRT-001', 29.99, 'https://example.com/tshirt-black.jpg', 100, 1),
('Premium Denim Jeans', 'High-quality denim jeans with perfect fit', 'JEANS-001', 89.99, 'https://example.com/jeans-blue.jpg', 50, 2),
('Casual Sneakers', 'Comfortable sneakers for casual outings', 'SHOES-001', 79.99, 'https://example.com/sneakers-white.jpg', 75, 3),
('Polo Shirt', 'Elegant polo shirt for semi-formal occasions', 'POLO-001', 49.99, 'https://example.com/polo-blue.jpg', 60, 1),
('Slim Fit Jeans', 'Modern slim fit jeans for trendy look', 'JEANS-002', 99.99, 'https://example.com/jeans-black.jpg', 40, 2);

-- Insert sample product variations with stock
INSERT INTO product_variations (ProductID, SizeID, ColorID, AdditionalStock) VALUES 
-- T-Shirt variations
(1, 3, 1, 20), -- Medium Black T-Shirt
(1, 3, 2, 15), -- Medium White T-Shirt
(1, 4, 1, 18), -- Large Black T-Shirt
(1, 4, 2, 12), -- Large White T-Shirt
(1, 5, 1, 10), -- XL Black T-Shirt
(1, 5, 2, 8),  -- XL White T-Shirt

-- Jeans variations
(2, 3, 3, 12), -- Medium Blue Jeans
(2, 4, 3, 15), -- Large Blue Jeans
(2, 5, 3, 8),  -- XL Blue Jeans
(2, 3, 1, 10), -- Medium Black Jeans
(2, 4, 1, 12), -- Large Black Jeans
(2, 5, 1, 6),  -- XL Black Jeans

-- Sneakers variations
(3, 3, 2, 25), -- Medium White Sneakers
(3, 4, 2, 30), -- Large White Sneakers
(3, 5, 2, 20), -- XL White Sneakers
(3, 3, 1, 15), -- Medium Black Sneakers
(3, 4, 1, 18), -- Large Black Sneakers
(3, 5, 1, 12), -- XL Black Sneakers

-- Polo variations
(4, 3, 3, 20), -- Medium Blue Polo
(4, 4, 3, 25), -- Large Blue Polo
(4, 5, 3, 15), -- XL Blue Polo
(4, 3, 1, 18), -- Medium Black Polo
(4, 4, 1, 22), -- Large Black Polo
(4, 5, 1, 12), -- XL Black Polo

-- Slim Fit Jeans variations
(5, 3, 1, 15), -- Medium Black Slim Jeans
(5, 4, 1, 18), -- Large Black Slim Jeans
(5, 5, 1, 10), -- XL Black Slim Jeans
(5, 3, 3, 12), -- Medium Blue Slim Jeans
(5, 4, 3, 15), -- Large Blue Slim Jeans
(5, 5, 3, 8);  -- XL Blue Slim Jeans

-- Create shopping cart for existing users (if any)
-- Note: This will only work if users already exist in the database
-- You may need to adjust the UserID values based on your actual user data

-- Example: Create shopping cart for user with ID 1 (if exists)
-- INSERT INTO shopping_carts (UserID, CreationDate) VALUES (1, NOW());

-- Example: Add some items to cart for testing (adjust UserID and CartID as needed)
-- INSERT INTO cart_items (CartID, VariationID, ProductID, Quantity) VALUES 
-- (1, 1, 1, 2),  -- 2 Medium Black T-Shirts
-- (1, 7, 2, 1);  -- 1 Medium Blue Jeans 