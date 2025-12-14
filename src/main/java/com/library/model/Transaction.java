package com.library.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Transaction model class representing borrowing/returning transactions
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int transactionId;
    private int bookId;
    private int memberId;
    private String transactionType; // BORROW or RETURN
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal fineAmount;
    private String status; // ACTIVE, RETURNED, OVERDUE
    private Book book; // Associated book object
    private Member member; // Associated member object
    
    public Transaction() {
        this.fineAmount = BigDecimal.ZERO;
        this.status = "ACTIVE";
        this.transactionType = "BORROW";
    }
    
    public Transaction(int bookId, int memberId, String transactionType) {
        this();
        this.bookId = bookId;
        this.memberId = memberId;
        this.transactionType = transactionType;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(14); // Default 14 days loan period
    }
    
    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public int getMemberId() {
        return memberId;
    }
    
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    
    public String getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
    
    public String getBorrowDateString() {
        if (borrowDate != null) {
            return borrowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }
    
    public void setBorrowDateString(String dateString) {
        if (dateString != null && !dateString.isEmpty()) {
            this.borrowDate = LocalDate.parse(dateString);
        }
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public String getDueDateString() {
        if (dueDate != null) {
            return dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }
    
    public void setDueDateString(String dateString) {
        if (dateString != null && !dateString.isEmpty()) {
            this.dueDate = LocalDate.parse(dateString);
        }
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public String getReturnDateString() {
        if (returnDate != null) {
            return returnDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }
    
    public void setReturnDateString(String dateString) {
        if (dateString != null && !dateString.isEmpty()) {
            this.returnDate = LocalDate.parse(dateString);
        }
    }
    
    public BigDecimal getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public Member getMember() {
        return member;
    }
    
    public void setMember(Member member) {
        this.member = member;
    }
    
    public boolean isOverdue() {
        if (returnDate != null || dueDate == null) {
            return false;
        }
        return LocalDate.now().isAfter(dueDate) && "ACTIVE".equals(status);
    }
    
    public long getDaysOverdue() {
        if (isOverdue()) {
            return java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", transactionType='" + transactionType + '\'' +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                '}';
    }
}

