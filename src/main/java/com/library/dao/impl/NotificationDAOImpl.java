package com.library.dao.impl;

import com.library.dao.NotificationDAO;
import com.library.model.Notification;
import com.library.model.Member;
import com.library.dao.MemberDAO;
import com.library.util.DatabaseConnection;
import com.library.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of NotificationDAO
 */
public class NotificationDAOImpl implements NotificationDAO {
    private final DatabaseConnection dbConnection;
    private final MemberDAO memberDAO;
    
    public NotificationDAOImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.memberDAO = new MemberDAOImpl();
    }
    
    @Override
    public boolean addNotification(Notification notification) throws DatabaseException {
        String sql = "INSERT INTO notifications (member_id, notification_type, title, message, is_read) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (notification.getMemberId() != null) {
                pstmt.setInt(1, notification.getMemberId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setString(2, notification.getNotificationType());
            pstmt.setString(3, notification.getTitle());
            pstmt.setString(4, notification.getMessage());
            pstmt.setBoolean(5, notification.isRead());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    notification.setNotificationId(generatedKeys.getInt(1));
                }
            }
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error adding notification: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateNotification(Notification notification) throws DatabaseException {
        String sql = "UPDATE notifications SET member_id=?, notification_type=?, title=?, message=?, is_read=? " +
                     "WHERE notification_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (notification.getMemberId() != null) {
                pstmt.setInt(1, notification.getMemberId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setString(2, notification.getNotificationType());
            pstmt.setString(3, notification.getTitle());
            pstmt.setString(4, notification.getMessage());
            pstmt.setBoolean(5, notification.isRead());
            pstmt.setInt(6, notification.getNotificationId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating notification: " + e.getMessage(), e);
        }
    }
    
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException, DatabaseException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getInt("notification_id"));
        
        int memberId = rs.getInt("member_id");
        if (!rs.wasNull()) {
            notification.setMemberId(memberId);
        }
        
        notification.setNotificationType(rs.getString("notification_type"));
        notification.setTitle(rs.getString("title"));
        notification.setMessage(rs.getString("message"));
        notification.setRead(rs.getBoolean("is_read"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            notification.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        // Load associated member if memberId exists
        if (memberId > 0) {
            try {
                Member member = memberDAO.getMemberById(memberId);
                notification.setMember(member);
            } catch (DatabaseException e) {
                // Log but don't fail if member can't be loaded
                System.err.println("Warning: Could not load associated member: " + e.getMessage());
            }
        }
        
        return notification;
    }
    
    @Override
    public Notification getNotificationById(int notificationId) throws DatabaseException {
        String sql = "SELECT * FROM notifications WHERE notification_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, notificationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToNotification(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting notification by ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Notification> getAllNotifications() throws DatabaseException {
        String sql = "SELECT * FROM notifications ORDER BY created_at DESC";
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all notifications: " + e.getMessage(), e);
        }
        
        return notifications;
    }
    
    @Override
    public List<Notification> getNotificationsByMemberId(int memberId) throws DatabaseException {
        String sql = "SELECT * FROM notifications WHERE member_id=? ORDER BY created_at DESC";
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting notifications by member ID: " + e.getMessage(), e);
        }
        
        return notifications;
    }
    
    @Override
    public List<Notification> getUnreadNotificationsByMemberId(int memberId) throws DatabaseException {
        String sql = "SELECT * FROM notifications WHERE member_id=? AND is_read=false ORDER BY created_at DESC";
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting unread notifications: " + e.getMessage(), e);
        }
        
        return notifications;
    }
    
    @Override
    public boolean markAsRead(int notificationId) throws DatabaseException {
        String sql = "UPDATE notifications SET is_read=true WHERE notification_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, notificationId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error marking notification as read: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteNotification(int notificationId) throws DatabaseException {
        String sql = "DELETE FROM notifications WHERE notification_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, notificationId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting notification: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Notification> getNotificationsByType(String notificationType) throws DatabaseException {
        String sql = "SELECT * FROM notifications WHERE notification_type=? ORDER BY created_at DESC";
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, notificationType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting notifications by type: " + e.getMessage(), e);
        }
        
        return notifications;
    }
}

