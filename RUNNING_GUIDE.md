# How to Run Library Management System

## Prerequisites

Before running the application, make sure you have the following installed:

1. **Java JDK 11 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version`

2. **Maven 3.6+**
   - Download from: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

3. **MySQL 8.0+**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Make sure MySQL server is running

4. **Apache Tomcat 9.0 or 10.0**
   - Download from: https://tomcat.apache.org/download-90.cgi
   - Extract to a folder (e.g., `C:\apache-tomcat-9.0`)

## Step-by-Step Instructions

### Step 1: Setup MySQL Database

1. **Start MySQL Server**
   ```bash
   # On Windows (if MySQL is installed as service)
   net start MySQL
   
   # Or start MySQL Workbench / Command Line
   ```

2. **Create Database**
   ```sql
   -- Open MySQL command line or MySQL Workbench
   mysql -u root -p
   
   -- Create database
   CREATE DATABASE librarydb;
   
   -- Use the database
   USE librarydb;
   ```

3. **Run SQL Script**
   ```bash
   # Option 1: Using MySQL command line
   mysql -u root -p librarydb < src/main/resources/database.sql
   
   # Option 2: Copy and paste SQL script into MySQL Workbench
   # Open src/main/resources/database.sql and execute it
   ```

4. **Verify Tables Created**
   ```sql
   SHOW TABLES;
   -- Should show: books, members, transactions, notifications
   ```

### Step 2: Configure Database Connection

1. **Edit Database Properties**
   - Open: `src/main/resources/database.properties`
   - Update with your MySQL credentials:
   ```properties
   db.driver=com.mysql.cj.jdbc.Driver
   db.url=jdbc:mysql://localhost:3306/librarydb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.username=root
   db.password=YOUR_MYSQL_PASSWORD
   ```
   - Replace `YOUR_MYSQL_PASSWORD` with your actual MySQL root password

### Step 3: Build the Project

1. **Open Command Prompt/Terminal**
   - Navigate to project directory:
   ```bash
   cd E:\javaproject
   ```

2. **Build with Maven**
   ```bash
   # Clean and compile
   mvn clean compile
   
   # Package into WAR file
   mvn clean package
   ```
   
   This will create: `target/LibraryManagementSystem.war`

3. **Verify Build Success**
   - Check for `BUILD SUCCESS` message
   - Verify WAR file exists in `target/` folder

### Step 4: Deploy to Tomcat

#### Option A: Manual Deployment (Recommended for Testing)

1. **Copy WAR File**
   ```bash
   # Copy the WAR file to Tomcat webapps folder
   copy target\LibraryManagementSystem.war C:\apache-tomcat-9.0\webapps\
   # Or
   xcopy target\LibraryManagementSystem.war C:\apache-tomcat-9.0\webapps\
   ```

2. **Start Tomcat**
   ```bash
   # On Windows
   cd C:\apache-tomcat-9.0\bin
   startup.bat
   
   # On Linux/Mac
   cd /path/to/apache-tomcat-9.0/bin
   ./startup.sh
   ```

3. **Verify Tomcat is Running**
   - Check console for "Server startup in [XXX] milliseconds"
   - Open browser: `http://localhost:8080`
   - You should see Tomcat welcome page

#### Option B: Using IDE (IntelliJ IDEA / Eclipse)

**IntelliJ IDEA:**

1. **Import Project**
   - File → Open → Select `pom.xml`
   - Choose "Open as Project"
   - Wait for Maven to download dependencies

2. **Add Tomcat Configuration**
   - Run → Edit Configurations
   - Click "+" → Tomcat Server → Local
   - Configure Tomcat home directory
   - In Deployment tab, add artifact: `LibraryManagementSystem:war`
   - Set Application context: `/LibraryManagementSystem`

3. **Run Application**
   - Click Run button or press Shift+F10
   - Tomcat will start and deploy application

**Eclipse:**

1. **Import Project**
   - File → Import → Maven → Existing Maven Projects
   - Select project directory
   - Wait for dependencies download

2. **Add Tomcat Server**
   - Right-click project → Properties
   - Project Facets → Convert to faceted form
   - Enable Dynamic Web Module and Java
   - Servers tab → Add server → Tomcat 9.0
   - Drag project to Tomcat server

3. **Run Application**
   - Right-click Tomcat server → Start
   - Application will deploy automatically

### Step 5: Access the Application

