# Library Management System - Prerequisites Checker
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Prerequisites Checker" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$allGood = $true

# Check Java
Write-Host "Checking Java..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    if ($javaVersion -match "version") {
        Write-Host "✓ Java is installed" -ForegroundColor Green
        Write-Host "  $javaVersion" -ForegroundColor Gray
    } else {
        Write-Host "✗ Java not found" -ForegroundColor Red
        $allGood = $false
    }
} catch {
    Write-Host "✗ Java not found" -ForegroundColor Red
    Write-Host "  Please install Java JDK 11+ from https://www.oracle.com/java/" -ForegroundColor Yellow
    $allGood = $false
}

Write-Host ""

# Check Maven
Write-Host "Checking Maven..." -ForegroundColor Yellow
try {
    $mvnVersion = mvn -version 2>&1 | Select-Object -First 1
    if ($mvnVersion -match "Apache Maven") {
        Write-Host "✓ Maven is installed" -ForegroundColor Green
        Write-Host "  $mvnVersion" -ForegroundColor Gray
    } else {
        Write-Host "✗ Maven not found in PATH" -ForegroundColor Red
        $allGood = $false
    }
} catch {
    Write-Host "✗ Maven not found in PATH" -ForegroundColor Red
    Write-Host "  Please install Maven from https://maven.apache.org/" -ForegroundColor Yellow
    Write-Host "  OR use an IDE (IntelliJ/Eclipse) which has built-in Maven" -ForegroundColor Yellow
    $allGood = $false
}

Write-Host ""

# Check MySQL
Write-Host "Checking MySQL..." -ForegroundColor Yellow
try {
    $mysqlVersion = mysql --version 2>&1
    if ($mysqlVersion -match "mysql") {
        Write-Host "✓ MySQL command line client found" -ForegroundColor Green
        Write-Host "  $mysqlVersion" -ForegroundColor Gray
    } else {
        Write-Host "⚠ MySQL command line client not in PATH" -ForegroundColor Yellow
        Write-Host "  (MySQL might be installed but not in PATH)" -ForegroundColor Gray
    }
} catch {
    Write-Host "⚠ MySQL command line client not found" -ForegroundColor Yellow
    Write-Host "  You can use MySQL Workbench instead" -ForegroundColor Gray
}

Write-Host ""

# Check Project Files
Write-Host "Checking Project Files..." -ForegroundColor Yellow
$projectPath = "E:\javaproject"
if (Test-Path $projectPath\pom.xml) {
    Write-Host "✓ pom.xml found" -ForegroundColor Green
} else {
    Write-Host "✗ pom.xml not found" -ForegroundColor Red
    $allGood = $false
}

if (Test-Path $projectPath\src\main\java) {
    Write-Host "✓ Java source files found" -ForegroundColor Green
} else {
    Write-Host "✗ Java source files not found" -ForegroundColor Red
    $allGood = $false
}

if (Test-Path $projectPath\src\main\webapp) {
    Write-Host "✓ Webapp files found" -ForegroundColor Green
} else {
    Write-Host "✗ Webapp files not found" -ForegroundColor Red
    $allGood = $false
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan

if ($allGood) {
    Write-Host "All prerequisites are met!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Cyan
    Write-Host "1. Setup database: Run setup-database.bat" -ForegroundColor White
    Write-Host "2. Configure database: Edit src\main\resources\database.properties" -ForegroundColor White
    Write-Host "3. Build project: Run run.bat or 'mvn clean package'" -ForegroundColor White
    Write-Host "4. Deploy to Tomcat and start server" -ForegroundColor White
} else {
    Write-Host "Some prerequisites are missing" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Please install missing components:" -ForegroundColor Cyan
    Write-Host "- Java JDK 11+: https://www.oracle.com/java/" -ForegroundColor White
    Write-Host "- Maven 3.6+: https://maven.apache.org/" -ForegroundColor White
    Write-Host "- OR use IDE (IntelliJ/Eclipse) with built-in Maven" -ForegroundColor White
    Write-Host "- MySQL 8.0+: https://dev.mysql.com/downloads/" -ForegroundColor White
    Write-Host "- Tomcat 9.0+: https://tomcat.apache.org/" -ForegroundColor White
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

