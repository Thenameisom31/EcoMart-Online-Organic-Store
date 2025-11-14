# EcoMart - Online Organic Product Store

A full-stack e-commerce application for organic products built with Spring Boot and Thymeleaf.

## Features

- **User Management**: Registration, login, and profile management
- **Product Management**: Browse products by category, view product details
- **Shopping Cart**: Add products to cart, update quantities, clear cart
- **Order Management**: Place orders, view order history, cancel orders
- **Admin Dashboard**: Manage products, categories, orders, and users
- **File Upload**: Product image upload and management
- **Responsive Design**: Modern UI with mobile-friendly design

## Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Database**: MySQL 8
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## Setup Instructions

### 1. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE Eco_Mart;
```

### 2. Configuration

**For Remote MySQL Database (Recommended):**

Set environment variables to connect to your remote MySQL database:

```bash
# Linux/Mac
export DATABASE_URL="jdbc:mysql://your-remote-host:3306/Eco_Mart"
export DATABASE_USERNAME="your_username"
export DATABASE_PASSWORD="your_password"

# Windows (PowerShell)
$env:DATABASE_URL="jdbc:mysql://your-remote-host:3306/Eco_Mart"
$env:DATABASE_USERNAME="your_username"
$env:DATABASE_PASSWORD="your_password"
```

**For Local MySQL Database:**

```bash
export DATABASE_URL="jdbc:mysql://localhost:3306/Eco_Mart"
export DATABASE_USERNAME="root"
export DATABASE_PASSWORD="your_password"
```

> **Note**: The application is configured to work with **any MySQL database** (local or remote). Just set the environment variables before running.

### 3. Build and Run

```bash
# Navigate to project directory
cd EcoMart-Online-Organic-Product-Store

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at `http://localhost:9876`

### 4. Default Admin Account

- Email: `ps@gmail.com`
- Password: (set during first run or check DataInitializer)

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST/Web controllers
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # Data repositories
│   │   └── service/         # Business logic
│   └── resources/
│       ├── static/          # CSS, JS, images
│       ├── templates/        # Thymeleaf templates
│       └── application.properties
└── test/                    # Test files
```

## Deployment

### Production Configuration

1. **Set Environment Variables**:

```bash
export SERVER_PORT=8080
export DATABASE_URL=jdbc:mysql://your-db-host:3306/Eco_Mart
export DATABASE_USERNAME=your_prod_username
export DATABASE_PASSWORD=your_prod_password
export HIBERNATE_DDL_AUTO=validate
export SHOW_SQL=false
export FORMAT_SQL=false
export LOG_LEVEL=INFO
```

2. **Build JAR**:

```bash
mvn clean package -DskipTests
```

3. **Run JAR**:

```bash
java -jar target/EcoMart-Online-Organic-Product-Store-0.0.1-SNAPSHOT.jar
```

### Docker Deployment (Optional)

Create a `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9876
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t ecomart .
docker run -p 9876:9876 -e DATABASE_URL=... ecomart
```

## API Endpoints

### User Endpoints
- `GET /index` - Landing page
- `GET /register` - Registration page
- `POST /inserted` - Register user
- `GET /login` - Login page
- `POST /done` - Login user
- `GET /logout` - Logout

### User Dashboard
- `GET /user/dashboard` - User dashboard
- `GET /user/products` - View all products
- `GET /user/cart` - View cart
- `POST /user/cart/add/{productId}` - Add to cart
- `GET /user/checkout` - Checkout page
- `POST /user/checkout/place-order` - Place order
- `GET /user/orders` - View orders
- `POST /user/orders/cancel/{id}` - Cancel order

### Admin Endpoints
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/products` - Manage products
- `GET /admin/orders` - Manage orders
- `GET /admin/users` - Manage users

## File Uploads

Product images are stored in the `uploads/` directory. Make sure this directory exists and has write permissions.

## Security Notes

- Change default admin email in production
- Use environment variables for sensitive data
- Enable HTTPS in production
- Consider adding Spring Security for enhanced security
- Use password hashing (BCrypt) instead of plain text passwords

## Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check database credentials
- Ensure database exists

### Port Already in Use
- Change `server.port` in `application.properties`
- Or set `SERVER_PORT` environment variable

### File Upload Issues
- Ensure `uploads/` directory exists
- Check file permissions
- Verify file size limits in `application.properties`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please open an issue on the repository.