1. **Open Web Browser**
   - Go to: `http://localhost:8080/LibraryManagementSystem`
   - Or: `http://localhost:8080/LibraryManagementSystem/index.jsp`

2. **Login as Librarian**
   - Email: `admin@library.com`
   - Password: `admin123`
   - Click "Login"

3. **Login as Member** (in new browser/incognito)
   - Email: `john.doe@email.com`
   - Password: `member123`
   - Click "Login"

### Step 6: Test the Application

**Librarian Features:**
- Dashboard shows statistics
- Add/Edit/Delete Books
- Manage Members
- Process Transactions (Borrow/Return)
- Send Notifications
- Generate Reports

**Member Features:**
- Search Books
- Borrow Books
- Return Books
- View Borrowing History
- Update Profile
- View Notifications

## Troubleshooting

### Issue: "Cannot connect to MySQL"

**Solution:**
- Verify MySQL service is running
- Check database credentials in `database.properties`
- Ensure database `librarydb` exists
- Test connection: `mysql -u root -p`

### Issue: "Port 8080 already in use"

**Solution:**
- Change Tomcat port in `conf/server.xml`:
  ```xml
  <Connector port="8081" ... />
  ```
- Or stop other application using port 8080
- Access application at: `http://localhost:8081/LibraryManagementSystem`

### Issue: "ClassNotFoundException" or "NoClassDefFoundError"

**Solution:**
- Clean and rebuild: `mvn clean install`
- Verify all dependencies in `pom.xml` are downloaded
- Check Maven repository: `~/.m2/repository`

### Issue: "404 Not Found"

**Solution:**
- Verify WAR file is deployed correctly
- Check Tomcat logs: `logs/catalina.out`
- Ensure application context is correct: `/LibraryManagementSystem`

### Issue: "500 Internal Server Error"

**Solution:**
- Check Tomcat logs for detailed error
- Verify database connection settings
- Ensure all tables exist in database
- Check JSP syntax errors

### Issue: "Build Failed"

**Solution:**
- Verify Java version: `java -version` (should be 11+)
- Verify Maven version: `mvn -version` (should be 3.6+)
- Delete `target` folder and rebuild: `mvn clean install`
- Check internet connection for Maven dependency download

## Quick Start Script (Windows)

Create a file `run.bat`:

```batch
@echo off
echo Starting Library Management System...

echo Step 1: Building project...
call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Check errors above.
    pause
    exit /b 1
)

echo Step 2: Copying WAR to Tomcat...
copy target\LibraryManagementSystem.war C:\apache-tomcat-9.0\webapps\

echo Step 3: Starting Tomcat...
cd C:\apache-tomcat-9.0\bin
start startup.bat

echo.
echo Application deployed successfully!
echo Open browser: http://localhost:8080/LibraryManagementSystem
echo.
pause
```

Run it: `run.bat`

## Quick Start Script (Linux/Mac)

Create a file `run.sh`:

```bash
#!/bin/bash
echo "Starting Library Management System..."

echo "Step 1: Building project..."
mvn clean package

if [ $? -ne 0 ]; then
    echo "Build failed! Check errors above."
    exit 1
fi

echo "Step 2: Copying WAR to Tomcat..."
cp target/LibraryManagementSystem.war /path/to/apache-tomcat-9.0/webapps/

echo "Step 3: Starting Tomcat..."
cd /path/to/apache-tomcat-9.0/bin
./startup.sh

echo ""
echo "Application deployed successfully!"
echo "Open browser: http://localhost:8080/LibraryManagementSystem"
```

Make it executable: `chmod +x run.sh`
Run it: `./run.sh`

## Stopping the Application

1. **Stop Tomcat**
   ```bash
   # Windows
   C:\apache-tomcat-9.0\bin\shutdown.bat
   
   # Linux/Mac
   /path/to/apache-tomcat-9.0/bin/shutdown.sh
   ```

2. **Or from IDE**
   - Stop server button in IntelliJ/Eclipse

## Additional Notes

- **Default Port**: 8080 (can be changed in Tomcat `server.xml`)
- **Context Path**: `/LibraryManagementSystem`
- **Database**: `librarydb` on localhost:3306
- **Session Timeout**: 30 minutes (configured in `web.xml`)

## Need Help?

- Check Tomcat logs: `apache-tomcat-9.0/logs/catalina.out`
- Check application logs in console
- Verify all prerequisites are installed correctly
- Ensure database is accessible and tables are created

