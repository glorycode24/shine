-- Fix Admin Role Script
-- Run these commands in your MySQL database

-- 1. Check current user role
SELECT email, role FROM users WHERE email = 'glorijohngozo@gmail.com';

-- 2. Update user to ADMIN role
UPDATE users SET role = 'ADMIN' WHERE email = 'glorijohngozo@gmail.com';

-- 3. Verify the update
SELECT email, role FROM users WHERE email = 'glorijohngozo@gmail.com';

-- 4. Check all users and their roles
SELECT email, first_name, last_name, role FROM users ORDER BY role, email; 