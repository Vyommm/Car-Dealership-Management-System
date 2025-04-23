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

