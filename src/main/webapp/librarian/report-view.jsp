<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="java.util.Map" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null || !user.isLibrarian()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    @SuppressWarnings("unchecked")
    Map<String, Object> report = (Map<String, Object>) request.getAttribute("report");
    String reportType = (String) request.getAttribute("reportType");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Report - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>📊 Report</h1>
        <nav>
            <a href="<%= request.getContextPath() %>/librarian/reports/">Back to Reports</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </nav>
    </div>
    
    <div class="container">
        <% if (report != null && reportType != null) { %>
            <div class="card">
                <h2><%= reportType.equals("inventory") ? "Inventory Report" : 
                         reportType.equals("trends") ? "Borrowing Trends Report" : 
                         "Overdue Books Report" %></h2>
                
                <% if (reportType.equals("inventory")) { %>
                    <p><strong>Total Books:</strong> <%= report.get("totalBooks") %></p>
                    <p><strong>Total Copies:</strong> <%= report.get("totalCopies") %></p>
                    <p><strong>Available Copies:</strong> <%= report.get("availableCopies") %></p>
                    <p><strong>Borrowed Copies:</strong> <%= report.get("borrowedCopies") %></p>
                    
                    <% if (report.get("booksByGenre") != null) { %>
                        <h3>Books by Genre</h3>
                        <pre><%= report.get("booksByGenre") %></pre>
                    <% } %>
                    
                <% } else if (reportType.equals("trends")) { %>
                    <p><strong>Total Transactions:</strong> <%= report.get("totalTransactions") %></p>
                    <p><strong>Borrow Transactions:</strong> <%= report.get("borrowTransactions") %></p>
                    <p><strong>Return Transactions:</strong> <%= report.get("returnTransactions") %></p>
                    
                <% } else if (reportType.equals("overdue")) { %>
                    <p><strong>Total Overdue:</strong> <%= report.get("totalOverdue") %></p>
                    <p><strong>Total Fines:</strong> $<%= report.get("totalFines") %></p>
                <% } %>
            </div>
        <% } else { %>
            <div class="alert alert-error">No report data available</div>
        <% } %>
        
        <a href="<%= request.getContextPath() %>/librarian/reports/" class="btn">Back to Reports</a>
    </div>
</body>
</html>

