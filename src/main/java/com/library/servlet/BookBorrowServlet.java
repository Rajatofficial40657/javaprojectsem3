package com.library.servlet;

import com.library.model.Member;
import com.library.model.Transaction;
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
 * Servlet for handling book borrowing (Member)
 */
@WebServlet("/member/books/borrow")
public class BookBorrowServlet extends HttpServlet {
    private final TransactionService transactionService;
    
    public BookBorrowServlet() {
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
        
        Member member = (Member) session.getAttribute("user");
        
        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int loanDays = 14; // Default 14 days
            
            String loanDaysParam = request.getParameter("loanDays");
            if (loanDaysParam != null && !loanDaysParam.isEmpty()) {
                loanDays = Integer.parseInt(loanDaysParam);
            }
            
            Transaction transaction = transactionService.borrowBook(bookId, member.getMemberId(), loanDays);
            request.setAttribute("success", "Book borrowed successfully. Due date: " + 
                transaction.getDueDate());
            response.sendRedirect(request.getContextPath() + "/member/books/search?keyword=");
        } catch (BusinessException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/member/books/search?keyword=");
        }
    }
}

