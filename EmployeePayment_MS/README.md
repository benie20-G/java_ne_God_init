# Employee Payment Management System

This is a Spring Boot application for managing employee payments, including payroll generation and approval.

## Features

- Employee Management
- Employment Management
- Deduction Management
- Payroll Generation
- Payslip Approval
- Messaging System for Payroll Notifications

## Technologies Used

- Spring Boot 3.5.0
- Spring Security with JWT Authentication
- Spring Data JPA
- MySQL Database
- Swagger for API Documentation
- Spring Mail for Email Notifications

## Setup

### Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Database Configuration

The application uses MySQL as the database. You need to create a database named `payrolldb` or update the `application.properties` file with your database configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/payrolldb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
```

### Email Configuration

To enable email notifications, update the `application.properties` file with your email configuration:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run `mvn clean install` to build the project
4. Run `mvn spring-boot:run` to start the application
5. Access the Swagger UI at `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Authentication

- `POST /api/auth/login` - Login with email and password
- `POST /api/auth/register` - Register a new employee

### Employee Management

- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get employee by ID
- `GET /api/employees/code/{code}` - Get employee by code
- `POST /api/employees` - Create a new employee
- `PUT /api/employees/{id}` - Update an employee
- `DELETE /api/employees/{id}` - Delete an employee (sets status to DISABLED)

### Employment Management

- `GET /api/employments` - Get all employments
- `GET /api/employments/active` - Get active employments
- `GET /api/employments/employee/{employeeId}` - Get employments by employee
- `GET /api/employments/{id}` - Get employment by ID
- `GET /api/employments/code/{code}` - Get employment by code
- `POST /api/employments` - Create a new employment
- `PUT /api/employments/{id}` - Update an employment
- `DELETE /api/employments/{id}` - Deactivate an employment

### Deduction Management

- `GET /api/deductions` - Get all deductions
- `GET /api/deductions/{id}` - Get deduction by ID
- `GET /api/deductions/code/{code}` - Get deduction by code
- `GET /api/deductions/name/{name}` - Get deduction by name
- `POST /api/deductions` - Create a new deduction
- `PUT /api/deductions/{id}` - Update a deduction
- `DELETE /api/deductions/{id}` - Delete a deduction
- `POST /api/deductions/initialize` - Initialize default deductions

### Payslip Management

- `GET /api/payslips` - Get all payslips
- `GET /api/payslips/{id}` - Get payslip by ID
- `GET /api/payslips/employee/{employeeId}` - Get payslips by employee
- `GET /api/payslips/month-year` - Get payslips by month and year
- `GET /api/payslips/employee/{employeeId}/month-year` - Get payslips by employee, month and year
- `POST /api/payslips/generate` - Generate payroll for a specific month and year
- `PUT /api/payslips/approve/{id}` - Approve a specific payslip
- `PUT /api/payslips/approve/all` - Approve all pending payslips for a specific month and year

### Message Management

- `GET /api/messages` - Get all messages
- `GET /api/messages/employee/{employeeId}` - Get messages by employee
- `GET /api/messages/month-year` - Get messages by month and year
- `GET /api/messages/unsent` - Get unsent messages
- `POST /api/messages/resend` - Resend unsent messages

## Default Users

The application creates the following default users on startup:

1. Admin User
   - Email: admin@example.com
   - Password: password
   - Role: ROLE_ADMIN

2. Manager User
   - Email: manager@example.com
   - Password: password
   - Role: ROLE_MANAGER

3. Employee User
   - Email: employee@example.com
   - Password: password
   - Role: ROLE_EMPLOYEE

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.