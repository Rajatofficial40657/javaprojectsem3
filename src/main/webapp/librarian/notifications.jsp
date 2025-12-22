<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="java.util.List" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null || !user.isLibrarian()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    @SuppressWarnings("unchecked")
    List<Member> members = (List<Member>) request.getAttribute("members");
    if (members == null) {
        members = new java.util.ArrayList<>();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Notification Management - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>📧 Notification Management</h1>
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
            <h2>Send Notification to Member</h2>
            <form action="<%= request.getContextPath() %>/librarian/notifications/" method="post">
                <input type="hidden" name="action" value="send">
                
                <div class="form-group">
                    <label for="memberId">Member *</label>
                    <select id="memberId" name="memberId" required>
                        <option value="">Select a member</option>
                        <% for (Member member : members) { %>
                            <option value="<%= member.getMemberId() %>">
                                <%= member.getName() %> (<%= member.getEmail() %>)
                            </option>
                        <% } %>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="notificationType">Notification Type *</label>
                    <select id="notificationType" name="notificationType" required>
                        <option value="GENERAL">General</option>
                        <option value="DUE_DATE">Due Date Reminder</option>
                        <option value="OVERDUE">Overdue Notice</option>
                        <option value="NEW_ARRIVAL">New Arrival</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="title">Title *</label>
                    <input type="text" id="title" name="title" required>
                </div>
                
                <div class="form-group">
                    <label for="message">Message *</label>
                    <textarea id="message" name="message" required></textarea>
                </div>
                
                <button type="submit" class="btn">Send Notification</button>
            </form>
        </div>
        
        <div class="card">
            <h2>Send Notification to All Members</h2>
            <form action="<%= request.getContextPath() %>/librarian/notifications/" method="post">
                <input type="hidden" name="action" value="sendToAll">
                
                <div class="form-group">
                    <label for="notificationTypeAll">Notification Type *</label>
                    <select id="notificationTypeAll" name="notificationType" required>
                        <option value="GENERAL">General</option>
                        <option value="NEW_ARRIVAL">New Arrival</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="titleAll">Title *</label>
                    <input type="text" id="titleAll" name="title" required>
                </div>
                
                <div class="form-group">
                    <label for="messageAll">Message *</label>
                    <textarea id="messageAll" name="message" required></textarea>
                </div>
                
                <button type="submit" class="btn">Send to All Members</button>
            </form>
        </div>
    </div>
</body>
</html>

