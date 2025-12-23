# 🚀 How to Run Library Management System

This guide explains how to see the **Library Management System** running on your local machine.

---

## ✅ Prerequisites

Before running the application, make sure you have the following installed:

### 1️⃣ Java
- **Java JDK 11 or higher (Java 21 recommended)**
- Download: https://www.oracle.com/java/technologies/downloads/
- Verify:
```bash
java -version
2️⃣ Maven
Maven 3.6+

Download: https://maven.apache.org/download.cgi

Verify:

bash
Copy code
mvn -version
3️⃣ MySQL
MySQL 8.0+

Download: https://dev.mysql.com/downloads/mysql/

Make sure MySQL Server is running

Verify:

bash
Copy code
mysql --version
4️⃣ Apache Tomcat
Apache Tomcat 9.0

Download: https://tomcat.apache.org/download-90.cgi

Example install path:

makefile
Copy code
C:\tomcat9
🗄️ Step 1: Setup MySQL Database
1️⃣ Start MySQL
bash
Copy code
mysql -u root -p
2️⃣ Create Database
sql
Copy code
CREATE DATABASE librarydb;
USE librarydb;
3️⃣ Import SQL Schema (PowerShell-safe)
powershell
Copy code
Get-Content src\main\resources\database.sql | mysql -u root -p librarydb
4️⃣ Verify Tables
sql
Copy code
SHOW TABLES;
Expected tables:

books

members

transactions

notifications

⚙️ Step 2: Configure Database Connection
Open:

css
Copy code
src/main/resources/database.properties
Update it as follows:

properties
Copy code
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/librarydb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=YOUR_MYSQL_PASSWORD

db.initialSize=5
db.maxActive=20
db.maxIdle=10
db.minIdle=5
📌 Replace YOUR_MYSQL_PASSWORD with your real MySQL password.

🛠️ Step 3: Build the Project
Navigate to project root:

bash
Copy code
cd E:\javaprojectsem3
Build the project:

bash
Copy code
mvn clean package
✅ On success:

nginx
Copy code
BUILD SUCCESS
WAR file generated:

aspectj
Copy code
target/LibraryManagementSystem.war
🚢 Step 4: Deploy to Tomcat
1️⃣ Copy WAR File
bash
Copy code
copy target\LibraryManagementSystem.war C:\tomcat9\webapps\
2️⃣ Start Tomcat
bash
Copy code
C:\tomcat9\bin\startup.bat
Wait until Tomcat fully starts.

🌐 Step 5: Access the Application
Open browser:

bash
Copy code
http://localhost:8084/LibraryManagementSystem/login
🔐 Default Login Credentials
Librarian
Email: admin@library.com

Password: admin123

Member
Email: john.doe@email.com

Password: member123

⚠️ Default credentials are for demo/testing purposes only.

🧪 Step 6: Test the Application
Librarian Features
Dashboard statistics

Add / Edit / Delete books

Manage members

Borrow & return books

Notifications & reports

Member Features
Search books

Borrow & return books

View borrowing history

Notifications

🛑 Stopping the Application
bash
Copy code
C:\tomcat9\bin\shutdown.bat
🧯 Troubleshooting
❌ Cannot connect to MySQL
Ensure MySQL is running

Check database.properties

Verify login:

bash
Copy code
mysql -u root -p
❌ Port Already in Use
Change port in:

pgsql
Copy code
C:\tomcat9\conf\server.xml
Example:

xml
Copy code
<Connector port="8084" />
❌ 404 Not Found
Verify WAR deployment

Check logs:

csharp
Copy code
C:\tomcat9\logs\catalina.out
❌ 500 Internal Server Error
Check Tomcat logs

Verify database tables

Check JSP compilation errors

📝 Notes
Context Path: /LibraryManagementSystem

Database: librarydb

Server: Apache Tomcat 9

Build Tool: Maven

✅ Project Status
✔ Database connected
✔ WAR deployed
✔ Login working
✔ Dashboard functional

