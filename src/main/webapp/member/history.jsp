<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Transaction" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    @SuppressWarnings("unchecked")
    List<Transaction> allTransactions = (List<Transaction>) request.getAttribute("allTransactions");
    @SuppressWarnings("unchecked")
    List<Transaction> activeTransactions = (List<Transaction>) request.getAttribute("activeTransactions");
    
    if (allTransactions == null) allTransactions = new java.util.ArrayList<>();
    if (activeTransactions == null) activeTransactions = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Borrowing History - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>📚 Borrowing History</h1>
        <nav>
            <a href="<%= request.getContextPath() %>/member/dashboard.jsp">Dashboard</a>
            <a href="<%= request.getContextPath() %>/member/books/search">Search Books</a>
            <a href="<%= request.getContextPath() %>/member/history">Borrowing History</a>
            <a href="<%= request.getContextPath() %>/member/profile/">Profile</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </nav>
    </div>
    
    <div class="container">
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("success") %></div>
        <% } %>
        
        <div class="card">
            <h2>Active Borrows (<%= activeTransactions.size() %>)</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Book</th>
                        <th>Borrow Date</th>
                        <th>Due Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (activeTransactions.isEmpty()) { %>
                        <tr>
                            <td colspan="5" style="text-align: center;">No active borrows</td>
                        </tr>
                    <% } else { %>
                        <% for (Transaction transaction : activeTransactions) { %>
                            <tr>
                                <td><%= transaction.getBook() != null ? transaction.getBook().getTitle() : "N/A" %></td>
                                <td><%= transaction.getBorrowDateString() != null ? transaction.getBorrowDateString() : "N/A" %></td>
                                <td><%= transaction.getDueDateString() != null ? transaction.getDueDateString() : "N/A" %></td>
                                <td>
                                    <% if (transaction.isOverdue()) { %>
                                        <span style="color: red; font-weight: bold;">OVERDUE</span>
                                    <% } else { %>
                                        <%= transaction.getStatus() %>
                                    <% } %>
                                </td>
                                <td>
                                    <form action="<%= request.getContextPath() %>/member/books/return" method="post" style="display: inline;">
                                        <input type="hidden" name="transactionId" value="<%= transaction.getTransactionId() %>">
                                        <button type="submit" class="btn btn-small btn-success"
                                                onclick="return confirm('Return this book?')">Return</button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                    <% } %>
                </tbody>
            </table>
        </div>
        
        <div class="card">
            <h2>All Transactions</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Book</th>
                        <th>Borrow Date</th>
                        <th>Due Date</th>
                        <th>Return Date</th>
                        <th>Fine</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (allTransactions.isEmpty()) { %>
                        <tr>
                            <td colspan="6" style="text-align: center;">No transactions found</td>
                        </tr>
                    <% } else { %>
                        <% for (Transaction transaction : allTransactions) { %>
                            <tr>
                                <td><%= transaction.getBook() != null ? transaction.getBook().getTitle() : "N/A" %></td>
                                <td><%= transaction.getBorrowDateString() != null ? transaction.getBorrowDateString() : "N/A" %></td>
                                <td><%= transaction.getDueDateString() != null ? transaction.getDueDateString() : "N/A" %></td>
                                <td><%= transaction.getReturnDateString() != null ? transaction.getReturnDateString() : "N/A" %></td>
                                <td>$<%= transaction.getFineAmount() != null ? transaction.getFineAmount() : "0.00" %></td>
                                <td><%= transaction.getStatus() %></td>
                            </tr>
                        <% } %>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>

