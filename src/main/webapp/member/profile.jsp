<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    Member member = (Member) request.getAttribute("member");
    if (member == null) {
        member = user;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Profile - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>👤 Profile</h1>
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
            <h2>Profile Information</h2>
            <form action="<%= request.getContextPath() %>/member/profile/" method="post">
                <div class="form-group">
                    <label for="name">Name *</label>
                    <input type="text" id="name" name="name" 
                           value="<%= member.getName() %>" required>
                </div>
                
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" value="<%= member.getEmail() %>" disabled>
                    <small style="color: #666;">Email cannot be changed</small>
                </div>
                
                <div class="form-group">
                    <label for="membershipId">Membership ID</label>
                    <input type="text" id="membershipId" value="<%= member.getMembershipId() %>" disabled>
                    <small style="color: #666;">Membership ID cannot be changed</small>
                </div>
                
                <div class="form-group">
                    <label for="phone">Phone</label>
                    <input type="text" id="phone" name="phone" 
                           value="<%= member.getPhone() != null ? member.getPhone() : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="address">Address</label>
                    <textarea id="address" name="address"><%= member.getAddress() != null ? member.getAddress() : "" %></textarea>
                </div>
                
                <div class="form-group">
                    <label for="newPassword">New Password (leave blank to keep current)</label>
                    <input type="password" id="newPassword" name="newPassword">
                </div>
                
                <button type="submit" class="btn">Update Profile</button>
            </form>
        </div>
    </div>
</body>
</html>

