# MyStreeT - Sneaker E-Commerce Platform

A full-stack sneaker e-commerce application built with Spring Boot and React, featuring user authentication, product catalog, shopping cart, and order management.

## 🚀 Features

### User Features
- Browse sneaker products with filtering by brand and size
- View detailed product information
- Add products to shopping cart
- User authentication (Register/Login)
- Checkout with mock payment options (COD/UPI)
- View order history
- Cart persistence in localStorage

### Admin Features
- Product management (Create, Read, Update, Delete)
- Admin-only access to product management

### Technical Highlights
- JWT-based authentication
- RESTful API design
- Responsive UI with modern CSS
- Error handling with consistent response format
- Database seeding with sample data
- Password hashing with BCrypt

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.2.5
- **Language:** Java 17
- **Database:** H2 (dev), PostgreSQL (prod)
- **Security:** Spring Security + JWT
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven

### Frontend
- **Framework:** React 19.2.4 with TypeScript
- **Build Tool:** Vite
- **Routing:** React Router DOM
- **HTTP Client:** Axios
- **State Management:** React Context API
- **Styling:** CSS3

## 📋 Prerequisites

- Java 17 or higher
- Node.js 16+ and npm
- Maven 3.6+

## 🔧 Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd mystreet-capstone
```

### 2. Backend Setup

```bash
cd mystreet-backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

**Access H2 Console (Dev Mode):**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:mystreetdb`
- Username: `sa`
- Password: (leave empty)

### 3. Frontend Setup

```bash
cd mystreet-ui

# Install dependencies
npm install

# Run development server
npm run dev
```

The frontend will start on `http://localhost:5173`

## 📊 Database Schema

### Tables

**users**
- id (UUID, Primary Key)
- email (Unique, Not Null)
- password_hash (Not Null)
- is_admin (Boolean, Default: false)
- first_name, last_name
- created_at, updated_at

**products**
- id (UUID, Primary Key)
- name, brand, description
- price (Decimal)
- image_url
- sizes_csv (e.g., "7,8,9,10,11")
- stock_qty
- created_at, updated_at

**orders**
- id (UUID, Primary Key)
- user_id (Foreign Key → users)
- total_amount
- status (PLACED, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- shipping_address
- payment_mode (COD, MOCK_UPI)
- created_at

**order_items**
- id (UUID, Primary Key)
- order_id (Foreign Key → orders)
- product_id (Foreign Key → products)
- size, quantity
- price_at_order

## 🔐 Demo Credentials

### Admin Account
- **Email:** admin@mystreet.com
- **Password:** admin123

### Regular User Account
- **Email:** user@mystreet.com
- **Password:** user123

## 🌐 API Endpoints

### Authentication
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login user | No |
| POST | `/api/auth/logout` | Logout user | No |

### Products
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/products` | Get all products (filter by brand/size) | No |
| GET | `/api/products/{id}` | Get product by ID | No |
| POST | `/api/products` | Create product | Admin |
| PUT | `/api/products/{id}` | Update product | Admin |
| DELETE | `/api/products/{id}` | Delete product | Admin |

### Orders
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/orders` | Create order | Yes |
| GET | `/api/orders/mine` | Get user's orders | Yes |
| GET | `/api/orders/{id}` | Get order by ID | Yes |

### Example API Request/Response

**Register User:**
```json
POST /api/auth/register
{
  "email": "test@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}

Response (201 Created):
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "email": "test@example.com",
  "isAdmin": false
}
```

**Create Order:**
```json
POST /api/orders
Headers: { "Authorization": "Bearer <token>" }
{
  "items": [
    {
      "productId": "123e4567-e89b-12d3-a456-426614174000",
      "size": "9",
      "quantity": 1
    }
  ],
  "shippingAddress": "123 Main St, City, State 12345",
  "paymentMode": "COD"
}
```

## 🧪 Running Tests

### Backend Tests

```bash
cd mystreet-backend
mvn test
```

Tests include:
- Service layer unit tests
- Repository tests
- Controller integration tests

**Note:** Test coverage target is ≥60%

## 📁 Project Structure

```
mystreet-capstone/
├── mystreet-backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/mystreet/
│   │   │   │   ├── config/          # Security & CORS config
│   │   │   │   ├── controller/      # REST controllers
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── exception/       # Custom exceptions & handler
│   │   │   │   ├── model/           # JPA entities
│   │   │   │   ├── repository/      # Spring Data JPA repos
│   │   │   │   ├── security/        # JWT util & filters
│   │   │   │   └── service/         # Business logic
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── application-dev.properties
│   │   │       ├── application-prod.properties
│   │   │       └── data.sql         # Seed data
│   │   └── test/
│   └── pom.xml
│
├── mystreet-ui/
│   ├── src/
│   │   ├── components/       # Reusable UI components
│   │   ├── contexts/         # React Context (Auth, Cart)
│   │   ├── pages/            # Page components
│   │   ├── services/         # API service layer
│   │   ├── types/            # TypeScript interfaces
│   │   ├── App.tsx           # Main app component
│   │   ├── App.css           # Global styles
│   │   └── main.tsx          # Entry point
│   ├── package.json
│   └── vite.config.ts
│
└── README.md
```

## 🎨 User Flow

1. **Browse Products:** User lands on homepage and sees product grid
2. **Filter:** Apply brand/size filters to narrow down choices
3. **View Details:** Click product to see full details
4. **Add to Cart:** Select size, quantity, and add to cart
5. **Cart Management:** View cart, update quantities, or remove items
6. **Checkout:** If not logged in, redirect to login/register
7. **Complete Order:** Enter shipping address, select payment mode
8. **Confirmation:** View order confirmation with order ID
9. **Order History:** Access "My Orders" to view past purchases

## 🔒 Security Features

- Passwords hashed using BCrypt
- JWT tokens for stateless authentication
- Token stored in localStorage (client-side)
- Protected routes (checkout, orders, admin)
- CORS configured for frontend origin
- Input validation on both frontend and backend
- SQL injection prevention via JPA

## 🚧 Known Limitations

- Mock payment system (no real payment integration)
- No email notifications
- No password reset functionality
- Basic admin flag (no complex RBAC)
- Cart stored in localStorage (not persisted on backend)
- No pagination for product list
- No advanced search functionality

## 📈 Future Enhancements

- Real payment gateway integration
- Email notifications for orders
- Product reviews and ratings
- Wishlist functionality
- Order tracking with status updates
- Advanced filtering and search
- Product image upload
- Inventory alerts
- Sales analytics dashboard for admin
- Backend cart persistence
- OAuth2 social login

## 🤝 Contributing

This is a capstone project for learning purposes. Feedback and suggestions are welcome!

## 📄 License

This project is for educational purposes.

## 👨‍💻 Author

Built as part of the MyStreeT Capstone Project

## 🐛 Troubleshooting

### Backend Issues

**Port 8080 already in use:**
```bash
# Change port in application.properties
server.port=8081
```

**Database connection issues:**
- Verify H2 is being used in dev profile
- Check application-dev.properties settings

### Frontend Issues

**API connection refused:**
- Ensure backend is running on port 8080
- Check CORS settings in SecurityConfig.java

**Build errors:**
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

## 📞 Support

For issues or questions, please create an issue in the repository.

---

**Built with ❤️ using Spring Boot & React**
