import React, { useState, useEffect } from 'react';
import { CartService } from '../CartService.js';

const ShoppingCart = ({ 
    onCartUpdate, 
    className = '',
    showCheckoutButton = true 
}) => {
    const [cartItems, setCartItems] = useState([]);
    const [cartSummary, setCartSummary] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [updatingItems, setUpdatingItems] = useState(new Set());

    const cartService = new CartService();

    // Load cart data on component mount
    useEffect(() => {
        loadCart();
    }, []);

    const loadCart = async () => {
        try {
            setLoading(true);
            setError(null);

            const [items, summary] = await Promise.all([
                cartService.getCartItems(),
                cartService.getCartSummary()
            ]);

            setCartItems(items);
            setCartSummary(summary);

            if (onCartUpdate) {
                onCartUpdate(items, summary);
            }
        } catch (error) {
            setError('Failed to load cart: ' + error.message);
            console.error('Error loading cart:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleQuantityUpdate = async (cartItemId, newQuantity) => {
        if (updatingItems.has(cartItemId)) return;

        setUpdatingItems(prev => new Set(prev).add(cartItemId));

        try {
            const result = await cartService.updateQuantity(cartItemId, newQuantity);
            
            if (result === null) {
                // Item was removed (quantity set to 0)
                setCartItems(prev => prev.filter(item => item.cartItemId !== cartItemId));
            } else {
                // Item was updated
                setCartItems(prev => prev.map(item => 
                    item.cartItemId === cartItemId ? result : item
                ));
            }

            // Reload cart summary
            const summary = await cartService.getCartSummary();
            setCartSummary(summary);

            if (onCartUpdate) {
                onCartUpdate(cartItems, summary);
            }
        } catch (error) {
            setError('Failed to update quantity: ' + error.message);
            console.error('Error updating quantity:', error);
        } finally {
            setUpdatingItems(prev => {
                const newSet = new Set(prev);
                newSet.delete(cartItemId);
                return newSet;
            });
        }
    };

    const handleRemoveItem = async (cartItemId) => {
        if (updatingItems.has(cartItemId)) return;

        setUpdatingItems(prev => new Set(prev).add(cartItemId));

        try {
            await cartService.removeFromCart(cartItemId);
            
            setCartItems(prev => prev.filter(item => item.cartItemId !== cartItemId));
            
            // Reload cart summary
            const summary = await cartService.getCartSummary();
            setCartSummary(summary);

            if (onCartUpdate) {
                onCartUpdate(cartItems, summary);
            }
        } catch (error) {
            setError('Failed to remove item: ' + error.message);
            console.error('Error removing item:', error);
        } finally {
            setUpdatingItems(prev => {
                const newSet = new Set(prev);
                newSet.delete(cartItemId);
                return newSet;
            });
        }
    };

    const handleClearCart = async () => {
        if (!window.confirm('Are you sure you want to clear your cart?')) {
            return;
        }

        try {
            await cartService.clearCart();
            setCartItems([]);
            setCartSummary({ totalItems: 0, totalPrice: 0 });

            if (onCartUpdate) {
                onCartUpdate([], { totalItems: 0, totalPrice: 0 });
            }
        } catch (error) {
            setError('Failed to clear cart: ' + error.message);
            console.error('Error clearing cart:', error);
        }
    };

    const formatPrice = (price) => {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(price);
    };

    if (loading) {
        return (
            <div className={`shopping-cart ${className}`}>
                <div className="loading">Loading cart...</div>
            </div>
        );
    }

    if (error) {
        return (
            <div className={`shopping-cart ${className}`}>
                <div className="error-message">{error}</div>
                <button onClick={loadCart} className="retry-btn">Retry</button>
            </div>
        );
    }

    if (cartItems.length === 0) {
        return (
            <div className={`shopping-cart empty ${className}`}>
                <div className="empty-cart">
                    <h3>Your cart is empty</h3>
                    <p>Add some items to get started!</p>
                </div>
            </div>
        );
    }

    return (
        <div className={`shopping-cart ${className}`}>
            <div className="cart-header">
                <h2>Shopping Cart ({cartSummary?.totalItems || 0} items)</h2>
                <button 
                    onClick={handleClearCart} 
                    className="clear-cart-btn"
                    disabled={cartItems.length === 0}
                >
                    Clear Cart
                </button>
            </div>

            <div className="cart-items">
                {cartItems.map(item => (
                    <div key={item.cartItemId} className="cart-item">
                        <div className="item-image">
                            <img 
                                src={item.productImage || '/placeholder-product.jpg'} 
                                alt={item.productName}
                                onError={(e) => {
                                    e.target.src = '/placeholder-product.jpg';
                                }}
                            />
                        </div>

                        <div className="item-details">
                            <h4 className="product-name">{item.productName}</h4>
                            <p className="product-variation">
                                {item.sizeName} • {item.colorName}
                            </p>
                            <p className="product-price">
                                {formatPrice(item.productPrice)} each
                            </p>
                            
                            {item.availableStock < item.quantity && (
                                <p className="stock-warning">
                                    Only {item.availableStock} available
                                </p>
                            )}
                        </div>

                        <div className="item-quantity">
                            <div className="quantity-controls">
                                <button
                                    onClick={() => handleQuantityUpdate(item.cartItemId, item.quantity - 1)}
                                    disabled={updatingItems.has(item.cartItemId) || item.quantity <= 1}
                                    className="quantity-btn"
                                >
                                    -
                                </button>
                                <span className="quantity-display">
                                    {updatingItems.has(item.cartItemId) ? '...' : item.quantity}
                                </span>
                                <button
                                    onClick={() => handleQuantityUpdate(item.cartItemId, item.quantity + 1)}
                                    disabled={
                                        updatingItems.has(item.cartItemId) || 
                                        item.quantity >= item.availableStock
                                    }
                                    className="quantity-btn"
                                >
                                    +
                                </button>
                            </div>
                        </div>

                        <div className="item-total">
                            <p className="total-price">{formatPrice(item.totalPrice)}</p>
                        </div>

                        <div className="item-actions">
                            <button
                                onClick={() => handleRemoveItem(item.cartItemId)}
                                disabled={updatingItems.has(item.cartItemId)}
                                className="remove-btn"
                            >
                                Remove
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            <div className="cart-summary">
                <div className="summary-row">
                    <span>Subtotal:</span>
                    <span>{formatPrice(cartSummary?.totalPrice || 0)}</span>
                </div>
                <div className="summary-row">
                    <span>Items:</span>
                    <span>{cartSummary?.totalItems || 0}</span>
                </div>
            </div>

            {showCheckoutButton && (
                <div className="cart-actions">
                    <button 
                        className="checkout-btn"
                        onClick={() => {
                            // Implement checkout logic here
                            console.log('Proceeding to checkout...');
                        }}
                    >
                        Proceed to Checkout
                    </button>
                </div>
            )}

            <style jsx>{`
                .shopping-cart {
                    max-width: 800px;
                    margin: 0 auto;
                    padding: 20px;
                }

                .cart-header {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    margin-bottom: 20px;
                    padding-bottom: 10px;
                    border-bottom: 1px solid #eee;
                }

                .cart-header h2 {
                    margin: 0;
                    color: #333;
                }

                .clear-cart-btn {
                    padding: 8px 16px;
                    background: #dc3545;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                    font-size: 14px;
                }

                .clear-cart-btn:hover:not(:disabled) {
                    background: #c82333;
                }

                .clear-cart-btn:disabled {
                    opacity: 0.6;
                    cursor: not-allowed;
                }

                .cart-items {
                    display: flex;
                    flex-direction: column;
                    gap: 20px;
                }

                .cart-item {
                    display: grid;
                    grid-template-columns: 80px 1fr auto auto auto;
                    gap: 15px;
                    align-items: center;
                    padding: 15px;
                    border: 1px solid #eee;
                    border-radius: 8px;
                    background: white;
                }

                .item-image img {
                    width: 80px;
                    height: 80px;
                    object-fit: cover;
                    border-radius: 4px;
                }

                .item-details {
                    display: flex;
                    flex-direction: column;
                    gap: 5px;
                }

                .product-name {
                    margin: 0;
                    font-size: 16px;
                    font-weight: 600;
                    color: #333;
                }

                .product-variation {
                    margin: 0;
                    font-size: 14px;
                    color: #666;
                }

                .product-price {
                    margin: 0;
                    font-size: 14px;
                    color: #007bff;
                    font-weight: 500;
                }

                .stock-warning {
                    margin: 0;
                    font-size: 12px;
                    color: #ffc107;
                }

                .quantity-controls {
                    display: flex;
                    align-items: center;
                    gap: 8px;
                }

                .quantity-btn {
                    width: 30px;
                    height: 30px;
                    border: 1px solid #ddd;
                    background: white;
                    cursor: pointer;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 14px;
                    font-weight: bold;
                }

                .quantity-btn:disabled {
                    opacity: 0.5;
                    cursor: not-allowed;
                }

                .quantity-display {
                    min-width: 30px;
                    text-align: center;
                    font-weight: 500;
                }

                .total-price {
                    margin: 0;
                    font-size: 16px;
                    font-weight: 600;
                    color: #333;
                }

                .remove-btn {
                    padding: 6px 12px;
                    background: #6c757d;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                    font-size: 12px;
                }

                .remove-btn:hover:not(:disabled) {
                    background: #5a6268;
                }

                .remove-btn:disabled {
                    opacity: 0.6;
                    cursor: not-allowed;
                }

                .cart-summary {
                    margin-top: 20px;
                    padding: 20px;
                    background: #f8f9fa;
                    border-radius: 8px;
                }

                .summary-row {
                    display: flex;
                    justify-content: space-between;
                    margin-bottom: 10px;
                    font-size: 16px;
                }

                .summary-row:last-child {
                    margin-bottom: 0;
                    font-weight: 600;
                    font-size: 18px;
                    border-top: 1px solid #dee2e6;
                    padding-top: 10px;
                }

                .cart-actions {
                    margin-top: 20px;
                    text-align: center;
                }

                .checkout-btn {
                    padding: 15px 30px;
                    background: #28a745;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    font-size: 16px;
                    font-weight: 500;
                    cursor: pointer;
                }

                .checkout-btn:hover {
                    background: #218838;
                }

                .empty-cart {
                    text-align: center;
                    padding: 40px 20px;
                }

                .empty-cart h3 {
                    margin: 0 0 10px 0;
                    color: #333;
                }

                .empty-cart p {
                    margin: 0;
                    color: #666;
                }

                .loading {
                    text-align: center;
                    padding: 40px;
                    color: #666;
                }

                .error-message {
                    color: #dc3545;
                    text-align: center;
                    padding: 20px;
                }

                .retry-btn {
                    display: block;
                    margin: 10px auto;
                    padding: 8px 16px;
                    background: #007bff;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                }

                @media (max-width: 768px) {
                    .cart-item {
                        grid-template-columns: 1fr;
                        gap: 10px;
                    }

                    .item-image {
                        text-align: center;
                    }

                    .quantity-controls {
                        justify-content: center;
                    }

                    .cart-header {
                        flex-direction: column;
                        gap: 10px;
                        align-items: flex-start;
                    }
                }
            `}</style>
        </div>
    );
};

export default ShoppingCart; 