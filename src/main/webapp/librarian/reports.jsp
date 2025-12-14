<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null || !user.isLibrarian()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Reports - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>📊 Reports</h1>
        <nav>
            <a href="<%= request.getContextPath() %>/librarian/dashboard.jsp">Dashboard</a>
            <a href="<%= request.getContextPath() %>/librarian/books/">Books</a>
            <a href="<%= request.getContextPath() %>/librarian/members/">Members</a>
            <a href="<%= request.getContextPath() %>/librarian/transactions/">Transactions</a>
            <a href="<%= request.getContextPath() %>/librarian/notifications/">Notifications</a>
            <a href="<%= request.getContextPath() %>/librarian/reports/">Reports</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </nav>
    </div>
    
    <div class="container">
        <div class="grid">
            <div class="card">
                <h2>Inventory Report</h2>
                <p>Generate a detailed inventory report showing book counts, availability, and status.</p>
                <a href="<%= request.getContextPath() %>/librarian/reports/inventory" class="btn">Generate Report</a>
            </div>
            
            <div class="card">
                <h2>Borrowing Trends Report</h2>
                <p>Generate a report showing borrowing trends over a specified date range.</p>
                <a href="<%= request.getContextPath() %>/librarian/reports/trends" class="btn">Generate Report</a>
            </div>
            
            <div class="card">
                <h2>Overdue Books Report</h2>
                <p>Generate a report of all overdue books and associated fines.</p>
                <a href="<%= request.getContextPath() %>/librarian/reports/overdue" class="btn">Generate Report</a>
            </div>
        </div>
    </div>
</body>
</html>

