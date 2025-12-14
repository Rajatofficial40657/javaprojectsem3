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
    <title>Member Management - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>👥 Member Management</h1>
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
                <h2>Members</h2>
                <a href="<%= request.getContextPath() %>/librarian/members/add" class="btn">Add Member</a>
            </div>
            
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Membership ID</th>
                        <th>Phone</th>
                        <th>User Type</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (members.isEmpty()) { %>
                        <tr>
                            <td colspan="8" style="text-align: center;">No members found</td>
                        </tr>
                    <% } else { %>
                        <% for (Member member : members) { %>
                            <tr>
                                <td><%= member.getMemberId() %></td>
                                <td><%= member.getName() %></td>
                                <td><%= member.getEmail() %></td>
                                <td><%= member.getMembershipId() %></td>
                                <td><%= member.getPhone() != null ? member.getPhone() : "N/A" %></td>
                                <td><%= member.getUserType() %></td>
                                <td><%= member.getStatus() %></td>
                                <td>
                                    <div class="actions">
                                        <a href="<%= request.getContextPath() %>/librarian/members/edit/<%= member.getMemberId() %>" class="btn btn-small">Edit</a>
                                        <a href="<%= request.getContextPath() %>/librarian/members/delete/<%= member.getMemberId() %>" 
                                           class="btn btn-small btn-danger"
                                           onclick="return confirm('Are you sure you want to delete this member?')">Delete</a>
                                    </div>
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

