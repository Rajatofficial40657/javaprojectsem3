package com.library.servlet;

import com.library.model.Member;
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
 * Servlet for handling login authentication
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final MemberService memberService;
    
    public LoginServlet() {
        this.memberService = new MemberService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            Member member = (Member) session.getAttribute("user");
            if (member.isLibrarian()) {
                response.sendRedirect(request.getContextPath() + "/librarian/dashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/member/dashboard.jsp");
            }
            return;
        }
        
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try {
            Member member = memberService.authenticate(email, password);
            
            if (member != null) {
                // Create session
                HttpSession session = request.getSession();
                session.setAttribute("user", member);
                session.setAttribute("userId", member.getMemberId());
                session.setAttribute("userType", member.getUserType());
                session.setAttribute("userName", member.getName());
                
                // Redirect based on user type
                if (member.isLibrarian()) {
                    response.sendRedirect(request.getContextPath() + "/librarian/dashboard.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/member/dashboard.jsp");
                }
            } else {
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}

