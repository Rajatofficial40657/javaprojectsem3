package com.library.servlet;

//import com.library.model.Member;
import com.library.service.TransactionService;
import com.library.exception.BusinessException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling book return (Member)
 */
@WebServlet("/member/books/return")
public class BookReturnServlet extends HttpServlet {
    private final TransactionService transactionService;
    
    public BookReturnServlet() {
        this.transactionService = new TransactionService();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            int transactionId = Integer.parseInt(request.getParameter("transactionId"));
            transactionService.returnBook(transactionId);
            request.setAttribute("success", "Book returned successfully");
            response.sendRedirect(request.getContextPath() + "/member/history");
        } catch (BusinessException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/member/history");
        }
    }
}

