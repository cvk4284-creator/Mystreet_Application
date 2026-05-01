import { useEffect, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { productAPI } from '../services/api';
import type { Product } from '../types';

export default function HomePage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchParams, setSearchParams] = useSearchParams();

  const selectedBrand = searchParams.get('brand') || '';
  const selectedSize = searchParams.get('size') || '';

  useEffect(() => {
    loadProducts();
  }, [selectedBrand, selectedSize]);

  const loadProducts = async () => {
    try {
      setLoading(true);
      const data = await productAPI.getAll(
        selectedBrand || undefined,
        selectedSize || undefined
      );
      setProducts(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load products');
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (filterType: string, value: string) => {
    const newParams = new URLSearchParams(searchParams);
    if (value) {
      newParams.set(filterType, value);
    } else {
      newParams.delete(filterType);
    }
    setSearchParams(newParams);
  };

  const brands = ['Nike', 'Adidas', 'Puma', 'New Balance'];
  const sizes = ['6', '7', '8', '9', '10', '11', '12'];

  if (loading) return <div className="loading">Loading products...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="home-page">
      <div className="filters">
        <div className="filter-group">
          <label>Brand:</label>
          <select
            value={selectedBrand}
            onChange={(e) => handleFilterChange('brand', e.target.value)}
          >
            <option value="">All Brands</option>
            {brands.map((brand) => (
              <option key={brand} value={brand}>
                {brand}
              </option>
            ))}
          </select>
        </div>

        <div className="filter-group">
          <label>Size:</label>
          <select
            value={selectedSize}
            onChange={(e) => handleFilterChange('size', e.target.value)}
          >
            <option value="">All Sizes</option>
            {sizes.map((size) => (
              <option key={size} value={size}>
                {size}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="product-grid">
        {products.length === 0 ? (
          <p>No products found</p>
        ) : (
          products.map((product) => (
            <div key={product.id} className="product-card">
              <img src={product.imageUrl} alt={product.name} />
              <h3>{product.name}</h3>
              <p className="brand">{product.brand}</p>
              <p className="price">${product.price.toFixed(2)}</p>
              <Link to={`/products/${product.id}`} className="btn">
                View Details
              </Link>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
