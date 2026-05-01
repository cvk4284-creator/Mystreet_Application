import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../contexts/CartContext';
import { orderAPI } from '../services/api';

export default function CheckoutPage() {
  const navigate = useNavigate();
  const { cart, clearCart, totalAmount } = useCart();

  const [shippingAddress, setShippingAddress] = useState('');
  const [paymentMode, setPaymentMode] = useState('COD');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!shippingAddress) {
      setError('Shipping address is required');
      return;
    }

    if (cart.length === 0) {
      setError('Cart is empty');
      return;
    }

    try {
      setLoading(true);
      const orderData = {
        items: cart.map((item) => ({
          productId: item.productId,
          size: item.size,
          quantity: item.quantity,
        })),
        shippingAddress,
        paymentMode,
      };

      const order = await orderAPI.create(orderData);
      clearCart();
      navigate(`/orders/${order.id}`);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to place order');
    } finally {
      setLoading(false);
    }
  };

  if (cart.length === 0) {
    return (
      <div className="checkout-page">
        <h1>Checkout</h1>
        <p>Your cart is empty</p>
      </div>
    );
  }

  return (
    <div className="checkout-page">
      <h1>Checkout</h1>
      {error && <div className="error">{error}</div>}

      <div className="checkout-content">
        <div className="checkout-form">
          <form onSubmit={handleSubmit}>
            <h2>Shipping Information</h2>

            <div className="form-group">
              <label>Shipping Address:</label>
              <textarea
                value={shippingAddress}
                onChange={(e) => setShippingAddress(e.target.value)}
                rows={4}
                required
                placeholder="Enter your full address"
              />
            </div>

            <h2>Payment Method</h2>

            <div className="form-group">
              <label>
                <input
                  type="radio"
                  value="COD"
                  checked={paymentMode === 'COD'}
                  onChange={(e) => setPaymentMode(e.target.value)}
                />
                Cash on Delivery
              </label>
            </div>

            <div className="form-group">
              <label>
                <input
                  type="radio"
                  value="MOCK_UPI"
                  checked={paymentMode === 'MOCK_UPI'}
                  onChange={(e) => setPaymentMode(e.target.value)}
                />
                Mock UPI Payment
              </label>
            </div>

            <button type="submit" disabled={loading} className="btn btn-primary">
              {loading ? 'Placing Order...' : 'Place Order'}
            </button>
          </form>
        </div>

        <div className="order-summary">
          <h2>Order Summary</h2>
          {cart.map((item) => (
            <div key={`${item.productId}-${item.size}`} className="summary-item">
              <span>
                {item.name} (Size {item.size}) x {item.quantity}
              </span>
              <span>${(item.price * item.quantity).toFixed(2)}</span>
            </div>
          ))}
          <div className="summary-total">
            <strong>Total:</strong>
            <strong>${totalAmount.toFixed(2)}</strong>
          </div>
        </div>
      </div>
    </div>
  );
}
