<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Book" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="java.util.List" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null || !user.isLibrarian()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    @SuppressWarnings("unchecked")
    List<Book> books = (List<Book>) request.getAttribute("books");
    if (books == null) {
        books = new java.util.ArrayList<>();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Book Management - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>📚 Book Management</h1>
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
                <h2>Books</h2>
                <a href="<%= request.getContextPath() %>/librarian/books/add" class="btn">Add Book</a>
            </div>
            
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Author</th>
                        <th>ISBN</th>
                        <th>Genre</th>
                        <th>Total Copies</th>
                        <th>Available</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (books.isEmpty()) { %>
                        <tr>
                            <td colspan="9" style="text-align: center;">No books found</td>
                        </tr>
                    <% } else { %>
                        <% for (Book book : books) { %>
                            <tr>
                                <td><%= book.getBookId() %></td>
                                <td><%= book.getTitle() %></td>
                                <td><%= book.getAuthor() %></td>
                                <td><%= book.getIsbn() %></td>
                                <td><%= book.getGenre() != null ? book.getGenre() : "N/A" %></td>
                                <td><%= book.getTotalCopies() %></td>
                                <td><%= book.getAvailableCopies() %></td>
                                <td><%= book.getStatus() %></td>
                                <td>
                                    <div class="actions">
                                        <a href="<%= request.getContextPath() %>/librarian/books/edit/<%= book.getBookId() %>" class="btn btn-small">Edit</a>
                                        <a href="<%= request.getContextPath() %>/librarian/books/delete/<%= book.getBookId() %>" 
                                           class="btn btn-small btn-danger"
                                           onclick="return confirm('Are you sure you want to delete this book?')">Delete</a>
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

