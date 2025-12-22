<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Notification" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="java.util.List" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    @SuppressWarnings("unchecked")
    List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
    Integer unreadCount = (Integer) request.getAttribute("unreadCount");
    
    if (notifications == null) {
        notifications = new java.util.ArrayList<>();
    }
    if (unreadCount == null) {
        unreadCount = 0;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Notifications - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>📧 Notifications (<%= unreadCount %> unread)</h1>
        <nav>
            <a href="<%= request.getContextPath() %>/member/dashboard.jsp">Dashboard</a>
            <a href="<%= request.getContextPath() %>/member/books/search">Search Books</a>
            <a href="<%= request.getContextPath() %>/member/history">Borrowing History</a>
            <a href="<%= request.getContextPath() %>/member/notifications/">Notifications</a>
            <a href="<%= request.getContextPath() %>/member/profile/">Profile</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </nav>
    </div>
    
    <div class="container">
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        
        <div class="card">
            <h2>Notifications</h2>
            <% if (notifications.isEmpty()) { %>
                <p style="text-align: center; color: #666; padding: 2rem;">No notifications</p>
            <% } else { %>
                <% for (Notification notification : notifications) { %>
                    <div class="card" style="margin-bottom: 1rem; <%= !notification.isRead() ? "background: #e3f2fd;" : "" %>">
                        <h3 style="color: #667eea; margin-bottom: 0.5rem;">
                            <%= notification.getTitle() %>
                            <% if (!notification.isRead()) { %>
                                <span style="background: #667eea; color: white; padding: 2px 8px; border-radius: 10px; font-size: 0.7rem;">NEW</span>
                            <% } %>
                        </h3>
                        <p style="color: #666; margin-bottom: 0.5rem;"><%= notification.getMessage() %></p>
                        <div style="display: flex; justify-content: space-between; align-items: center;">
                            <small style="color: #999;">
                                <%= notification.getCreatedAtString() != null ? notification.getCreatedAtString() : "" %> | 
                                Type: <%= notification.getNotificationType() %>
                            </small>
                            <% if (!notification.isRead()) { %>
                                <a href="<%= request.getContextPath() %>/member/notifications/read/<%= notification.getNotificationId() %>" 
                                   class="btn btn-small">Mark as Read</a>
                            <% } %>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </div>
    </div>
</body>
</html>

