package com.library.dao.impl;

import com.library.dao.MemberDAO;
import com.library.model.Member;
import com.library.util.DatabaseConnection;
import com.library.exception.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of MemberDAO
 */
public class MemberDAOImpl implements MemberDAO {
    private final DatabaseConnection dbConnection;
    
    public MemberDAOImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public boolean addMember(Member member) throws DatabaseException {
        String sql = "INSERT INTO members (name, email, password, membership_id, phone, " +
                     "address, registration_date, status, user_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getMembershipId());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getAddress());
            pstmt.setDate(7, member.getRegistrationDate() != null ? 
                         Date.valueOf(member.getRegistrationDate()) : Date.valueOf(LocalDate.now()));
            pstmt.setString(8, member.getStatus());
            pstmt.setString(9, member.getUserType());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error adding member: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateMember(Member member) throws DatabaseException {
        String sql = "UPDATE members SET name=?, email=?, password=?, membership_id=?, " +
                     "phone=?, address=?, status=?, user_type=? WHERE member_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getMembershipId());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getAddress());
            pstmt.setString(7, member.getStatus());
            pstmt.setString(8, member.getUserType());
            pstmt.setInt(9, member.getMemberId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating member: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteMember(int memberId) throws DatabaseException {
        String sql = "DELETE FROM members WHERE member_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting member: " + e.getMessage(), e);
        }
    }
    
    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPassword(rs.getString("password"));
        member.setMembershipId(rs.getString("membership_id"));
        member.setPhone(rs.getString("phone"));
        member.setAddress(rs.getString("address"));
        Date regDate = rs.getDate("registration_date");
        if (regDate != null) {
            member.setRegistrationDate(regDate.toLocalDate());
        }
        member.setStatus(rs.getString("status"));
        member.setUserType(rs.getString("user_type"));
        return member;
    }
    
    @Override
    public Member getMemberById(int memberId) throws DatabaseException {
        String sql = "SELECT * FROM members WHERE member_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMember(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting member by ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Member getMemberByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM members WHERE email=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMember(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting member by email: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Member getMemberByMembershipId(String membershipId) throws DatabaseException {
        String sql = "SELECT * FROM members WHERE membership_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, membershipId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMember(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting member by membership ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Member authenticate(String email, String password) throws DatabaseException {
        String sql = "SELECT * FROM members WHERE email=? AND password=? AND status='ACTIVE'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMember(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error authenticating member: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Member> getAllMembers() throws DatabaseException {
        String sql = "SELECT * FROM members ORDER BY name";
        List<Member> members = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all members: " + e.getMessage(), e);
        }
        
        return members;
    }
    
    @Override
    public List<Member> getAllLibrarians() throws DatabaseException {
        String sql = "SELECT * FROM members WHERE user_type='LIBRARIAN' ORDER BY name";
        List<Member> members = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all librarians: " + e.getMessage(), e);
        }
        
        return members;
    }
    
    @Override
    public List<Member> getAllRegularMembers() throws DatabaseException {
        String sql = "SELECT * FROM members WHERE user_type='MEMBER' ORDER BY name";
        List<Member> members = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all regular members: " + e.getMessage(), e);
        }
        
        return members;
    }
}

