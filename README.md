# Employee Directory

Small Spring Boot application for managing employees. It uses Thymeleaf for the pages, Spring Data JPA for persistence, and MySQL for the database.

The app currently supports:

- searching by first name, last name, or email and browsing results by last name
- adding and editing employees
- deleting employees after confirmation
- validating required fields, email format, and database field lengths
- signing in with a configured administrator account

## Requirements

- Java 25
- MySQL
- Git

## Running it

Clone the repository and open the project directory:

```bash
git clone https://github.com/MohanKalia/Employee-Controller-MVC-Thymleaf.git
cd Employee-Controller-MVC-Thymleaf
```

Create the database and sample data with:

```bash
mysql -u YOUR_MYSQL_USER -p < sql-scripts/employee-directory.sql
```

The script recreates the `employee` table, so running it again removes any employee records that were added through the app.

Set the database and directory credentials in a `.env` file in the project directory:

```dotenv
DATABASE_USERNAME=your_mysql_username
DATABASE_PASSWORD=your_mysql_password
DIRECTORY_ADMIN_USERNAME=admin
DIRECTORY_ADMIN_PASSWORD=choose_a_strong_password
```

Use plain `KEY=value` lines without shell quotes. You can also set these values as environment variables. The administrator password is required, and the username defaults to `admin`. Keep `.env` out of version control. Use HTTPS if you run the directory outside localhost.

Start the application with:

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run` instead.

Once it has started, visit <http://localhost:8080/employees/list> and sign in with the directory credentials.

## Tests

Run the tests with:

```bash
./mvnw test
```

The integration tests use an in-memory H2 database and isolated test credentials. They exercise login, authorization, CSRF protection, validation, CRUD, and search through Spring MVC. MySQL is only needed to run the application.

## Project layout

The Java code is split into a controller, service, repository, and entity. The Thymeleaf templates are under `src/main/resources/templates/employees`, and the database setup script is in `sql-scripts`.
