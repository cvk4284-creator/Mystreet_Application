import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { useCart } from '../contexts/CartContext';

export default function Navbar() {
  const { isAuthenticated, isAdmin, logout, user } = useAuth();
  const { totalItems } = useCart();

  return (
    <nav className="navbar">
      <div className="nav-container">
        <Link to="/" className="nav-brand">
          MyStreeT
        </Link>

        <div className="nav-links">
          <Link to="/">Products</Link>

          {isAuthenticated && (
            <>
              <Link to="/orders">My Orders</Link>
              {isAdmin && <Link to="/admin/products">Admin</Link>}
            </>
          )}

          <Link to="/cart" className="cart-link">
            Cart {totalItems > 0 && <span className="badge">{totalItems}</span>}
          </Link>

          {isAuthenticated ? (
            <div className="user-menu">
              <span>{user?.email}</span>
              <button onClick={logout} className="btn btn-sm">
                Logout
              </button>
            </div>
          ) : (
            <>
              <Link to="/login">Login</Link>
              <Link to="/register">Register</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
