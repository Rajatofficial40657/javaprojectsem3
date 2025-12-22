<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="com.library.service.BookService" %>
<%@ page import="com.library.service.MemberService" %>
<%@ page import="com.library.service.TransactionService" %>
<%@ page import="com.library.exception.BusinessException" %>
<%@ page import="java.util.Map" %>

<%
    // Session validation
    Member user = (Member) session.getAttribute("user");
    if (user == null || !user.isLibrarian()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    BookService bookService = new BookService();
    MemberService memberService = new MemberService();
    TransactionService transactionService = new TransactionService();

    int totalBooks = 0;
    int totalMembers = 0;
    int activeTransactions = 0;
    int overdueTransactions = 0;

    try {
        totalBooks = bookService.getAllBooks().size();
        totalMembers = memberService.getAllRegularMembers().size();

        Map<String, Object> stats = transactionService.getTransactionStatistics();
        activeTransactions = ((Long) stats.get("activeTransactions")).intValue();
        overdueTransactions = ((Long) stats.get("overdueTransactions")).intValue();

    } catch (BusinessException e) {
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Librarian Dashboard - Library Management System</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>
<div class="header">
    <h1>📚 Library Management System - Librarian Dashboard</h1>
    <nav>
        <a href="<%= request.getContextPath() %>/librarian/dashboard.jsp">Dashboard</a>
        <a href="<%= request.getContextPath() %>/librarian/books/">Books</a>
        <a href="<%= request.getContextPath() %>/librarian/members/">Members</a>
        <a href="<%= request.getContextPath() %>/librarian/transactions/">Transactions</a>
        <a href="<%= request.getContextPath() %>/librarian/notifications/">Notifications</a>
        <a href="<%= request.getContextPath() %>/librarian/reports/">Reports</a>
        <span>Welcome, <strong><%= user.getName() %></strong></span>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </nav>
</div>

<div class="container">

    <div class="stats">
        <div class="stat-card">
            <h3><%= totalBooks %></h3>
            <p>Total Books</p>
        </div>

        <div class="stat-card">
            <h3><%= totalMembers %></h3>
            <p>Total Members</p>
        </div>

        <div class="stat-card">
            <h3><%= activeTransactions %></h3>
            <p>Active Transactions</p>
        </div>

        <div class="stat-card">
            <h3><%= overdueTransactions %></h3>
            <p>Overdue Books</p>
        </div>
    </div>

    <div class="card">
        <h2>Quick Actions</h2>
        <div class="grid">
            <a href="<%= request.getContextPath() %>/librarian/books/add" class="btn">Add Book</a>
            <a href="<%= request.getContextPath() %>/librarian/members/add" class="btn">Add Member</a>
            <a href="<%= request.getContextPath() %>/librarian/transactions/borrow" class="btn">Borrow Book</a>
            <a href="<%= request.getContextPath() %>/librarian/notifications/" class="btn">Send Notification</a>
        </div>
    </div>

</div>
</body>
</html>
