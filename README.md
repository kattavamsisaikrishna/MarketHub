# MarketHub – Full Stack Marketplace Application
A complete end-to-end **Marketplace Web Application** built using:

**Java · Spring Boot · MySQL · JWT Authentication · HTML · CSS · JavaScript**

This project supports two user roles:

- **Seller** – Can add, view, update, delete, and manage products.
- **Customer** – Can browse products, search, add quantities, and place orders.

Frontend is built with **vanilla HTML/CSS/JS**, and backend is built using **Spring Boot REST APIs secured with JWT**.

---

## 🚀 Features

### 🔐 Authentication
- Register as **Customer** or **Seller**
- Login using JWT
- Role-based access control
- Frontend stores JWT token securely in `localStorage`

### 🛒 Customer Features
- View all products
- Search products by keyword
- Select quantities and place orders
- View order history

### 🏪 Seller Features
- Add new products
- List all products
- Search products
- Update & delete products

### 🎨 Frontend (HTML + CSS + JS)
- Clean & modern responsive UI
- Login page, Register page, Seller dashboard, Customer product page
- API interactions using Fetch API & JWT Authorization headers

---

## 🏗️ Tech Stack

### Backend
- Java 17+
- Spring Boot 3+
- Spring Security (JWT authentication)
- Spring Data JPA (Hibernate)
- MySQL Database

### Frontend
- HTML5
- CSS3 (custom stylesheet)
- Vanilla JavaScript (no frameworks)

---

## 🗂️ Project Structure

```
src/main/java/com/vamsi/markethub/
│
├── auth/          # Login / Register / JWT
├── config/        # SecurityConfig & Cors
├── security/      # JWT Filter + Utility
├── product/       # Product CRUD (Seller + Customer)
├── order/         # Orders & Order Items
└── user/          # User entity + Repository
```

Frontend pages:

```
src/main/resources/static/
│
├── index.html
├── login.html
├── register.html
├── seller-dashboard.html
├── customer-products.html
│
├── css/styles.css
└── js/
     ├── common.js
     ├── login.js
     ├── register.js
     ├── seller-dashboard.js
     └── customer-products.js
```

---

## 🛠️ Setup Instructions

### 1️⃣ Clone the repository
```bash
git clone https://github.com/kattavamsisaikrishna/MarketHub.git
```

### 2️⃣ Configure MySQL in `application.properties`
```
spring.datasource.url=jdbc:mysql://localhost:3306/markethub
spring.datasource.username=<your-mysql-username>
spring.datasource.password=<your-mysql-password>

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=<some_secret_key>
jwt.expiration=900000
```

### 3️⃣ Run the project
```bash
./mvnw spring-boot:run
```

Open frontend pages:

```
http://localhost:8080/login.html
http://localhost:8080/register.html
http://localhost:8080/seller-dashboard.html
http://localhost:8080/customer-products.html
```

---

## 📦 API Endpoints (Summary)

### Auth
```
POST /api/auth/register
POST /api/auth/login
```

### Customer
```
GET  /api/products
GET  /api/products/search?keyword=
POST /api/orders
GET  /api/orders/my
```

### Seller
```
POST   /api/seller/products
GET    /api/seller/products
PUT    /api/seller/products/{id}
DELETE /api/seller/products/{id}
```

# ⭐ If you like this project, give it a star on GitHub!
