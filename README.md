# ATM Machine Application

This project is an ATM simulation application built using Spring Boot for the backend. The project simulates bank account operations, such as creating accounts, depositing and withdrawing money, and tracking transaction history.

## Table of Contents
- [Architecture](#architecture)
- [Principles](#principles)
- [Routes](#routes)
- [Transaction Routes](#transaction-routes)
- [Testing](#testing)
- [Setup](#setup)

## Architecture

The project follows a service-oriented architecture and employs SOLID principles for separation of concerns and maintainability. The application is structured as follows:

- **Controller Layer:** Handles HTTP requests and responses.
- **Service Layer:** Contains business logic.
- **Repository Layer:** Handles database interaction.
- **Entity Layer:** Defines the data model.

### Key Components
- **Spring Boot:** Main framework for building the REST API.
- **JPA (Java Persistence API):** Used for database interactions.
- **Mockito:** For unit testing.
- **JUnit:** Test framework for writing and running tests.

## Principles

This project adheres to several best practices and design principles:

- **SOLID Principles:** For maintainability and scalability.
- **Layered Architecture:** Controller, Service, and Repository layers.
- **DTO (Data Transfer Objects):** To manage data between different layers.
- **DRY (Don't Repeat Yourself):** To avoid redundancy in code.

## Routes

### User Routes

| Method | Endpoint           | Description             | Request Body                       | Response Status |
|--------|--------------------|-------------------------|-------------------------------------|-----------------|
| GET    | `/api/users/{id}`   | Get user by ID          | N/A                                 | `200 OK`        |
| POST   | `/api/users`        | Create a new user       | `{ "name": "John Doe", "email": "john@example.com" }` | `201 Created`  |
| PUT    | `/api/users/{id}`   | Update user by ID       | `{ "name": "Jane Doe" }`            | `200 OK`        |
| DELETE | `/api/users/{id}`   | Delete user by ID       | N/A                                 | `204 No Content`|

### Account Routes

| Method | Endpoint                     | Description                           | Request Body                      | Response Status |
|--------|------------------------------|---------------------------------------|------------------------------------|-----------------|
| GET    | `/api/accounts`              | Get all accounts                      | N/A                                | `200 OK`        |
| GET    | `/api/accounts/{id}`          | Get account by ID                     | N/A                                | `200 OK`        |
| POST   | `/api/accounts`               | Create a new account                  | `{ "userId": 1, "initialDeposit": 1000 }` | `201 Created` |
| PUT    | `/api/accounts/{id}/balance`  | Update account balance                | `{ "balance": 1500 }`              | `200 OK`        |

### Transaction Routes

| Method | Endpoint                     | Description                           | Request Body                      | Response Status |
|--------|------------------------------|---------------------------------------|------------------------------------|-----------------|
| POST   | `/api/transactions/deposit`   | Deposits money into an account        | `{ "accountId": 1, "amount": 500 }`| `200 OK`        |
| POST   | `/api/transactions/withdraw`  | Withdraws money from an account       | `{ "accountId": 1, "amount": 200 }`| `200 OK`        |
| GET    | `/api/transactions/{id}`      | Retrieves transaction by ID           | N/A                                | `200 OK`        |
| GET    | `/api/transactions/account/{accountId}` | Retrieves transactions for an account | N/A                                | `200 OK`        |

## Testing

Unit tests have been written using JUnit and Mockito for all service classes to ensure the functionality of business logic.

### Example Test Cases
- `testGetUserById_UserExists()`
- `testUpdateBalance_AccountExists()`
- `testCreateAccount_UserExists()`

## Setup

1. Clone the repository.
2. Run `mvn clean install` to install dependencies.
3. Use `mvn spring-boot:run` to start the application.
4. The application will be running on `http://localhost:8089/`.
5. The application git repository `https://github.com/DENMOUNS/ATM-MACHINE.git`.
