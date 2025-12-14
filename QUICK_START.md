# Quick Start Guide

## Fast Setup (5 Minutes)

### 1. Install Prerequisites
- ✅ Java JDK 11+
- ✅ Maven 3.6+
- ✅ MySQL 8.0+
- ✅ Apache Tomcat 9.0+

### 2. Setup Database
```sql
-- Login to MySQL
mysql -u root -p

-- Create database and import schema
CREATE DATABASE librarydb;
USE librarydb;
SOURCE src/main/resources/database.sql;
```

Or manually:
1. Open `src/main/resources/database.sql`
2. Copy all SQL commands
3. Execute in MySQL Workbench

### 3. Configure Database
Edit `src/main/resources/database.properties`:
```properties
db.username=root
db.password=YOUR_PASSWORD_HERE
```

### 4. Build Project
```bash
cd E:\javaproject
mvn clean package
```

### 5. Deploy to Tomcat
```bash
# Copy WAR file
copy target\LibraryManagementSystem.war C:\apache-tomcat-9.0\webapps\

# Start Tomcat
cd C:\apache-tomcat-9.0\bin
startup.bat
```

### 6. Access Application
Open browser: **http://localhost:8080/LibraryManagementSystem**

**Login Credentials:**
- **Librarian**: admin@library.com / admin123
- **Member**: john.doe@email.com / member123

## Using IDE (Easier)

### IntelliJ IDEA:

1. **Open Project**
   - File → Open → Select `pom.xml`
   - Wait for Maven sync

2. **Add Tomcat Run Configuration**
   - Run → Edit Configurations
   - + → Tomcat Server → Local
   - Tomcat Home: Select your Tomcat folder
   - Deployment → Add → Artifact → `LibraryManagementSystem:war`

3. **Run**
   - Click Run button (Green Play icon)
   - Browser opens automatically

### Eclipse:

1. **Import Project**
   - File → Import → Maven → Existing Maven Projects
   - Select project folder

2. **Add Tomcat**
   - Servers view → New → Server → Tomcat 9.0
   - Right-click project → Properties → Project Facets
   - Enable "Dynamic Web Module" and "Java"

3. **Run**
   - Drag project to Tomcat server
   - Right-click Tomcat → Start

## Common Issues

### MySQL Connection Error
- ✅ Check MySQL is running
- ✅ Verify password in `database.properties`
- ✅ Ensure `librarydb` database exists

### Port 8080 Already in Use
- ✅ Stop other applications using port 8080
- ✅ Or change Tomcat port in `conf/server.xml`

### Build Fails
- ✅ Check Java version: `java -version` (need 11+)
- ✅ Check Maven: `mvn -version` (need 3.6+)
- ✅ Delete `target` folder and rebuild

### 404 Error
- ✅ Check URL: `http://localhost:8080/LibraryManagementSystem`
- ✅ Verify WAR file in `webapps` folder
- ✅ Check Tomcat logs for errors

## File Structure Check

Make sure you have:
```
javaproject/
├── pom.xml ✅
├── src/main/
│   ├── java/ ✅
│   ├── resources/
│   │   ├── database.properties ✅
│   │   └── database.sql ✅
│   └── webapp/ ✅
└── target/
    └── LibraryManagementSystem.war ✅ (after build)
```

That's it! You're ready to use the Library Management System! 🎉

