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
 * Servlet for handling profile management (Member)
 */
@WebServlet("/member/profile/*")
public class ProfileServlet extends HttpServlet {
    private final MemberService memberService;
    
    public ProfileServlet() {
        this.memberService = new MemberService();
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
            Member updatedMember = memberService.getMemberById(member.getMemberId());
            request.setAttribute("member", updatedMember);
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
        }
        
        request.getRequestDispatcher("/member/profile.jsp").forward(request, response);
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
            member.setName(request.getParameter("name"));
            member.setPhone(request.getParameter("phone"));
            member.setAddress(request.getParameter("address"));
            
            String newPassword = request.getParameter("newPassword");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                member.setPassword(newPassword);
            }
            
            memberService.updateMember(member);
            
            // Update session
            Member updatedMember = memberService.getMemberById(member.getMemberId());
            session.setAttribute("user", updatedMember);
            session.setAttribute("userName", updatedMember.getName());
            
            request.setAttribute("success", "Profile updated successfully");
            request.setAttribute("member", updatedMember);
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            try {
                request.setAttribute("member", memberService.getMemberById(member.getMemberId()));
            } catch (BusinessException ex) {
                // Ignore
            }
        }
        
        request.getRequestDispatcher("/member/profile.jsp").forward(request, response);
    }
}

