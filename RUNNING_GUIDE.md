# 🏃 RUNNING GUIDE – Library Management System

This document explains step by step how to set up, build, deploy, and run the
Library Management System on a local machine.

---

## ✅ Prerequisites

Make sure the following software is installed:

### 1️⃣ Java
- Java JDK 11 or higher (Java 21 recommended)
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

Ensure MySQL Server is running

Verify:

bash
Copy code
mysql --version
4️⃣ Apache Tomcat
Apache Tomcat 9.0

Download: https://tomcat.apache.org/download-90.cgi

Example path:

makefile
Copy code
C:\tomcat9
🗄️ Step 1: Database Setup
1️⃣ Login to MySQL
bash
Copy code
mysql -u root -p
2️⃣ Create Database
sql
Copy code
CREATE DATABASE librarydb;
USE librarydb;
3️⃣ Import Database Schema (PowerShell)
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
Edit the file:

css
Copy code
src/main/resources/database.properties
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
Replace YOUR_MYSQL_PASSWORD with your actual MySQL password.

🛠️ Step 3: Build the Project
Navigate to project root:

bash
Copy code
cd E:\javaprojectsem3
Build using Maven:

bash
Copy code
mvn clean package
On success:

nginx
Copy code
BUILD SUCCESS
WAR file generated:

aspectj
Copy code
target/LibraryManagementSystem.war
🚢 Step 4: Deploy on Tomcat
1️⃣ Copy WAR File
bash
Copy code
copy target\LibraryManagementSystem.war C:\tomcat9\webapps\
2️⃣ Start Tomcat
bash
Copy code
C:\tomcat9\bin\startup.bat
Wait until Tomcat fully starts.

🌐 Step 5: Access Application
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

⚠️ Default credentials are for demo/testing only.

🧪 Step 6: Verify Features
Librarian
Dashboard statistics

Manage books & members

Borrow / return books

Notifications & reports

Member
Search books

Borrow / return books

View history & notifications

🛑 Stop the Application
bash
Copy code
C:\tomcat9\bin\shutdown.bat
🧯 Troubleshooting
❌ MySQL Connection Error
Check MySQL service

Verify credentials

Test:

bash
Copy code
mysql -u root -p
❌ Port Already in Use
Edit:

pgsql
Copy code
C:\tomcat9\conf\server.xml
xml
Copy code
<Connector port="8084" />
❌ 404 / 500 Errors
Check Tomcat logs:

csharp
Copy code
C:\tomcat9\logs\catalina.out
📝 Notes
Context Path: /LibraryManagementSystem

Database: librarydb

Server: Apache Tomcat 9

Build Tool: Maven

Session Timeout: 30 minutes

✅ Project Status
✔ Database configured
✔ WAR built
✔ Application deployed
✔ Login & dashboard working

