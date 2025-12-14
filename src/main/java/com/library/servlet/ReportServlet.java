package com.library.servlet;

import com.library.model.Member;
import com.library.service.ReportService;
import com.library.exception.BusinessException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Servlet for handling reports (Librarian only)
 * Demonstrates multithreading for report generation
 */
@WebServlet("/librarian/reports/*")
public class ReportServlet extends HttpServlet {
    private final ReportService reportService;
    
    public ReportServlet() {
        this.reportService = new ReportService();
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
                // Show reports dashboard
                request.getRequestDispatcher("/librarian/reports.jsp").forward(request, response);
            } else if (pathInfo.equals("/inventory")) {
                // Generate inventory report (async)
                CompletableFuture<Map<String, Object>> future = reportService.generateInventoryReport();
                Map<String, Object> report = future.join(); // Wait for completion
                request.setAttribute("report", report);
                request.setAttribute("reportType", "inventory");
                request.getRequestDispatcher("/librarian/report-view.jsp").forward(request, response);
            } else if (pathInfo.equals("/trends")) {
                // Generate borrowing trends report (async)
                LocalDate startDate = LocalDate.now().minusMonths(1);
                LocalDate endDate = LocalDate.now();
                
                String startDateParam = request.getParameter("startDate");
                String endDateParam = request.getParameter("endDate");
                
                if (startDateParam != null && !startDateParam.isEmpty()) {
                    startDate = LocalDate.parse(startDateParam);
                }
                if (endDateParam != null && !endDateParam.isEmpty()) {
                    endDate = LocalDate.parse(endDateParam);
                }
                
                CompletableFuture<Map<String, Object>> future = 
                    reportService.generateBorrowingTrendsReport(startDate, endDate);
                Map<String, Object> report = future.join(); // Wait for completion
                request.setAttribute("report", report);
                request.setAttribute("reportType", "trends");
                request.getRequestDispatcher("/librarian/report-view.jsp").forward(request, response);
            } else if (pathInfo.equals("/overdue")) {
                // Generate overdue books report
                Map<String, Object> report = reportService.generateOverdueBooksReport();
                request.setAttribute("report", report);
                request.setAttribute("reportType", "overdue");
                request.getRequestDispatcher("/librarian/report-view.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/librarian/reports.jsp").forward(request, response);
        }
    }
    
    private boolean isLibrarian(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Member user = (Member) session.getAttribute("user");
        return user != null && user.isLibrarian();
    }
    
    @Override
    public void destroy() {
        reportService.shutdown();
        super.destroy();
    }
}

