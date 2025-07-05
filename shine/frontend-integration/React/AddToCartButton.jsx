import React, { useState, useEffect } from 'react';
import { CartService } from '../CartService.js';

const AddToCartButton = ({ 
    variationId, 
    initialQuantity = 1, 
    maxStock = null,
    onSuccess, 
    onError,
    className = '',
    disabled = false 
}) => {
    const [quantity, setQuantity] = useState(initialQuantity);
    const [loading, setLoading] = useState(false);
    const [stockInfo, setStockInfo] = useState(null);
    const [error, setError] = useState(null);

    const cartService = new CartService();

    // Check stock availability on component mount
    useEffect(() => {
        if (variationId) {
            checkStock();
        }
    }, [variationId]);

    const checkStock = async () => {
        try {
            const stockData = await cartService.checkStock(variationId, 1);
            setStockInfo(stockData);
        } catch (error) {
            console.error('Error checking stock:', error);
        }
    };

    const handleQuantityChange = (newQuantity) => {
        const validQuantity = Math.max(1, Math.min(newQuantity, maxStock || newQuantity));
        setQuantity(validQuantity);
        setError(null);
    };

    const handleAddToCart = async () => {
        if (loading || disabled) return;

        setLoading(true);
        setError(null);

        try {
            // Check stock before adding
            if (stockInfo && !stockInfo.hasSufficientStock) {
                throw new Error('Insufficient stock available');
            }

            const result = await cartService.addToCart(variationId, quantity);
            
            // Reset quantity to initial value
            setQuantity(initialQuantity);
            
            // Call success callback
            if (onSuccess) {
                onSuccess(result);
            }

            // Show success message (you can customize this)
            console.log('Item added to cart successfully!', result);

        } catch (error) {
            setError(error.message);
            if (onError) {
                onError(error);
            }
        } finally {
            setLoading(false);
        }
    };

    const isOutOfStock = stockInfo && stockInfo.availableStock === 0;
    const isLowStock = stockInfo && stockInfo.availableStock > 0 && stockInfo.availableStock <= 5;
    const canAddToCart = !disabled && !loading && !isOutOfStock && quantity > 0;

    return (
        <div className={`add-to-cart-container ${className}`}>
            {/* Stock Status */}
            {stockInfo && (
                <div className="stock-status">
                    {isOutOfStock ? (
                        <span className="stock-out">Out of Stock</span>
                    ) : isLowStock ? (
                        <span className="stock-low">Only {stockInfo.availableStock} left!</span>
                    ) : (
                        <span className="stock-available">In Stock ({stockInfo.availableStock} available)</span>
                    )}
                </div>
            )}

            {/* Quantity Selector */}
            <div className="quantity-selector">
                <button
                    type="button"
                    onClick={() => handleQuantityChange(quantity - 1)}
                    disabled={quantity <= 1 || loading}
                    className="quantity-btn"
                >
                    -
                </button>
                <input
                    type="number"
                    value={quantity}
                    onChange={(e) => handleQuantityChange(parseInt(e.target.value) || 1)}
                    min="1"
                    max={maxStock || stockInfo?.availableStock || 99}
                    className="quantity-input"
                    disabled={loading}
                />
                <button
                    type="button"
                    onClick={() => handleQuantityChange(quantity + 1)}
                    disabled={
                        loading || 
                        (maxStock && quantity >= maxStock) ||
                        (stockInfo && quantity >= stockInfo.availableStock)
                    }
                    className="quantity-btn"
                >
                    +
                </button>
            </div>

            {/* Add to Cart Button */}
            <button
                onClick={handleAddToCart}
                disabled={!canAddToCart}
                className={`add-to-cart-btn ${loading ? 'loading' : ''} ${isOutOfStock ? 'out-of-stock' : ''}`}
            >
                {loading ? (
                    <span className="loading-spinner">Adding...</span>
                ) : isOutOfStock ? (
                    'Out of Stock'
                ) : (
                    'Add to Cart'
                )}
            </button>

            {/* Error Message */}
            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

            <style jsx>{`
                .add-to-cart-container {
                    display: flex;
                    flex-direction: column;
                    gap: 10px;
                    max-width: 300px;
                }

                .stock-status {
                    font-size: 14px;
                    font-weight: 500;
                }

                .stock-out {
                    color: #dc3545;
                }

                .stock-low {
                    color: #ffc107;
                }

                .stock-available {
                    color: #28a745;
                }

                .quantity-selector {
                    display: flex;
                    align-items: center;
                    gap: 5px;
                }

                .quantity-btn {
                    width: 35px;
                    height: 35px;
                    border: 1px solid #ddd;
                    background: #fff;
                    cursor: pointer;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 16px;
                    font-weight: bold;
                }

                .quantity-btn:disabled {
                    opacity: 0.5;
                    cursor: not-allowed;
                }

                .quantity-input {
                    width: 60px;
                    height: 35px;
                    text-align: center;
                    border: 1px solid #ddd;
                    border-radius: 4px;
                    font-size: 14px;
                }

                .add-to-cart-btn {
                    padding: 12px 24px;
                    background: #007bff;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    font-size: 16px;
                    font-weight: 500;
                    cursor: pointer;
                    transition: background-color 0.2s;
                }

                .add-to-cart-btn:hover:not(:disabled) {
                    background: #0056b3;
                }

                .add-to-cart-btn:disabled {
                    opacity: 0.6;
                    cursor: not-allowed;
                }

                .add-to-cart-btn.loading {
                    background: #6c757d;
                }

                .add-to-cart-btn.out-of-stock {
                    background: #6c757d;
                }

                .loading-spinner {
                    display: inline-block;
                    animation: spin 1s linear infinite;
                }

                @keyframes spin {
                    0% { transform: rotate(0deg); }
                    100% { transform: rotate(360deg); }
                }

                .error-message {
                    color: #dc3545;
                    font-size: 14px;
                    margin-top: 5px;
                }
            `}</style>
        </div>
    );
};

export default AddToCartButton; 