package com.library.service;

import com.library.model.Notification;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.dao.NotificationDAO;
import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.dao.impl.NotificationDAOImpl;
import com.library.dao.impl.MemberDAOImpl;
import com.library.dao.impl.TransactionDAOImpl;
import com.library.exception.DatabaseException;
import com.library.exception.BusinessException;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service class for Notification operations
 * Demonstrates Multithreading for sending notifications
 */
public class NotificationService {
    private final NotificationDAO notificationDAO;
    private final MemberDAO memberDAO;
    private final TransactionDAO transactionDAO;
    private final ExecutorService executorService;
    
    public NotificationService() {
        this.notificationDAO = new NotificationDAOImpl();
        this.memberDAO = new MemberDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
        this.executorService = Executors.newFixedThreadPool(10);
    }
    
    /**
     * Send notification to a member
     */
    public boolean sendNotification(int memberId, String notificationType, 
                                    String title, String message) throws BusinessException {
        try {
            Notification notification = new Notification(memberId, notificationType, title, message);
            return notificationDAO.addNotification(notification);
        } catch (DatabaseException e) {
            throw new BusinessException("Error sending notification: " + e.getMessage(), e);
        }
    }
    
    /**
     * Send notification to all members
     */
    public void sendNotificationToAllMembers(String notificationType, 
                                            String title, String message) throws BusinessException {
        try {
            List<Member> members = memberDAO.getAllRegularMembers();
            
            // Use multithreading to send notifications concurrently
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            
            for (Member member : members) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        Notification notification = new Notification(
                            member.getMemberId(), 
                            notificationType, 
                            title, 
                            message
                        );
                        notificationDAO.addNotification(notification);
                    } catch (DatabaseException e) {
                        System.err.println("Error sending notification to " + member.getEmail() + ": " + e.getMessage());
                    }
                }, executorService);
                
                futures.add(future);
            }
            
            // Wait for all notifications to be sent
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            
        } catch (DatabaseException e) {
            throw new BusinessException("Error sending notifications: " + e.getMessage(), e);
        }
    }
    
    /**
     * Send due date reminders (runs asynchronously)
     * Demonstrates multithreading
     */
    public CompletableFuture<Void> sendDueDateReminders() {
        return CompletableFuture.runAsync(() -> {
            try {
                List<Transaction> activeTransactions = transactionDAO.getActiveTransactions();
                LocalDate today = LocalDate.now();
                LocalDate tomorrow = today.plusDays(1);
                
                for (Transaction transaction : activeTransactions) {
                    if (transaction.getDueDate() != null) {
                        // Send reminder if due in 1 day
                        if (transaction.getDueDate().equals(tomorrow)) {
                            Member member = memberDAO.getMemberById(transaction.getMemberId());
                            if (member != null) {
                                String title = "Book Due Tomorrow";
                                String message = "Your book \"" + 
                                    (transaction.getBook() != null ? transaction.getBook().getTitle() : "Book") +
                                    "\" is due tomorrow (" + transaction.getDueDate() + "). Please return it on time.";
                                
                                try {
                                    sendNotification(member.getMemberId(), "DUE_DATE", title, message);
                                } catch (BusinessException e) {
                                    System.err.println("Error sending reminder: " + e.getMessage());
                                }
                            }
                        }
                    }
                }
            } catch (DatabaseException e) {
                System.err.println("Error sending due date reminders: " + e.getMessage());
            }
        }, executorService);
    }
    
    /**
     * Send overdue notifications (runs asynchronously)
     * Demonstrates multithreading
     */
    public CompletableFuture<Void> sendOverdueNotifications() {
        return CompletableFuture.runAsync(() -> {
            try {
                List<Transaction> overdueTransactions = transactionDAO.getOverdueTransactions();
                
                for (Transaction transaction : overdueTransactions) {
                    Member member = memberDAO.getMemberById(transaction.getMemberId());
                    if (member != null) {
                        String title = "Overdue Book";
                        String message = "Your book \"" + 
                            (transaction.getBook() != null ? transaction.getBook().getTitle() : "Book") +
                            "\" was due on " + transaction.getDueDate() + 
                            ". Please return it immediately to avoid additional fines.";
                        
                        try {
                            sendNotification(member.getMemberId(), "OVERDUE", title, message);
                        } catch (BusinessException e) {
                            System.err.println("Error sending overdue notification: " + e.getMessage());
                        }
                    }
                }
            } catch (DatabaseException e) {
                System.err.println("Error sending overdue notifications: " + e.getMessage());
            }
        }, executorService);
    }
    
    /**
     * Get notifications by member ID
     */
    public List<Notification> getNotificationsByMemberId(int memberId) throws BusinessException {
        try {
            return notificationDAO.getNotificationsByMemberId(memberId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting notifications: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get unread notifications by member ID
     */
    public List<Notification> getUnreadNotificationsByMemberId(int memberId) throws BusinessException {
        try {
            return notificationDAO.getUnreadNotificationsByMemberId(memberId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting unread notifications: " + e.getMessage(), e);
        }
    }
    
    /**
     * Mark notification as read
     */
    public boolean markAsRead(int notificationId) throws BusinessException {
        try {
            return notificationDAO.markAsRead(notificationId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error marking notification as read: " + e.getMessage(), e);
        }
    }
    
    /**
     * Shutdown executor service (should be called when application stops)
     */
    public void shutdown() {
        executorService.shutdown();
    }
}

