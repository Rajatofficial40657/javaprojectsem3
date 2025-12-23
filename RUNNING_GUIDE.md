# 🚀 Running Guide – Library Management System

This guide explains how to build, deploy, and run the **Library Management System** on your local machine.

---

## ✅ Prerequisites

Ensure the following are installed:

### 1️⃣ Java
- **Java JDK 21** (recommended)
- Verify:
```bash
java -version
2️⃣ Maven
Maven 3.6+

Verify:

bash
Copy code
mvn -version
3️⃣ MySQL
MySQL 8.0+

MySQL Server must be running

Verify:

bash
Copy code
mysql --version
4️⃣ Apache Tomcat
Apache Tomcat 9.0

Example installation path:

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
Edit the file:

css
Copy code
src/main/resources/database.properties
Example configuration:

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
📌 Replace YOUR_MYSQL_PASSWORD with your actual MySQL password.

🛠️ Step 3: Build the Project
Navigate to the project root:

bash
Copy code
cd E:\javaprojectsem3
Build the project:

bash
Copy code
mvn clean package
✅ On success, this file will be created:

aspectj
Copy code
target/LibraryManagementSystem.war
🚢 Step 4: Deploy on Tomcat
1️⃣ Copy WAR file
bash
Copy code
copy target\LibraryManagementSystem.war C:\tomcat9\webapps\
2️⃣ Start Tomcat
bash
Copy code
C:\tomcat9\bin\startup.bat
Wait until Tomcat starts completely.

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

⚠️ Default credentials are for demo/testing purposes only.

🧪 Step 6: Test Features
Librarian
Dashboard statistics

Add / Edit / Delete books

Manage members

Borrow & return books

Notifications & reports

Member
Search books

Borrow / return books

View borrowing history

Notifications

🛑 Stopping the Application
bash
Copy code
C:\tomcat9\bin\shutdown.bat
🧯 Troubleshooting
❌ MySQL Access Denied
Verify username/password in database.properties

Ensure MySQL service is running

❌ Port Already in Use
Change port in:

pgsql
Copy code
C:\tomcat9\conf\server.xml
Example:

xml
Copy code
<Connector port="8084" />
❌ 404 or 500 Error
Ensure WAR is deployed correctly

Check logs:

csharp
Copy code
C:\tomcat9\logs\catalina.out
✅ Notes
Context Path: /LibraryManagementSystem

Database: librarydb

Server: Apache Tomcat 9

Build Tool: Maven

🎓 Project Status
✅ Database connected
✅ Application deployed
✅ Login working
✅ Dashboard operational
