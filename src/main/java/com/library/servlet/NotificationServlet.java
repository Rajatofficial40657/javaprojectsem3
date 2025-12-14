package com.library.servlet;

import com.library.model.Member;
import com.library.model.Notification;
import com.library.service.NotificationService;
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
 * Servlet for handling notifications (Member)
 */
@WebServlet("/member/notifications/*")
public class NotificationServlet extends HttpServlet {
    private final NotificationService notificationService;
    
    public NotificationServlet() {
        this.notificationService = new NotificationService();
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
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("")) {
                // List all notifications
                List<Notification> notifications = notificationService.getNotificationsByMemberId(member.getMemberId());
                List<Notification> unreadNotifications = notificationService.getUnreadNotificationsByMemberId(member.getMemberId());
                
                request.setAttribute("notifications", notifications);
                request.setAttribute("unreadCount", unreadNotifications.size());
            } else if (pathInfo.startsWith("/read/")) {
                // Mark notification as read
                int notificationId = Integer.parseInt(pathInfo.substring(6));
                notificationService.markAsRead(notificationId);
                response.sendRedirect(request.getContextPath() + "/member/notifications/");
                return;
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        } catch (BusinessException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
        }
        
        request.getRequestDispatcher("/member/notifications.jsp").forward(request, response);
    }
}

