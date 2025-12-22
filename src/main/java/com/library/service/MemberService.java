package com.library.service;

import com.library.model.Member;
import com.library.dao.MemberDAO;
import com.library.dao.impl.MemberDAOImpl;
import com.library.exception.DatabaseException;
import com.library.exception.BusinessException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for Member operations
 * Demonstrates Collections and Generics usage
 */
public class MemberService {
    private final MemberDAO memberDAO;
    
    public MemberService() {
        this.memberDAO = new MemberDAOImpl();
    }
    
    /**
     * Add a new member
     */
    public boolean addMember(Member member) throws BusinessException {
        try {
            validateMember(member);
            
            // Check if email already exists
            Member existingMember = memberDAO.getMemberByEmail(member.getEmail());
            if (existingMember != null) {
                throw new BusinessException("Member with email " + member.getEmail() + " already exists");
            }
            
            // Check if membership ID already exists
            Member existingByMembershipId = memberDAO.getMemberByMembershipId(member.getMembershipId());
            if (existingByMembershipId != null) {
                throw new BusinessException("Membership ID " + member.getMembershipId() + " already exists");
            }
            
            return memberDAO.addMember(member);
        } catch (DatabaseException e) {
            throw new BusinessException("Error adding member: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update a member
     */
    public boolean updateMember(Member member) throws BusinessException {
        try {
            validateMember(member);
            
            Member existingMember = memberDAO.getMemberById(member.getMemberId());
            if (existingMember == null) {
                throw new BusinessException("Member not found");
            }
            
            // Check email uniqueness if changed
            if (!existingMember.getEmail().equals(member.getEmail())) {
                Member memberWithEmail = memberDAO.getMemberByEmail(member.getEmail());
                if (memberWithEmail != null && memberWithEmail.getMemberId() != member.getMemberId()) {
                    throw new BusinessException("Email already exists for another member");
                }
            }
            
            return memberDAO.updateMember(member);
        } catch (DatabaseException e) {
            throw new BusinessException("Error updating member: " + e.getMessage(), e);
        }
    }
    
    /**
     * Delete a member
     */
    public boolean deleteMember(int memberId) throws BusinessException {
        try {
            Member member = memberDAO.getMemberById(memberId);
            if (member == null) {
                throw new BusinessException("Member not found");
            }
            
            return memberDAO.deleteMember(memberId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error deleting member: " + e.getMessage(), e);
        }
    }
    
    /**
     * Authenticate member
     */
    public Member authenticate(String email, String password) throws BusinessException {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new BusinessException("Email is required");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new BusinessException("Password is required");
            }
            
            Member member = memberDAO.authenticate(email.trim(), password);
            if (member == null) {
                throw new BusinessException("Invalid email or password");
            }
            
            return member;
        } catch (DatabaseException e) {
            throw new BusinessException("Error authenticating member: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all members
     */
    public List<Member> getAllMembers() throws BusinessException {
        try {
            return memberDAO.getAllMembers();
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting members: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all regular members (excluding librarians)
     */
    public List<Member> getAllRegularMembers() throws BusinessException {
        try {
            return memberDAO.getAllRegularMembers();
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting regular members: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get members grouped by status
     * Demonstrates Collections (Map) and Streams
     */
    public Map<String, List<Member>> getMembersByStatus() throws BusinessException {
        try {
            List<Member> allMembers = memberDAO.getAllMembers();
            
            return allMembers.stream()
                    .filter(member -> member.getStatus() != null)
                    .collect(Collectors.groupingBy(
                        Member::getStatus,
                        Collectors.toList()
                    ));
        } catch (DatabaseException e) {
            throw new BusinessException("Error grouping members by status: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get member by ID
     */
    public Member getMemberById(int memberId) throws BusinessException {
        try {
            Member member = memberDAO.getMemberById(memberId);
            if (member == null) {
                throw new BusinessException("Member not found");
            }
            return member;
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting member: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate member data
     */
    private void validateMember(Member member) throws BusinessException {
        if (member == null) {
            throw new BusinessException("Member cannot be null");
        }
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new BusinessException("Member name is required");
        }
        if (member.getEmail() == null || member.getEmail().trim().isEmpty()) {
            throw new BusinessException("Member email is required");
        }
        if (!isValidEmail(member.getEmail())) {
            throw new BusinessException("Invalid email format");
        }
        if (member.getPassword() == null || member.getPassword().trim().isEmpty()) {
            throw new BusinessException("Password is required");
        }
        if (member.getMembershipId() == null || member.getMembershipId().trim().isEmpty()) {
            throw new BusinessException("Membership ID is required");
        }
    }
    
    /**
     * Simple email validation
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}

