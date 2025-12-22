package com.library.servlet;

import com.library.model.Member;
import com.library.service.NotificationService;
import com.library.service.MemberService;
import com.library.exception.BusinessException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling notification management (Librarian only)
 */
@WebServlet("/librarian/notifications/*")
public class NotificationManagementServlet extends HttpServlet {
    private final NotificationService notificationService;
    private final MemberService memberService;
    
    public NotificationManagementServlet() {
        this.notificationService = new NotificationService();
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
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("")) {
            // Show notification form
            try {
                request.setAttribute("members", memberService.getAllRegularMembers());
                request.getRequestDispatcher("/librarian/notifications.jsp").forward(request, response);
            } catch (BusinessException e) {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/librarian/notifications.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
            if ("send".equals(action)) {
                sendNotification(request, response);
            } else if ("sendToAll".equals(action)) {
                sendNotificationToAll(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            try {
                request.setAttribute("members", memberService.getAllRegularMembers());
                request.getRequestDispatcher("/librarian/notifications.jsp").forward(request, response);
            } catch (BusinessException ex) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    private void sendNotification(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, BusinessException {
        int memberId = Integer.parseInt(request.getParameter("memberId"));
        String notificationType = request.getParameter("notificationType");
        String title = request.getParameter("title");
        String message = request.getParameter("message");
        
        notificationService.sendNotification(memberId, notificationType, title, message);
        request.setAttribute("success", "Notification sent successfully");
        response.sendRedirect(request.getContextPath() + "/librarian/notifications/");
    }
    
    private void sendNotificationToAll(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, BusinessException {
        String notificationType = request.getParameter("notificationType");
        String title = request.getParameter("title");
        String message = request.getParameter("message");
        
        notificationService.sendNotificationToAllMembers(notificationType, title, message);
        request.setAttribute("success", "Notification sent to all members successfully");
        response.sendRedirect(request.getContextPath() + "/librarian/notifications/");
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

