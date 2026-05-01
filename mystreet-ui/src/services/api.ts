import axios from 'axios';
import type { Product, Order } from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add token
api.interceptors.request.use(
  (config) => {
    const user = localStorage.getItem('user');
    if (user) {
      const { token } = JSON.parse(user);
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Auth API
export const authAPI = {
  register: async (email: string, password: string, firstName?: string, lastName?: string) => {
    const response = await api.post('/auth/register', { email, password, firstName, lastName });
    return response.data;
  },

  login: async (email: string, password: string) => {
    const response = await api.post('/auth/login', { email, password });
    return response.data;
  },

  logout: async () => {
    await api.post('/auth/logout');
  },
};

// Product API
export const productAPI = {
  getAll: async (brand?: string, size?: string): Promise<Product[]> => {
    const params = new URLSearchParams();
    if (brand) params.append('brand', brand);
    if (size) params.append('size', size);
    const response = await api.get(`/products?${params.toString()}`);
    return response.data;
  },

  getById: async (id: string): Promise<Product> => {
    const response = await api.get(`/products/${id}`);
    return response.data;
  },

  create: async (product: Omit<Product, 'id' | 'createdAt'>) => {
    const response = await api.post('/products', product);
    return response.data;
  },

  update: async (id: string, product: Omit<Product, 'id' | 'createdAt'>) => {
    const response = await api.put(`/products/${id}`, product);
    return response.data;
  },

  delete: async (id: string) => {
    await api.delete(`/products/${id}`);
  },
};

// Order API
export const orderAPI = {
  create: async (orderData: {
    items: { productId: string; size: string; quantity: number }[];
    shippingAddress: string;
    paymentMode: string;
  }): Promise<Order> => {
    const response = await api.post('/orders', orderData);
    return response.data;
  },

  getMine: async (): Promise<Order[]> => {
    const response = await api.get('/orders/mine');
    return response.data;
  },

  getById: async (id: string): Promise<Order> => {
    const response = await api.get(`/orders/${id}`);
    return response.data;
  },
};

export default api;
