import { useNavigate } from 'react-router-dom';
import { useCart } from '../contexts/CartContext';
import { useAuth } from '../contexts/AuthContext';

export default function CartPage() {
  const navigate = useNavigate();
  const { cart, removeFromCart, updateQuantity, totalAmount } = useCart();
  const { isAuthenticated } = useAuth();

  const handleCheckout = () => {
    if (!isAuthenticated) {
      navigate('/login', { state: { from: { pathname: '/checkout' } } });
    } else {
      navigate('/checkout');
    }
  };

  if (cart.length === 0) {
    return (
      <div className="cart-page">
        <h1>Shopping Cart</h1>
        <div className="empty-cart">
          <p>Your cart is empty</p>
          <button onClick={() => navigate('/')} className="btn">
            Continue Shopping
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="cart-page">
      <h1>Shopping Cart</h1>

      <div className="cart-items">
        <table>
          <thead>
            <tr>
              <th>Product</th>
              <th>Size</th>
              <th>Price</th>
              <th>Quantity</th>
              <th>Total</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {cart.map((item) => (
              <tr key={`${item.productId}-${item.size}`}>
                <td>
                  <div className="cart-item-info">
                    <img src={item.imageUrl} alt={item.name} />
                    <div>
                      <h4>{item.name}</h4>
                      <p>{item.brand}</p>
                    </div>
                  </div>
                </td>
                <td>{item.size}</td>
                <td>${item.price.toFixed(2)}</td>
                <td>
                  <input
                    type="number"
                    min="1"
                    value={item.quantity}
                    onChange={(e) =>
                      updateQuantity(item.productId, item.size, Number(e.target.value))
                    }
                  />
                </td>
                <td>${(item.price * item.quantity).toFixed(2)}</td>
                <td>
                  <button
                    onClick={() => removeFromCart(item.productId, item.size)}
                    className="btn btn-danger"
                  >
                    Remove
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="cart-summary">
        <h2>Total: ${totalAmount.toFixed(2)}</h2>
        <button onClick={handleCheckout} className="btn btn-primary">
          Proceed to Checkout
        </button>
      </div>
    </div>
  );
}
