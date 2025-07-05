# Enhanced Cart System Implementation

This document describes the comprehensive add-to-cart system that has been implemented for the Shine e-commerce platform. The system integrates with user authentication, provides stock management, and offers a complete shopping cart experience.

## Overview

The cart system consists of several key components:

1. **Enhanced Cart Service** - Core business logic for cart operations
2. **Stock Service** - Manages product stock quantities
3. **Enhanced Cart Controller** - REST API endpoints for cart operations
4. **Stock Controller** - Admin endpoints for stock management
5. **DTOs** - Data transfer objects for API communication

## Key Features

### 🔐 User Authentication Integration
- All cart operations require user authentication
- Cart items are automatically associated with the logged-in user
- Shopping carts are created automatically for new users

### 📦 Stock Management
- Real-time stock validation before adding items to cart
- Stock reservation and release functionality
- Admin tools for stock management

### 🛒 Cart Operations
- Add items to cart with quantity validation
- Update item quantities
- Remove items from cart
- Clear entire cart
- Get cart summary (total items and price)

### 🛡️ Security Features
- User ownership validation for all cart operations
- Stock availability checks
- Input validation and error handling

## API Endpoints

### Cart Operations (`/api/cart`)

#### Add Item to Cart
```http
POST /api/cart/add
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "variationId": 1,
  "quantity": 2
}
```

#### Get Cart Items
```http
GET /api/cart/items
Authorization: Bearer <jwt-token>
```

#### Update Item Quantity
```http
PUT /api/cart/update-quantity
Content-Type: application/json
Authorization: Bearer <jwt-token>

{
  "cartItemId": 1,
  "quantity": 3
}
```

#### Remove Item from Cart
```http
DELETE /api/cart/remove/{cartItemId}
Authorization: Bearer <jwt-token>
```

#### Clear Cart
```http
DELETE /api/cart/clear
Authorization: Bearer <jwt-token>
```

#### Get Cart Summary
```http
GET /api/cart/summary
Authorization: Bearer <jwt-token>
```

### Stock Management (`/api/stock`)

#### Get Stock Information
```http
GET /api/stock/{variationId}
```

#### Check Stock Availability
```http
GET /api/stock/{variationId}/check?quantity=5
```

#### Update Stock (Admin)
```http
PUT /api/stock/{variationId}
Content-Type: application/json

{
  "quantity": 100
}
```

#### Reserve Stock
```http
POST /api/stock/{variationId}/reserve
Content-Type: application/json

{
  "quantity": 5
}
```

#### Release Stock
```http
POST /api/stock/{variationId}/release
Content-Type: application/json

{
  "quantity": 5
}
```

## Data Models

### AddToCartRequestDto
```java
{
  "variationId": Integer,  // Product variation ID
  "quantity": Integer      // Quantity to add
}
```

### CartItemResponseDto
```java
{
  "cartItemId": Integer,
  "productId": Integer,
  "productName": String,
  "productImage": String,
  "productPrice": BigDecimal,
  "variationId": Integer,
  "sizeName": String,
  "colorName": String,
  "quantity": Integer,
  "availableStock": Integer,
  "totalPrice": BigDecimal
}
```

### UpdateQuantityRequestDto
```java
{
  "cartItemId": Integer,   // Cart item ID to update
  "quantity": Integer      // New quantity
}
```

## Database Schema

The cart system uses the following database tables:

### cart_items
- `CartItemID` (Primary Key)
- `CartID` (Foreign Key to shopping_carts)
- `VariationID` (Foreign Key to product_variations)
- `ProductID` (Foreign Key to products)
- `Quantity`

### shopping_carts
- `CartID` (Primary Key)
- `UserID` (Foreign Key to users)
- `CreationDate`

### product_variations
- `VariationID` (Primary Key)
- `ProductID` (Foreign Key to products)
- `SizeID` (Foreign Key to sizes)
- `ColorID` (Foreign Key to colors)
- `AdditionalStock`

## Setup Instructions

### 1. Database Setup
Run the sample data script to populate the database with test products:
```sql
-- Execute the data-cart-sample.sql script
```

### 2. Authentication
Ensure users are registered and can authenticate. The cart system requires:
- Valid JWT token in Authorization header
- User must exist in the database

### 3. Testing the System

#### Test User Registration
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

#### Test Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### Test Add to Cart
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "variationId": 1,
    "quantity": 2
  }'
```

#### Test Get Cart
```bash
curl -X GET http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Error Handling

The system provides comprehensive error handling:

### Common Error Responses

#### 400 Bad Request
- Invalid request parameters
- Missing required fields
- Invalid quantity values

#### 401 Unauthorized
- Missing or invalid JWT token
- User not authenticated

#### 403 Forbidden
- User trying to access another user's cart
- Insufficient permissions

#### 409 Conflict
- Insufficient stock for requested quantity
- Stock reservation failed

#### 500 Internal Server Error
- Database connection issues
- Unexpected system errors

## Frontend Integration

### React Example

```javascript
// Add to cart function
const addToCart = async (variationId, quantity) => {
  try {
    const response = await fetch('/api/cart/add', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({
        variationId: variationId,
        quantity: quantity
      })
    });
    
    if (response.ok) {
      const cartItem = await response.json();
      console.log('Item added to cart:', cartItem);
    } else {
      const error = await response.text();
      console.error('Error adding to cart:', error);
    }
  } catch (error) {
    console.error('Network error:', error);
  }
};

// Get cart items
const getCartItems = async () => {
  try {
    const response = await fetch('/api/cart/items', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    
    if (response.ok) {
      const cartItems = await response.json();
      return cartItems;
    }
  } catch (error) {
    console.error('Error fetching cart:', error);
  }
};
```

## Security Considerations

1. **Authentication Required**: All cart operations require valid JWT tokens
2. **User Isolation**: Users can only access their own cart items
3. **Stock Validation**: Real-time stock checks prevent overselling
4. **Input Validation**: All inputs are validated before processing
5. **Transaction Safety**: Database operations use transactions for consistency

## Performance Considerations

1. **Database Indexes**: Ensure proper indexes on foreign keys
2. **Caching**: Consider caching frequently accessed cart data
3. **Batch Operations**: For large carts, consider batch operations
4. **Connection Pooling**: Configure appropriate database connection pools

## Troubleshooting

### Common Issues

1. **403 Forbidden**: Check JWT token and user authentication
2. **409 Conflict**: Verify stock availability for the product variation
3. **400 Bad Request**: Validate request parameters and data types
4. **500 Internal Server Error**: Check database connectivity and logs

### Debug Endpoints

Use the existing debug endpoints to troubleshoot authentication issues:
- `/api/auth/current-user` - Get current user info
- `/api/auth/token-info` - Get JWT token details

## Future Enhancements

Potential improvements for the cart system:

1. **Cart Persistence**: Save cart items to database for cross-device access
2. **Wishlist Integration**: Add wishlist functionality
3. **Cart Sharing**: Allow users to share cart contents
4. **Bulk Operations**: Add/remove multiple items at once
5. **Cart Expiration**: Automatic cart cleanup for inactive users
6. **Real-time Updates**: WebSocket integration for real-time cart updates
7. **Analytics**: Track cart abandonment and conversion rates

## Support

For issues or questions about the cart system implementation, check:
1. Application logs for detailed error messages
2. Database connection and schema
3. JWT token validity and expiration
4. Stock availability in the database 