import { useEffect, useState } from 'react';
import { productAPI } from '../services/api';
import type { Product } from '../types';

export default function AdminProductsPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);

  const [formData, setFormData] = useState({
    name: '',
    brand: '',
    description: '',
    price: '',
    imageUrl: '',
    sizesCsv: '',
    stockQty: '',
  });

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {
      setLoading(true);
      const data = await productAPI.getAll();
      setProducts(data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to load products');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (product: Product) => {
    setEditingProduct(product);
    setFormData({
      name: product.name,
      brand: product.brand,
      description: product.description,
      price: product.price.toString(),
      imageUrl: product.imageUrl,
      sizesCsv: product.sizesCsv,
      stockQty: product.stockQty.toString(),
    });
    setShowForm(true);
  };

  const handleDelete = async (id: string) => {
    if (!confirm('Are you sure you want to delete this product?')) return;

    try {
      await productAPI.delete(id);
      loadProducts();
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to delete product');
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const productData = {
        name: formData.name,
        brand: formData.brand,
        description: formData.description,
        price: parseFloat(formData.price),
        imageUrl: formData.imageUrl,
        sizesCsv: formData.sizesCsv,
        stockQty: parseInt(formData.stockQty),
      };

      if (editingProduct) {
        await productAPI.update(editingProduct.id, productData);
      } else {
        await productAPI.create(productData);
      }

      resetForm();
      loadProducts();
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to save product');
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      brand: '',
      description: '',
      price: '',
      imageUrl: '',
      sizesCsv: '',
      stockQty: '',
    });
    setEditingProduct(null);
    setShowForm(false);
  };

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className="admin-page">
      <div className="admin-header">
        <h1>Manage Products</h1>
        <button onClick={() => setShowForm(!showForm)} className="btn btn-primary">
          {showForm ? 'Cancel' : 'Add New Product'}
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      {showForm && (
        <div className="product-form">
          <h2>{editingProduct ? 'Edit Product' : 'Add New Product'}</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Name:</label>
              <input
                type="text"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                required
              />
            </div>

            <div className="form-group">
              <label>Brand:</label>
              <input
                type="text"
                value={formData.brand}
                onChange={(e) => setFormData({ ...formData, brand: e.target.value })}
                required
              />
            </div>

            <div className="form-group">
              <label>Description:</label>
              <textarea
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                rows={3}
              />
            </div>

            <div className="form-group">
              <label>Price:</label>
              <input
                type="number"
                step="0.01"
                value={formData.price}
                onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                required
              />
            </div>

            <div className="form-group">
              <label>Image URL:</label>
              <input
                type="text"
                value={formData.imageUrl}
                onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
              />
            </div>

            <div className="form-group">
              <label>Sizes (comma-separated):</label>
              <input
                type="text"
                value={formData.sizesCsv}
                onChange={(e) => setFormData({ ...formData, sizesCsv: e.target.value })}
                placeholder="e.g., 7,8,9,10,11"
                required
              />
            </div>

            <div className="form-group">
              <label>Stock Quantity:</label>
              <input
                type="number"
                value={formData.stockQty}
                onChange={(e) => setFormData({ ...formData, stockQty: e.target.value })}
                required
              />
            </div>

            <div className="form-actions">
              <button type="submit" className="btn btn-primary">
                {editingProduct ? 'Update' : 'Create'}
              </button>
              <button type="button" onClick={resetForm} className="btn">
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="products-table">
        <table>
          <thead>
            <tr>
              <th>Image</th>
              <th>Name</th>
              <th>Brand</th>
              <th>Price</th>
              <th>Stock</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr key={product.id}>
                <td>
                  <img src={product.imageUrl} alt={product.name} width="50" />
                </td>
                <td>{product.name}</td>
                <td>{product.brand}</td>
                <td>${product.price.toFixed(2)}</td>
                <td>{product.stockQty}</td>
                <td>
                  <button onClick={() => handleEdit(product)} className="btn btn-sm">
                    Edit
                  </button>
                  <button
                    onClick={() => handleDelete(product.id)}
                    className="btn btn-sm btn-danger"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
