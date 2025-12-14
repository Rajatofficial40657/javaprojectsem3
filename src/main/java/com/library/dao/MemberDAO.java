package com.library.dao;

import com.library.model.Member;
import com.library.exception.DatabaseException;
import java.util.List;

/**
 * DAO interface for Member operations
 */
public interface MemberDAO {
    /**
     * Add a new member to the database
     */
    boolean addMember(Member member) throws DatabaseException;
    
    /**
     * Update an existing member
     */
    boolean updateMember(Member member) throws DatabaseException;
    
    /**
     * Delete a member by ID
     */
    boolean deleteMember(int memberId) throws DatabaseException;
    
    /**
     * Get a member by ID
     */
    Member getMemberById(int memberId) throws DatabaseException;
    
    /**
     * Get a member by email
     */
    Member getMemberByEmail(String email) throws DatabaseException;
    
    /**
     * Get a member by membership ID
     */
    Member getMemberByMembershipId(String membershipId) throws DatabaseException;
    
    /**
     * Authenticate member by email and password
     */
    Member authenticate(String email, String password) throws DatabaseException;
    
    /**
     * Get all members
     */
    List<Member> getAllMembers() throws DatabaseException;
    
    /**
     * Get all librarians
     */
    List<Member> getAllLibrarians() throws DatabaseException;
    
    /**
     * Get all regular members
     */
    List<Member> getAllRegularMembers() throws DatabaseException;
}

