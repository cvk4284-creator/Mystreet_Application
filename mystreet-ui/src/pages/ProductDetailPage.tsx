import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { productAPI } from '../services/api';
import { useCart } from '../contexts/CartContext';
import type { Product } from '../types';

export default function ProductDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { addToCart } = useCart();

  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedSize, setSelectedSize] = useState('');
  const [quantity, setQuantity] = useState(1);

  useEffect(() => {
    if (id) {
      loadProduct();
    }
  }, [id]);

  const loadProduct = async () => {
    try {
      setLoading(true);
      const data = await productAPI.getById(id!);
      setProduct(data);
      const sizes = data.sizesCsv.split(',');
      setSelectedSize(sizes[0]);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load product');
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = () => {
    if (!product || !selectedSize) return;

    addToCart({
      productId: product.id,
      name: product.name,
      brand: product.brand,
      price: product.price,
      imageUrl: product.imageUrl,
      size: selectedSize,
      quantity,
    });

    alert('Added to cart!');
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">{error}</div>;
  if (!product) return <div>Product not found</div>;

  const sizes = product.sizesCsv.split(',').map((s) => s.trim());

  return (
    <div className="product-detail-page">
      <button onClick={() => navigate(-1)} className="back-btn">
        ← Back
      </button>

      <div className="product-detail">
        <div className="product-image">
          <img src={product.imageUrl} alt={product.name} />
        </div>

        <div className="product-info">
          <h1>{product.name}</h1>
          <p className="brand">{product.brand}</p>
          <p className="price">${product.price.toFixed(2)}</p>
          <p className="description">{product.description}</p>

          <div className="product-options">
            <div className="option-group">
              <label>Size:</label>
              <select
                value={selectedSize}
                onChange={(e) => setSelectedSize(e.target.value)}
              >
                {sizes.map((size) => (
                  <option key={size} value={size}>
                    {size}
                  </option>
                ))}
              </select>
            </div>

            <div className="option-group">
              <label>Quantity:</label>
              <input
                type="number"
                min="1"
                max={product.stockQty}
                value={quantity}
                onChange={(e) => setQuantity(Number(e.target.value))}
              />
            </div>
          </div>

          <p className="stock">
            {product.stockQty > 0 ? `${product.stockQty} in stock` : 'Out of stock'}
          </p>

          <button
            onClick={handleAddToCart}
            disabled={product.stockQty === 0}
            className="btn btn-primary"
          >
            Add to Cart
          </button>
        </div>
      </div>
    </div>
  );
}
