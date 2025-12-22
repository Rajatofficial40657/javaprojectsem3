package com.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Notification model class representing notifications sent to members
 */
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int notificationId;
    private Integer memberId; // null for general notifications
    private String notificationType; // DUE_DATE, NEW_ARRIVAL, OVERDUE, GENERAL
    private String title;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Member member; // Associated member object
    
    public Notification() {
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }
    
    public Notification(String notificationType, String title, String message) {
        this();
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
    }
    
    public Notification(Integer memberId, String notificationType, String title, String message) {
        this(notificationType, title, message);
        this.memberId = memberId;
    }
    
    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }
    
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
    
    public Integer getMemberId() {
        return memberId;
    }
    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
    
    public String getNotificationType() {
        return notificationType;
    }
    
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean read) {
        isRead = read;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getCreatedAtString() {
        if (createdAt != null) {
            return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return null;
    }
    
    public void setCreatedAtString(String dateString) {
        if (dateString != null && !dateString.isEmpty()) {
            this.createdAt = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }
    
    public Member getMember() {
        return member;
    }
    
    public void setMember(Member member) {
        this.member = member;
    }
    
    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", memberId=" + memberId +
                ", notificationType='" + notificationType + '\'' +
                ", title='" + title + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}

