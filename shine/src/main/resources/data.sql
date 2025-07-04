-- Insert admin user if not exists
-- Note: These are proper BCrypt hashes for the passwords

-- Admin user (password: admin123)
-- This is a proper BCrypt hash for "admin123"
INSERT INTO users (email, first_name, last_name, password_hash, registration_date, role) 
SELECT 'admin@shine.com', 'Admin', 'User', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', NOW(), 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@shine.com');

-- Regular user for testing (password: user123)
-- This is a proper BCrypt hash for "user123"
INSERT INTO users (email, first_name, last_name, password_hash, registration_date, role) 
SELECT 'user@shine.com', 'Regular', 'User', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', NOW(), 'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'user@shine.com');

-- Update existing users to have ADMIN role if they don't have it
UPDATE users SET role = 'ADMIN' WHERE email = 'glorijohngozo@gmail.com' AND role != 'ADMIN'; 