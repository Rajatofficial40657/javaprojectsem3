<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Transaction" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="java.util.List" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null || !user.isLibrarian()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    @SuppressWarnings("unchecked")
    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
    if (transactions == null) {
        transactions = new java.util.ArrayList<>();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Transaction Management - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>📋 Transaction Management</h1>
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
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("success") %></div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        
        <div class="card">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                <h2>Transactions</h2>
                <a href="<%= request.getContextPath() %>/librarian/transactions/borrow" class="btn">Borrow Book</a>
            </div>
            
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Book</th>
                        <th>Member</th>
                        <th>Type</th>
                        <th>Borrow Date</th>
                        <th>Due Date</th>
                        <th>Return Date</th>
                        <th>Fine</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (transactions.isEmpty()) { %>
                        <tr>
                            <td colspan="10" style="text-align: center;">No transactions found</td>
                        </tr>
                    <% } else { %>
                        <% for (Transaction transaction : transactions) { %>
                            <tr>
                                <td><%= transaction.getTransactionId() %></td>
                                <td><%= transaction.getBook() != null ? transaction.getBook().getTitle() : "N/A" %></td>
                                <td><%= transaction.getMember() != null ? transaction.getMember().getName() : "N/A" %></td>
                                <td><%= transaction.getTransactionType() %></td>
                                <td><%= transaction.getBorrowDateString() != null ? transaction.getBorrowDateString() : "N/A" %></td>
                                <td><%= transaction.getDueDateString() != null ? transaction.getDueDateString() : "N/A" %></td>
                                <td><%= transaction.getReturnDateString() != null ? transaction.getReturnDateString() : "N/A" %></td>
                                <td>$<%= transaction.getFineAmount() != null ? transaction.getFineAmount() : "0.00" %></td>
                                <td><%= transaction.getStatus() %></td>
                                <td>
                                    <% if ("ACTIVE".equals(transaction.getStatus())) { %>
                                        <a href="<%= request.getContextPath() %>/librarian/transactions/return/<%= transaction.getTransactionId() %>" 
                                           class="btn btn-small btn-success"
                                           onclick="return confirm('Mark this book as returned?')">Return</a>
                                    <% } %>
                                </td>
                            </tr>
                        <% } %>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>

