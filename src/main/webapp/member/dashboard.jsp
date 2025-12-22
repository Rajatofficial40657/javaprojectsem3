<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="com.library.service.TransactionService" %>
<%@ page import="com.library.service.NotificationService" %>
<%@ page import="com.library.exception.BusinessException" %>
<%@ page import="java.util.List" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    TransactionService transactionService = new TransactionService();
    NotificationService notificationService = new NotificationService();
    
    int activeBorrows = 0;
    int unreadNotifications = 0;
    
    try {
        activeBorrows = transactionService.getActiveTransactionsByMemberId(user.getMemberId()).size();
        unreadNotifications = notificationService.getUnreadNotificationsByMemberId(user.getMemberId()).size();
    } catch (BusinessException e) {
        // Handle error
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Member Dashboard - Library Management System</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>📚 Library Management System - Member Dashboard</h1>
        <nav>
            <a href="<%= request.getContextPath() %>/member/dashboard.jsp">Dashboard</a>
            <a href="<%= request.getContextPath() %>/member/books/search">Search Books</a>
            <a href="<%= request.getContextPath() %>/member/history">Borrowing History</a>
            <a href="<%= request.getContextPath() %>/member/notifications/">Notifications (<%= unreadNotifications %>)</a>
            <a href="<%= request.getContextPath() %>/member/profile/">Profile</a>
            <span>Welcome, <%= user.getName() %></span>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </nav>
    </div>
    
    <div class="container">
        <div class="stats">
            <div class="stat-card">
                <h3><%= activeBorrows %></h3>
                <p>Active Borrows</p>
            </div>
            <div class="stat-card">
                <h3><%= unreadNotifications %></h3>
                <p>Unread Notifications</p>
            </div>
        </div>
        
        <div class="card">
            <h2>Quick Actions</h2>
            <div class="grid">
                <a href="<%= request.getContextPath() %>/member/books/search" class="btn">Search Books</a>
                <a href="<%= request.getContextPath() %>/member/history" class="btn">View History</a>
                <a href="<%= request.getContextPath() %>/member/notifications/" class="btn">View Notifications</a>
                <a href="<%= request.getContextPath() %>/member/profile/" class="btn">Update Profile</a>
            </div>
        </div>
    </div>
</body>
</html>

