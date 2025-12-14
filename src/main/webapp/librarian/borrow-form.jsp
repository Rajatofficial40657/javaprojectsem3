<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Book" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="com.library.model.Member as LibMember" %>
<%@ page import="java.util.List" %>
<%
    LibMember user = (LibMember) session.getAttribute("user");
    if (user == null || !user.isLibrarian()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    @SuppressWarnings("unchecked")
    List<Book> books = (List<Book>) request.getAttribute("books");
    @SuppressWarnings("unchecked")
    List<Member> members = (List<Member>) request.getAttribute("members");
    
    if (books == null) books = new java.util.ArrayList<>();
    if (members == null) members = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Borrow Book - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>📖 Borrow Book</h1>
        <nav>
            <a href="<%= request.getContextPath() %>/librarian/dashboard.jsp">Dashboard</a>
            <a href="<%= request.getContextPath() %>/librarian/transactions/">Transactions</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </nav>
    </div>
    
    <div class="container">
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        
        <div class="card">
            <h2>Borrow Book</h2>
            <form action="<%= request.getContextPath() %>/librarian/transactions/" method="post">
                <input type="hidden" name="action" value="borrow">
                
                <div class="form-group">
                    <label for="bookId">Book *</label>
                    <select id="bookId" name="bookId" required>
                        <option value="">Select a book</option>
                        <% for (Book book : books) { %>
                            <option value="<%= book.getBookId() %>">
                                <%= book.getTitle() %> by <%= book.getAuthor() %> 
                                (<%= book.getAvailableCopies() %> available)
                            </option>
                        <% } %>
                    </select>
                </div>
                
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
                    <label for="loanDays">Loan Period (days)</label>
                    <input type="number" id="loanDays" name="loanDays" value="14" min="1" max="30">
                </div>
                
                <button type="submit" class="btn">Borrow Book</button>
                <a href="<%= request.getContextPath() %>/librarian/transactions/" class="btn btn-danger">Cancel</a>
            </form>
        </div>
    </div>
</body>
</html>

