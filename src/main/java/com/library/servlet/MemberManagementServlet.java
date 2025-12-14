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
//import java.time.LocalDate;
import java.util.List;

/**
 * Servlet for handling member management (Librarian only)
 */
@WebServlet("/librarian/members/*")
public class MemberManagementServlet extends HttpServlet {
    private final MemberService memberService;
    
    public MemberManagementServlet() {
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
                // List all members
                List<Member> members = memberService.getAllMembers();
                request.setAttribute("members", members);
                request.getRequestDispatcher("/librarian/members.jsp").forward(request, response);
            } else if (pathInfo.equals("/add")) {
                // Show add member form
                request.getRequestDispatcher("/librarian/member-form.jsp").forward(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                // Show edit member form
                int memberId = Integer.parseInt(pathInfo.substring(6));
                Member member = memberService.getMemberById(memberId);
                request.setAttribute("member", member);
                request.setAttribute("edit", true);
                request.getRequestDispatcher("/librarian/member-form.jsp").forward(request, response);
            } else if (pathInfo.startsWith("/delete/")) {
                // Delete member
                int memberId = Integer.parseInt(pathInfo.substring(8));
                memberService.deleteMember(memberId);
                request.setAttribute("success", "Member deleted successfully");
                List<Member> members = memberService.getAllMembers();
                request.setAttribute("members", members);
                request.getRequestDispatcher("/librarian/members.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (BusinessException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
            try {
                List<Member> members = memberService.getAllMembers();
                request.setAttribute("members", members);
                request.getRequestDispatcher("/librarian/members.jsp").forward(request, response);
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
            if ("add".equals(action)) {
                addMember(request, response);
            } else if ("update".equals(action)) {
                updateMember(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (BusinessException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/librarian/member-form.jsp").forward(request, response);
        }
    }
    
    private void addMember(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, BusinessException {
        Member member = createMemberFromRequest(request);
        memberService.addMember(member);
        request.setAttribute("success", "Member added successfully");
        response.sendRedirect(request.getContextPath() + "/librarian/members/");
    }
    
    private void updateMember(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, BusinessException {
        int memberId = Integer.parseInt(request.getParameter("memberId"));
        Member member = createMemberFromRequest(request);
        member.setMemberId(memberId);
        memberService.updateMember(member);
        request.setAttribute("success", "Member updated successfully");
        response.sendRedirect(request.getContextPath() + "/librarian/members/");
    }
    
    private Member createMemberFromRequest(HttpServletRequest request) {
        Member member = new Member();
        member.setName(request.getParameter("name"));
        member.setEmail(request.getParameter("email"));
        member.setPassword(request.getParameter("password"));
        member.setMembershipId(request.getParameter("membershipId"));
        member.setPhone(request.getParameter("phone"));
        member.setAddress(request.getParameter("address"));
        member.setStatus(request.getParameter("status"));
        member.setUserType(request.getParameter("userType"));
        
        return member;
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

