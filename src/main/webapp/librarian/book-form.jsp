<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.library.model.Book" %>
<%@ page import="com.library.model.Member" %>
<%
    Member user = (Member) session.getAttribute("user");
    if (user == null || !user.isLibrarian()) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    Book book = (Book) request.getAttribute("book");
    boolean isEdit = book != null && request.getAttribute("edit") != null;
%>
<!DOCTYPE html>
<html>
<head>
    <title><%= isEdit ? "Edit" : "Add" %> Book - Library Management System</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
    <div class="header">
        <h1>📚 <%= isEdit ? "Edit" : "Add" %> Book</h1>
        <nav>
            <a href="<%= request.getContextPath() %>/librarian/dashboard.jsp">Dashboard</a>
            <a href="<%= request.getContextPath() %>/librarian/books/">Books</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </nav>
    </div>
    
    <div class="container">
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        
        <div class="card">
            <h2><%= isEdit ? "Edit" : "Add" %> Book</h2>
            <form action="<%= request.getContextPath() %>/librarian/books/" method="post">
                <input type="hidden" name="action" value="<%= isEdit ? "update" : "add" %>">
                <% if (isEdit) { %>
                    <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                <% } %>
                
                <div class="form-group">
                    <label for="title">Title *</label>
                    <input type="text" id="title" name="title" 
                           value="<%= book != null ? book.getTitle() : "" %>" required>
                </div>
                
                <div class="form-group">
                    <label for="author">Author *</label>
                    <input type="text" id="author" name="author" 
                           value="<%= book != null ? book.getAuthor() : "" %>" required>
                </div>
                
                <div class="form-group">
                    <label for="isbn">ISBN *</label>
                    <input type="text" id="isbn" name="isbn" 
                           value="<%= book != null ? book.getIsbn() : "" %>" required>
                </div>
                
                <div class="form-group">
                    <label for="genre">Genre</label>
                    <input type="text" id="genre" name="genre" 
                           value="<%= book != null && book.getGenre() != null ? book.getGenre() : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="publisher">Publisher</label>
                    <input type="text" id="publisher" name="publisher" 
                           value="<%= book != null && book.getPublisher() != null ? book.getPublisher() : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="publicationDate">Publication Date</label>
                    <input type="date" id="publicationDate" name="publicationDate" 
                           value="<%= book != null && book.getPublicationDateString() != null ? book.getPublicationDateString() : "" %>">
                </div>
                
                <div class="form-group">
                    <label for="totalCopies">Total Copies *</label>
                    <input type="number" id="totalCopies" name="totalCopies" min="1" 
                           value="<%= book != null ? book.getTotalCopies() : "1" %>" required>
                </div>
                
                <div class="form-group">
                    <label for="availableCopies">Available Copies *</label>
                    <input type="number" id="availableCopies" name="availableCopies" min="0" 
                           value="<%= book != null ? book.getAvailableCopies() : "1" %>" required>
                </div>
                
                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" name="status">
                        <option value="AVAILABLE" <%= book != null && "AVAILABLE".equals(book.getStatus()) ? "selected" : "" %>>Available</option>
                        <option value="UNAVAILABLE" <%= book != null && "UNAVAILABLE".equals(book.getStatus()) ? "selected" : "" %>>Unavailable</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description"><%= book != null && book.getDescription() != null ? book.getDescription() : "" %></textarea>
                </div>
                
                <button type="submit" class="btn"><%= isEdit ? "Update" : "Add" %> Book</button>
                <a href="<%= request.getContextPath() %>/librarian/books/" class="btn btn-danger">Cancel</a>
            </form>
        </div>
    </div>
</body>
</html>

