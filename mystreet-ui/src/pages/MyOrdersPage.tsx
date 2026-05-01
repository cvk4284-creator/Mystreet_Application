import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { orderAPI } from '../services/api';
import type { Order } from '../types';

export default function MyOrdersPage() {
  const navigate = useNavigate();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadOrders();
  }, []);

  const loadOrders = async () => {
    try {
      setLoading(true);
      const data = await orderAPI.getMine();
      setOrders(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load orders');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="orders-page">
      <h1>My Orders</h1>

      {orders.length === 0 ? (
        <div className="no-orders">
          <p>You haven't placed any orders yet</p>
          <button onClick={() => navigate('/')} className="btn">
            Start Shopping
          </button>
        </div>
      ) : (
        <div className="orders-list">
          {orders.map((order) => (
            <div key={order.id} className="order-card">
              <div className="order-header">
                <h3>Order #{order.id.substring(0, 8)}</h3>
                <span className="order-status">{order.status}</span>
              </div>

              <p>
                <strong>Date:</strong>{' '}
                {new Date(order.createdAt).toLocaleDateString()}
              </p>
              <p>
                <strong>Total:</strong> ${order.totalAmount.toFixed(2)}
              </p>
              <p>
                <strong>Items:</strong> {order.items.length}
              </p>

              <button
                onClick={() => navigate(`/orders/${order.id}`)}
                className="btn"
              >
                View Details
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
