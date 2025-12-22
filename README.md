# Library Management System

A comprehensive Java Web-Based Library Management System built using Java Servlets, JSP, JDBC, and MySQL.

## Features

### Librarian Functionalities

- **Book Management**: Add, edit, delete, and view books in the catalog
- **Member Management**: Create, update, and delete member accounts
- **Transaction Management**: Manage borrowing and returning of books
- **Notification Management**: Send notifications to members about due dates and new arrivals
- **Inventory Reports**: Generate reports on book inventory status and borrowing trends

### Member Functionalities

- **Book Search**: Search books by title, author, genre, or ISBN
- **Borrow/Return Books**: Borrow and return books from the library
- **Borrowing History**: View complete borrowing history and active borrows
- **Profile Management**: Update personal profile details
- **Notification Alerts**: View and manage notifications

## Technologies Used

- **Java 21**: Core programming language
- **Java Servlets**: Web application framework
- **JSP (JavaServer Pages)**: View layer
- **JDBC**: Database connectivity
- **MySQL**: Database management system
- **Maven**: Build automation and dependency management
- **Apache DBCP2**: Connection pooling
- **JSTL**: JSP Standard Tag Library

## Project Structure

```
javaprojectsem3
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/library/
│   │   │       ├── model/          # Model classes (Book, Member, Transaction, Notification)
│   │   │       ├── dao/            # DAO interfaces
│   │   │       │   └── impl/       # JDBC implementations
│   │   │       ├── service/        # Service layer (business logic)
│   │   │       ├── servlet/        # Servlet controllers
│   │   │       ├── util/           # Utility classes (DatabaseConnection)
│   │   │       └── exception/      # Custom exceptions
│   │   ├── resources/
│   │   │   ├── database.properties # Database configuration
│   │   │   └── database.sql        # Database schema
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml         # Web application configuration
│   │       ├── css/
│   │       │   └── style.css       # Stylesheet
│   │       ├── librarian/          # Librarian JSP pages
│   │       ├── member/             # Member JSP pages
│   │       ├── index.jsp           # Home page
│   │       └── login.jsp           # Login page
│   └── test/                       # Test classes (optional)
├── pom.xml                         # Maven configuration
└── README.md                       # Project documentation
```

## Prerequisites

- **Java JDK 21**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Apache Tomcat 9.0+** or similar servlet container
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code)

## Database Setup

1. **Create MySQL Database**:

   ```sql
   CREATE DATABASE librarydb;
   ```

2. **Run SQL Script**:

   - Open MySQL command line or MySQL Workbench
   - Execute the SQL script from `src/main/resources/database.sql`

   ```bash
   mysql -u root -p librarydb < src/main/resources/database.sql
   ```

3. **Configure Database Connection**:

   - Edit `src/main/resources/database.properties`
   - Update database credentials:

     ```properties
     db.username=root
     db.password=your_password
     ```

## Build and Deployment

### Using Maven

1. **Build the project**:

   ```bash
   mvn clean package
   ```

2. **Deploy WAR file**:
   - Copy `target/LibraryManagementSystem.war` to Tomcat's `webapps` directory
   - Start Tomcat server

### Using IDE

1. **Import Project**:

   - Open your IDE
   - Import as Maven project
   - Wait for dependencies to download

2. **Configure Tomcat**:

   - Add Tomcat server configuration
   - Deploy the webapp

3. **Run**:
   - Start Tomcat server
   - Access application at `http://localhost:8084/LibraryManagementSystem`

## Default Accounts

### Librarian Account

- **Email**: `admin@library.com`
- **Password**: `admin123`

### Member Account

- **Email**: `john.doe@email.com`
- **Password**: `member123`

⚠️ Default credentials are for demo/testing purposes only.

## Key Features Implementation

### OOP Concepts

- **Polymorphism**: DAO interfaces with multiple implementations
- **Inheritance**: Model classes with proper inheritance hierarchy
- **Exception Handling**: Custom exceptions (DatabaseException, BusinessException)
- **Interfaces**: DAO interfaces for abstraction

### Collections & Generics

- Use of `List<T>`, `Map<K,V>`, and generics throughout the application
- Stream API for data processing
- Collection operations for grouping and filtering

### Multithreading

- `NotificationService`: Asynchronous notification sending using ExecutorService
- `ReportService`: Concurrent report generation using CompletableFuture

### JDBC

- Connection pooling with Apache DBCP2
- Prepared statements for SQL injection prevention
- Transaction management
- ResultSet mapping to model objects

### Servlets & Web Integration

- RESTful URL patterns
- Session management
- Request/response handling
- Forward/redirect patterns

## Application Flow

1. **User Authentication**: Login servlet validates credentials and creates session
2. **Role-Based Access**: Librarian and Member have different dashboards
3. **Data Access Layer**: DAOs handle all database operations
4. **Business Logic Layer**: Services contain business rules and validations
5. **Presentation Layer**: JSP pages render the UI
6. **Controller Layer**: Servlets handle HTTP requests and coordinate between layers

## Security Features

- Session-based authentication
- Role-based access control
- SQL injection prevention (PreparedStatements)
- Input validation
- Password protection (basic - for production, use encryption)

## Future Enhancements

- Password encryption (BCrypt)
- JWT token-based authentication
- RESTful API endpoints
- Unit and integration testing
- Logging framework (Log4j2)
- Email notifications
- Advanced search with filters
- Book reservations
- Fine payment integration

## Troubleshooting

### Database Connection Issues

- Verify MySQL service is running
- Check database credentials in `database.properties`
- Ensure database `librarydb` exists

### Build Errors

- Ensure Java 21 is installed
- Run `mvn clean install` to refresh dependencies
- Check Maven settings.xml configuration

### Deployment Issues

- Verify Tomcat version compatibility (9.0+)
- Check WAR file is deployed correctly
- Review Tomcat logs for errors

## License

This project is developed for educational and learning purposes.

## Contact

For issues or questions, please refer to the project repository.
