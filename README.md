# Employee Directory

Small Spring Boot application for managing employees. It uses Thymeleaf for the pages, Spring Data JPA for persistence, and MySQL for the database.

The app currently supports:

- listing employees by last name
- adding and editing employees
- deleting employees after confirmation
- basic validation for names and email addresses

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

Set the database credentials in a `.env` file in the project directory:

```dotenv
DATABASE_USERNAME=your_mysql_username
DATABASE_PASSWORD=your_mysql_password
```

The credentials are read by `application.properties` and should not be committed.

Start the application with:

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run` instead.

Once it has started, visit <http://localhost:8080/employees/list>.

## Tests

Run the tests with:

```bash
./mvnw test
```

The validation tests do not need a database. The Spring context test uses the configured MySQL connection.

## Project layout

The Java code is split into a controller, service, repository, and entity. The Thymeleaf templates are under `src/main/resources/templates/employees`, and the database setup script is in `sql-scripts`.
