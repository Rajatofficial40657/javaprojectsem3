package com.library.servlet;

import com.library.model.Book;
import com.library.model.Member;
import com.library.service.BookService;
import com.library.exception.BusinessException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
//import java.time.LocalDate;
import java.util.List;

/**
 * Servlet for handling book management (Librarian only)
 */
@WebServlet("/librarian/books/*")
public class BookManagementServlet extends HttpServlet {
    private final BookService bookService;
    
    public BookManagementServlet() {
        this.bookService = new BookService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check authentication
        if (!isLibrarian(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("")) {
                // List all books
                List<Book> books = bookService.getAllBooks();
                request.setAttribute("books", books);
                request.getRequestDispatcher("/librarian/books.jsp").forward(request, response);
            } else if (pathInfo.equals("/add")) {
                // Show add book form
                request.getRequestDispatcher("/librarian/book-form.jsp").forward(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                // Show edit book form
                int bookId = Integer.parseInt(pathInfo.substring(6));
                Book book = bookService.getBookById(bookId);
                request.setAttribute("book", book);
                request.setAttribute("edit", true);
                request.getRequestDispatcher("/librarian/book-form.jsp").forward(request, response);
            } else if (pathInfo.startsWith("/delete/")) {
                // Delete book
                int bookId = Integer.parseInt(pathInfo.substring(8));
                bookService.deleteBook(bookId);
                request.setAttribute("success", "Book deleted successfully");
                List<Book> books = bookService.getAllBooks();
                request.setAttribute("books", books);
                request.getRequestDispatcher("/librarian/books.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (BusinessException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
            try {
                List<Book> books = bookService.getAllBooks();
                request.setAttribute("books", books);
                request.getRequestDispatcher("/librarian/books.jsp").forward(request, response);
            } catch (BusinessException ex) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check authentication
        if (!isLibrarian(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("add".equals(action)) {
                addBook(request, response);
            } else if ("update".equals(action)) {
                updateBook(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/librarian/book-form.jsp").forward(request, response);
        }
    }
    
    private void addBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, BusinessException {
        Book book = createBookFromRequest(request);
        bookService.addBook(book);
        request.setAttribute("success", "Book added successfully");
        response.sendRedirect(request.getContextPath() + "/librarian/books/");
    }
    
    private void updateBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, BusinessException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        Book book = createBookFromRequest(request);
        book.setBookId(bookId);
        bookService.updateBook(book);
        request.setAttribute("success", "Book updated successfully");
        response.sendRedirect(request.getContextPath() + "/librarian/books/");
    }
    
    private Book createBookFromRequest(HttpServletRequest request) {
        Book book = new Book();
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        book.setIsbn(request.getParameter("isbn"));
        book.setGenre(request.getParameter("genre"));
        book.setPublisher(request.getParameter("publisher"));
        
        String publicationDate = request.getParameter("publicationDate");
        if (publicationDate != null && !publicationDate.isEmpty()) {
            book.setPublicationDateString(publicationDate);
        }
        
        String totalCopies = request.getParameter("totalCopies");
        if (totalCopies != null && !totalCopies.isEmpty()) {
            book.setTotalCopies(Integer.parseInt(totalCopies));
        }
        
        String availableCopies = request.getParameter("availableCopies");
        if (availableCopies != null && !availableCopies.isEmpty()) {
            book.setAvailableCopies(Integer.parseInt(availableCopies));
        }
        
        book.setStatus(request.getParameter("status"));
        book.setDescription(request.getParameter("description"));
        
        return book;
    }
    
    private boolean isLibrarian(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Member user = (Member) session.getAttribute("user");
        return user != null && user.isLibrarian();
    }
}

