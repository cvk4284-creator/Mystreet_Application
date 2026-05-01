import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { orderAPI } from '../services/api';
import type { Order } from '../types';

export default function OrderConfirmationPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (id) {
      loadOrder();
    }
  }, [id]);

  const loadOrder = async () => {
    try {
      setLoading(true);
      const data = await orderAPI.getById(id!);
      setOrder(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load order');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">{error}</div>;
  if (!order) return <div>Order not found</div>;

  return (
    <div className="confirmation-page">
      <div className="confirmation-card">
        <h1>Order Confirmed!</h1>
        <p className="success-message">Thank you for your order!</p>

        <div className="order-details">
          <h2>Order Details</h2>
          <p>
            <strong>Order ID:</strong> {order.id}
          </p>
          <p>
            <strong>Status:</strong> {order.status}
          </p>
          <p>
            <strong>Payment Method:</strong> {order.paymentMode}
          </p>
          <p>
            <strong>Shipping Address:</strong> {order.shippingAddress}
          </p>
        </div>

        <div className="order-items">
          <h3>Items:</h3>
          {order.items.map((item) => (
            <div key={item.id} className="order-item">
              <span>
                {item.productName} (Size {item.size}) x {item.quantity}
              </span>
              <span>${(item.priceAtOrder * item.quantity).toFixed(2)}</span>
            </div>
          ))}
        </div>

        <div className="order-total">
          <strong>Total Amount:</strong>
          <strong>${order.totalAmount.toFixed(2)}</strong>
        </div>

        <div className="actions">
          <button onClick={() => navigate('/')} className="btn">
            Continue Shopping
          </button>
          <button onClick={() => navigate('/orders')} className="btn btn-primary">
            View My Orders
          </button>
        </div>
      </div>
    </div>
  );
}
