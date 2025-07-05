/**
 * CartService - Handles all cart-related API operations
 * This service can be used with React, Vue, Angular, or vanilla JavaScript
 */
class CartService {
    constructor(baseURL = 'http://localhost:8080') {
        this.baseURL = baseURL;
        this.token = localStorage.getItem('jwt_token') || sessionStorage.getItem('jwt_token');
    }

    /**
     * Set the authentication token
     * @param {string} token - JWT token
     */
    setToken(token) {
        this.token = token;
        localStorage.setItem('jwt_token', token);
    }

    /**
     * Clear the authentication token
     */
    clearToken() {
        this.token = null;
        localStorage.removeItem('jwt_token');
        sessionStorage.removeItem('jwt_token');
    }

    /**
     * Get headers for API requests
     * @returns {Object} Headers object
     */
    getHeaders() {
        const headers = {
            'Content-Type': 'application/json',
        };
        
        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }
        
        return headers;
    }

    /**
     * Add item to cart
     * @param {number} variationId - Product variation ID
     * @param {number} quantity - Quantity to add
     * @returns {Promise<Object>} Cart item response
     */
    async addToCart(variationId, quantity) {
        try {
            const response = await fetch(`${this.baseURL}/api/cart/add`, {
                method: 'POST',
                headers: this.getHeaders(),
                body: JSON.stringify({
                    variationId: variationId,
                    quantity: quantity
                })
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to add to cart: ${errorText}`);
            }

            return await response.json();
        } catch (error) {
            console.error('Error adding to cart:', error);
            throw error;
        }
    }

    /**
     * Get user's cart items
     * @returns {Promise<Array>} Array of cart items
     */
    async getCartItems() {
        try {
            const response = await fetch(`${this.baseURL}/api/cart/items`, {
                method: 'GET',
                headers: this.getHeaders()
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to get cart items: ${errorText}`);
            }

            return await response.json();
        } catch (error) {
            console.error('Error getting cart items:', error);
            throw error;
        }
    }

    /**
     * Update cart item quantity
     * @param {number} cartItemId - Cart item ID
     * @param {number} quantity - New quantity
     * @returns {Promise<Object|null>} Updated cart item or null if removed
     */
    async updateQuantity(cartItemId, quantity) {
        try {
            const response = await fetch(`${this.baseURL}/api/cart/update-quantity`, {
                method: 'PUT',
                headers: this.getHeaders(),
                body: JSON.stringify({
                    cartItemId: cartItemId,
                    quantity: quantity
                })
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to update quantity: ${errorText}`);
            }

            // If quantity is 0, item is removed and response is a string
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            } else {
                return null; // Item was removed
            }
        } catch (error) {
            console.error('Error updating quantity:', error);
            throw error;
        }
    }

    /**
     * Remove item from cart
     * @param {number} cartItemId - Cart item ID
     * @returns {Promise<boolean>} Success status
     */
    async removeFromCart(cartItemId) {
        try {
            const response = await fetch(`${this.baseURL}/api/cart/remove/${cartItemId}`, {
                method: 'DELETE',
                headers: this.getHeaders()
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to remove from cart: ${errorText}`);
            }

            return true;
        } catch (error) {
            console.error('Error removing from cart:', error);
            throw error;
        }
    }

    /**
     * Clear entire cart
     * @returns {Promise<boolean>} Success status
     */
    async clearCart() {
        try {
            const response = await fetch(`${this.baseURL}/api/cart/clear`, {
                method: 'DELETE',
                headers: this.getHeaders()
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to clear cart: ${errorText}`);
            }

            return true;
        } catch (error) {
            console.error('Error clearing cart:', error);
            throw error;
        }
    }

    /**
     * Get cart summary
     * @returns {Promise<Object>} Cart summary with total items and price
     */
    async getCartSummary() {
        try {
            const response = await fetch(`${this.baseURL}/api/cart/summary`, {
                method: 'GET',
                headers: this.getHeaders()
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to get cart summary: ${errorText}`);
            }

            return await response.json();
        } catch (error) {
            console.error('Error getting cart summary:', error);
            throw error;
        }
    }

    /**
     * Check stock availability
     * @param {number} variationId - Product variation ID
     * @param {number} quantity - Quantity to check
     * @returns {Promise<Object>} Stock availability info
     */
    async checkStock(variationId, quantity) {
        try {
            const response = await fetch(`${this.baseURL}/api/stock/${variationId}/check?quantity=${quantity}`, {
                method: 'GET',
                headers: this.getHeaders()
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to check stock: ${errorText}`);
            }

            return await response.json();
        } catch (error) {
            console.error('Error checking stock:', error);
            throw error;
        }
    }

    /**
     * Get stock information
     * @param {number} variationId - Product variation ID
     * @returns {Promise<Object>} Stock information
     */
    async getStock(variationId) {
        try {
            const response = await fetch(`${this.baseURL}/api/stock/${variationId}`, {
                method: 'GET',
                headers: this.getHeaders()
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Failed to get stock: ${errorText}`);
            }

            return await response.json();
        } catch (error) {
            console.error('Error getting stock:', error);
            throw error;
        }
    }
}

// Export for different module systems
if (typeof module !== 'undefined' && module.exports) {
    // CommonJS (Node.js)
    module.exports = CartService;
} else if (typeof define === 'function' && define.amd) {
    // AMD
    define([], function() { return CartService; });
} else {
    // Browser global
    window.CartService = CartService;
} 