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
import java.util.List;

/**
 * Servlet for handling borrowing history (Member)
 */
@WebServlet("/member/history")
public class HistoryServlet extends HttpServlet {
    private final TransactionService transactionService;
    
    public HistoryServlet() {
        this.transactionService = new TransactionService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        Member member = (Member) session.getAttribute("user");
        
        try {
            List<Transaction> allTransactions = transactionService.getTransactionsByMemberId(member.getMemberId());
            List<Transaction> activeTransactions = transactionService.getActiveTransactionsByMemberId(member.getMemberId());
            
            request.setAttribute("allTransactions", allTransactions);
            request.setAttribute("activeTransactions", activeTransactions);
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
        }
        
        request.getRequestDispatcher("/member/history.jsp").forward(request, response);
    }
}

