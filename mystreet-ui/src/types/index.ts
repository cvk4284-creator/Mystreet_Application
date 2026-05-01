export interface User {
  userId: string;
  email: string;
  isAdmin: boolean;
  token: string;
}

export interface Product {
  id: string;
  name: string;
  brand: string;
  description: string;
  price: number;
  imageUrl: string;
  sizesCsv: string;
  stockQty: number;
  createdAt: string;
}

export interface CartItem {
  productId: string;
  name: string;
  brand: string;
  price: number;
  imageUrl: string;
  size: string;
  quantity: number;
}

export interface OrderItem {
  productId: string;
  size: string;
  quantity: number;
}

export interface Order {
  id: string;
  userId: string;
  items: {
    id: string;
    productId: string;
    productName: string;
    size: string;
    quantity: number;
    priceAtOrder: number;
  }[];
  totalAmount: number;
  status: string;
  shippingAddress: string;
  paymentMode: string;
  createdAt: string;
}
