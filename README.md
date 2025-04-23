# 🚗 Car Dealership Management System

A Spring Boot-based backend application for managing car dealership operations, including customer management, car inventory, and purchase tracking.

## 📌 Features

- Customer registration, update, and deletion
- Car inventory listing, searching, and filtering
- Fetch customers by email, phone, or name
- Track customers with purchases
- Identify top customers by purchase count
- Find customers registered after a specific date
- Robust exception handling and input validation

## 🧱 Tech Stack

- **Backend**: Java, Spring Boot
- **Data Access**: Spring Data JPA (with `JpaRepository`)
- **Database**: H2 / MySQL / PostgreSQL (configurable)
- **Framework Annotations**: `@Service`, `@Repository`, `@Transactional`
- **Custom Queries**: `@Query` for JPQL/SQL operations
- **Exception Handling**: Custom exceptions like `CustomerNotFoundException`

## 📂 Project Structure

src/
├── main/
│   ├── java/com/cardealership/managementsystem/
│   │   ├── controller/       # (Optional) REST controllers
│   │   ├── service/          # Business logic layer
│   │   ├── repository/       # JPA repositories
│   │   ├── model/            # Entity classes
│   │   └── exception/        # Custom exception classes
│   └── resources/
│       ├── application.properties
🔧 How to Run

Clone the Repository

git clone https://github.com/your-username/car-dealership-management.git
cd car-dealership-management

Configure the Database Update application.properties with your DB credentials or use the default in-memory H2 database.
Run the Application You can run the project from an IDE or use the command line:
./mvnw spring-boot:run
Access API
Swagger UI (if added): http://localhost:8080/swagger-ui/
REST endpoints available via Postman or your frontend.
🧠 Architecture & Design

Architecture Pattern: Model-View-Controller (MVC)
Model: Entity classes like Customer, Car
Controller: (Optional) Handles HTTP requests
Service: Business logic
Repository: Data access layer
Design Principles
Separation of Concerns
Single Responsibility Principle
Open/Closed Principle
Dependency Injection
Fail Fast Principle
Design Patterns
Repository Pattern
Service Layer Pattern
Factory (via Spring DI)
Exception Handling Pattern
🛠️ Sample Endpoints

GET /customers - Get all customers
POST /customers - Register a new customer
GET /customers/{id} - Get customer by ID
GET /customers/search?firstName=John - Search customers
GET /customers/top - Top customers by purchase count
