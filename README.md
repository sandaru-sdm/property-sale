
# 🏡 Property Sale

Property Sale is a Spring Boot application for listing, managing, and selling properties. It provides RESTful APIs for user authentication, property management, file uploads, and user administration. The backend is built with Java 17, Spring Boot, Spring Security, JPA, JWT, and MySQL.

---

## ✨ Features

- 👤 User registration and authentication (JWT-based)
- 🔐 Role-based access control (Admin/User)
- 🏠 Add, update, and view properties
- 🖼️ Upload property images and plans
- 🔄 Change user and property status
- 🗄️ MySQL database integration
- 🌐 RESTful API endpoints

## 🛠️ Tech Stack

- ☕ Java 17
- 🧰 Spring Boot 3.4.5
- 🛡️ Spring Security
- 🗃️ Spring Data JPA
- 🗄️ MySQL
- 🔑 JWT (JSON Web Token)
- 📦 Maven

## 🚀 Getting Started

### 📋 Prerequisites

- ☕ Java 17+
- 📦 Maven
- 🗄️ MySQL

### ⚙️ Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/sandaru-sdm/property-sale.git
   cd property-sale
   ```
2. **Configure the database:**
   - Create a MySQL database.
   - Set the following environment variables or update `src/main/resources/application.properties`:
     - `DATASOURCE_URL` (e.g., `jdbc:mysql://localhost:3306/property_sale`)
     - `DATASOURCE_USER`
     - `DATASOURCE_PASSWORD`
     - `JWT_SECRET_KEY`
     - `FRONTEND_URL`, `BASE_URL` (optional)
3. **Build and run the application:**
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```
4. **Access the API:**
   - The server runs on `http://localhost:8080` by default.

## 📚 API Endpoints

### 🔑 Authentication

#### Login
```http
POST /api/v1/auth/authenticate
Content-Type: application/json
{
  "email": "<user_email>",
  "password": "<user_password>"
}
```

### 👥 User Management

#### Register User
```http
POST /api/v1/register
Content-Type: application/json
{
  "name": "John Doe",
  "mobile": "0754306610",
  "email": "johndoe@email.com",
  "password": "12345",
  "userRole": "USER"
}
```

#### Get All Users
```http
GET /api/v1/users
Authorization: Bearer <JWT_TOKEN>
```

#### Update User
```http
PUT /api/v1/update/{userId}
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
{
  "name": "John Doe 1",
  "mobile": "0754306610",
  "email": "johndoe@email.com",
  "userRole": "ADMIN"
}
```

#### Change User Status
```http
PATCH /api/v1/status/{userId}
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
{
  "activated": true
}
```

### 🏠 Property Management

#### Get Property by ID
```http
GET /api/v1/property/{propertyId}
```

#### Add Property
```http
POST /api/v1/property/save
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
{
  ...property fields...
}
```

### 📤 File Upload

#### Upload Property Images
```http
POST /api/v1/upload/property-images
Content-Type: multipart/form-data
landImages: [files]
planImages: [files]
```

## ⚙️ Configuration

See `src/main/resources/application.properties` for all configuration options, including database, JWT, and file upload settings.

## 📄 License

This project is licensed under the MIT License.

---
