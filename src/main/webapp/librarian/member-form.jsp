<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Member" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null || !user.isLibrarian()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    Member member = (Member) request.getAttribute("member");
    boolean isEdit = member != null && request.getAttribute("edit") != null;
%>
<!DOCTYPE html>
<html>
<head>
    <title><%= isEdit ? "Edit" : "Add" %> Member - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>👥 <%= isEdit ? "Edit" : "Add" %> Member</h1>
        <nav>
            <a href="<%= request.getContextPath() %>/librarian/dashboard.jsp">Dashboard</a>
            <a href="<%= request.getContextPath() %>/librarian/members/">Members</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </nav>
    </div>
    
    <div class="container">
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        
        <div class="card">
            <h2><%= isEdit ? "Edit" : "Add" %> Member</h2>
            <form action="<%= request.getContextPath() %>/librarian/members/" method="post">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "add" %>">
                <% if (isEdit) { %>
                    <input type="hidden" name="memberId" value="<%= member.getMemberId() %>">
                <% } %>
                
                <div class="form-group">
                    <label for="name">Name *</label>
                    <input type="text" id="name" name="name" 
                           value="<%= member != null ? member.getName() : "" %>" required>
                </div>
                
                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" id="email" name="email" 
                           value="<%= member != null ? member.getEmail() : "" %>" required>
                </div>
                
                <div class="form-group">
                    <label for="password">Password *</label>
                    <input type="password" id="password" name="password" 
                           value="<%= member != null ? member.getPassword() : "" %>" required>
                </div>
                
                <div class="form-group">
                    <label for="membershipId">Membership ID *</label>
                    <input type="text" id="membershipId" name="membershipId" 
                           value="<%= member != null ? member.getMembershipId() : "" %>" required>
                </div>
                
                <div class="form-group">
                    <label for="phone">Phone</label>
                    <input type="text" id="phone" name="phone" 
                           value="<%= member != null && member.getPhone() != null ? member.getPhone() : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="address">Address</label>
                    <textarea id="address" name="address"><%= member != null && member.getAddress() != null ? member.getAddress() : "" %></textarea>
                </div>
                
                <div class="form-group">
                    <label for="userType">User Type</label>
                    <select id="userType" name="userType">
                        <option value="MEMBER" <%= member != null && "MEMBER".equals(member.getUserType()) ? "selected" : "" %>>Member</option>
                        <option value="LIBRARIAN" <%= member != null && "LIBRARIAN".equals(member.getUserType()) ? "selected" : "" %>>Librarian</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" name="status">
                        <option value="ACTIVE" <%= member != null && "ACTIVE".equals(member.getStatus()) ? "selected" : "" %>>Active</option>
                        <option value="INACTIVE" <%= member != null && "INACTIVE".equals(member.getStatus()) ? "selected" : "" %>>Inactive</option>
                    </select>
                </div>
                
                <button type="submit" class="btn"><%= isEdit ? "Update" : "Add" %> Member</button>
                <a href="<%= request.getContextPath() %>/librarian/members/" class="btn btn-danger">Cancel</a>
            </form>
        </div>
    </div>
</body>
</html>

