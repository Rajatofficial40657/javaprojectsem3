<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Book" %>
<%@ page import="com.library.model.Member" %>
<%@ page import="java.util.List" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    @SuppressWarnings("unchecked")
    List<Book> books = (List<Book>) request.getAttribute("books");
    if (books == null) {
        books = new java.util.ArrayList<>();
    }
    
    String keyword = (String) request.getAttribute("keyword");
    if (keyword == null) keyword = "";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Search Books - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>🔍 Search Books</h1>
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
            <h2>Search Books</h2>
            <form action="<%= request.getContextPath() %>/member/books/search" method="get">
                <div class="form-group">
                    <label for="keyword">Search by title, author, genre, or ISBN:</label>
                    <input type="text" id="keyword" name="keyword" 
                           value="<%= keyword %>" 
                           placeholder="Enter search keyword...">
                </div>
                <button type="submit" class="btn">Search</button>
                <a href="<%= request.getContextPath() %>/member/books/search" class="btn">Show All Available Books</a>
            </form>
        </div>
        
        <div class="card">
            <h2>Search Results</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>ISBN</th>
                        <th>Genre</th>
                        <th>Available Copies</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (books.isEmpty()) { %>
                        <tr>
                            <td colspan="7" style="text-align: center;">No books found</td>
                        </tr>
                    <% } else { %>
                        <% for (Book book : books) { %>
                            <tr>
                                <td><%= book.getTitle() %></td>
                                <td><%= book.getAuthor() %></td>
                                <td><%= book.getIsbn() %></td>
                                <td><%= book.getGenre() != null ? book.getGenre() : "N/A" %></td>
                                <td><%= book.getAvailableCopies() %></td>
                                <td><%= book.getStatus() %></td>
                                <td>
                                    <% if (book.isAvailable()) { %>
                                        <form action="<%= request.getContextPath() %>/member/books/borrow" method="post" style="display: inline;">
                                            <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                                            <button type="submit" class="btn btn-small btn-success"
                                                    onclick="return confirm('Borrow this book?')">Borrow</button>
                                        </form>
                                    <% } else { %>
                                        <span style="color: #999;">Unavailable</span>
                                    <% } %>
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

