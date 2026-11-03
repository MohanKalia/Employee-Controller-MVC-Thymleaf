# Employee Directory

A Java-based employee management web application built with **Spring Boot**, demonstrating layered MVC architecture, server-side rendering with Thymeleaf, and database persistence using Spring Data JPA and MySQL.

## Features

- View employees, sorted alphabetically by last name.
- Add new employees.
- Update existing employee information.
- Delete employees with confirmation.
- Persist employee records in a MySQL database.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 25 | Programming language |
| Spring Boot 4 | Application framework |
| Spring MVC | Web request handling |
| Thymeleaf | Server-side HTML rendering |
| Spring Data JPA | Data access and persistence |
| Hibernate | ORM |
| MySQL | Relational database |
| Maven | Build and dependency management |

## Architecture

The application follows a layered architecture:

- **Controller:** Handles HTTP requests and coordinates application flow.
- **Service:** Encapsulates employee-related business operations.
- **Repository:** Provides database access through Spring Data JPA.
- **Entity:** Maps the Employee model to the database.

## Getting Started

### Prerequisites

- Java 25
- MySQL Server
- Git

### 1. Clone the repository

```bash
git clone https://github.com/MohanKalia/Employee-Controller-MVC-Thymleaf.git
cd Employee-Controller-MVC-Thymleaf
```

### 2. Set up the database

Execute the provided SQL script:

```bash
mysql -u YOUR_MYSQL_USER -p < sql-scripts/employee-directory.sql
```

This creates the `employee_directory` database and inserts sample employee records.

**Note:** The SQL script drops and recreates the `employee` table. Running it again will delete existing employee data.

### 3. Configure environment variables

Create a `.env` file in the project root:

```dotenv
DATABASE_USERNAME=your_mysql_username
DATABASE_PASSWORD=your_mysql_password
```

Keep your credentials private. The `.env` file is excluded from version control.

### 4. Run the application

**Linux / macOS**

```bash
./mvnw spring-boot:run
```

**Windows**

```bat
mvnw.cmd spring-boot:run
```

Open the application:

`http://localhost:8080/employees/list`

## Testing

Run the test suite:

```bash
./mvnw test
```

The application test context uses the configured database, so ensure MySQL and the required environment variables are available.

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── .../
│   │       ├── controller/
│   │       ├── service/
│   │       ├── dao/
│   │       └── entity/
│   └── resources/
│       └── templates/
│           └── employees/
└── test/
sql-scripts/
└── employee-directory.sql
```

## Learning Objectives

This project explores core Java backend development concepts, including dependency injection, MVC request handling, service-layer design, ORM-based persistence, database integration, and server-side rendering.
