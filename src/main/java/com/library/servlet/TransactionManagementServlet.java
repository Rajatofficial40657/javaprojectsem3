package com.library.servlet;

import com.library.model.Member;
import com.library.model.Transaction;
import com.library.service.TransactionService;
import com.library.service.BookService;
import com.library.service.MemberService;
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
 * Servlet for handling transaction management (Librarian only)
 */
@WebServlet("/librarian/transactions/*")
public class TransactionManagementServlet extends HttpServlet {
    private final TransactionService transactionService;
    private final BookService bookService;
    private final MemberService memberService;
    
    public TransactionManagementServlet() {
        this.transactionService = new TransactionService();
        this.bookService = new BookService();
        this.memberService = new MemberService();
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
                // List all transactions
                List<Transaction> transactions = transactionService.getAllTransactions();
                request.setAttribute("transactions", transactions);
                request.getRequestDispatcher("/librarian/transactions.jsp").forward(request, response);
            } else if (pathInfo.equals("/borrow")) {
                // Show borrow book form
                request.setAttribute("books", bookService.getAvailableBooks());
                request.setAttribute("members", memberService.getAllRegularMembers());
                request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
            } else if (pathInfo.startsWith("/return/")) {
                // Return book
                int transactionId = Integer.parseInt(pathInfo.substring(8));
                transactionService.returnBook(transactionId);
                request.setAttribute("success", "Book returned successfully");
                List<Transaction> transactions = transactionService.getAllTransactions();
                request.setAttribute("transactions", transactions);
                request.getRequestDispatcher("/librarian/transactions.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (BusinessException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
            try {
                List<Transaction> transactions = transactionService.getAllTransactions();
                request.setAttribute("transactions", transactions);
                request.getRequestDispatcher("/librarian/transactions.jsp").forward(request, response);
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
            if ("borrow".equals(action)) {
                borrowBook(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            try {
                request.setAttribute("books", bookService.getAvailableBooks());
                request.setAttribute("members", memberService.getAllRegularMembers());
                request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
            } catch (BusinessException ex) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    private void borrowBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, BusinessException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int memberId = Integer.parseInt(request.getParameter("memberId"));
        int loanDays = 14; // Default 14 days
        
        String loanDaysParam = request.getParameter("loanDays");
        if (loanDaysParam != null && !loanDaysParam.isEmpty()) {
            loanDays = Integer.parseInt(loanDaysParam);
        }
        
        transactionService.borrowBook(bookId, memberId, loanDays);
        request.setAttribute("success", "Book borrowed successfully");
        response.sendRedirect(request.getContextPath() + "/librarian/transactions/");
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

