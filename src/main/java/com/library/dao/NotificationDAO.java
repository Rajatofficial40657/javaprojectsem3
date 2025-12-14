package com.library.dao;

import com.library.model.Notification;
import com.library.exception.DatabaseException;
import java.util.List;

/**
 * DAO interface for Notification operations
 */
public interface NotificationDAO {
    /**
     * Add a new notification
     */
    boolean addNotification(Notification notification) throws DatabaseException;
    
    /**
     * Update a notification
     */
    boolean updateNotification(Notification notification) throws DatabaseException;
    
    /**
     * Get notification by ID
     */
    Notification getNotificationById(int notificationId) throws DatabaseException;
    
    /**
     * Get all notifications
     */
    List<Notification> getAllNotifications() throws DatabaseException;
    
    /**
     * Get notifications by member ID
     */
    List<Notification> getNotificationsByMemberId(int memberId) throws DatabaseException;
    
    /**
     * Get unread notifications by member ID
     */
    List<Notification> getUnreadNotificationsByMemberId(int memberId) throws DatabaseException;
    
    /**
     * Mark notification as read
     */
    boolean markAsRead(int notificationId) throws DatabaseException;
    
    /**
     * Delete notification by ID
     */
    boolean deleteNotification(int notificationId) throws DatabaseException;
    
    /**
     * Get notifications by type
     */
    List<Notification> getNotificationsByType(String notificationType) throws DatabaseException;
}

