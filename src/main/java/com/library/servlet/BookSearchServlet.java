package com.library.servlet;

//import com.library.model.Member;
import com.library.model.Book;
import com.library.service.BookService;
import com.library.exception.BusinessException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for handling book search (Member)
 */
@WebServlet("/member/books/search")
public class BookSearchServlet extends HttpServlet {
    private final BookService bookService;
    
    public BookSearchServlet() {
        this.bookService = new BookService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check authentication
        if (!isAuthenticated(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String keyword = request.getParameter("keyword");
        List<Book> books;
        
        try {
            if (keyword != null && !keyword.trim().isEmpty()) {
                books = bookService.searchBooks(keyword);
            } else {
                books = bookService.getAvailableBooks();
            }
            request.setAttribute("books", books);
            request.setAttribute("keyword", keyword);
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
        }
        
        request.getRequestDispatcher("/member/search.jsp").forward(request, response);
    }
    
    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }
}

