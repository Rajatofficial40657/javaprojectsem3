package com.library.service;

import com.library.model.Transaction;
import com.library.model.Book;
import com.library.model.Member;
import com.library.dao.TransactionDAO;
import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.impl.TransactionDAOImpl;
import com.library.dao.impl.BookDAOImpl;
import com.library.dao.impl.MemberDAOImpl;
import com.library.exception.DatabaseException;
import com.library.exception.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
//import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Service class for Transaction operations
 * Demonstrates Collections and Generics usage
 */
public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    
    public TransactionService() {
        this.transactionDAO = new TransactionDAOImpl();
        this.bookDAO = new BookDAOImpl();
        this.memberDAO = new MemberDAOImpl();
    }
    
    /**
     * Borrow a book
     */
    public Transaction borrowBook(int bookId, int memberId, int loanDays) throws BusinessException {
        try {
            // Validate book exists and is available
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                throw new BusinessException("Book not found");
            }
            
            if (!book.isAvailable()) {
                throw new BusinessException("Book is not available");
            }
            
            // Validate member exists and is active
            Member member = memberDAO.getMemberById(memberId);
            if (member == null) {
                throw new BusinessException("Member not found");
            }
            
            if (!member.isActive()) {
                throw new BusinessException("Member account is not active");
            }
            
            // Check if member has overdue books
            List<Transaction> overdueTransactions = getOverdueTransactionsByMember(memberId);
            if (!overdueTransactions.isEmpty()) {
                throw new BusinessException("Member has overdue books. Please return them first.");
            }
            
            // Create transaction
            Transaction transaction = new Transaction(bookId, memberId, "BORROW");
            if (loanDays > 0) {
                transaction.setDueDate(LocalDate.now().plusDays(loanDays));
            }
            
            boolean transactionAdded = transactionDAO.addTransaction(transaction);
            if (!transactionAdded) {
                throw new BusinessException("Failed to create transaction");
            }
            
            // Decrement available copies
            boolean updated = bookDAO.decrementAvailableCopies(bookId);
            if (!updated) {
                throw new BusinessException("Failed to update book availability");
            }
            
            return transaction;
        } catch (DatabaseException e) {
            throw new BusinessException("Error borrowing book: " + e.getMessage(), e);
        }
    }
    
    /**
     * Return a book
     */
    public Transaction returnBook(int transactionId) throws BusinessException {
        try {
            Transaction transaction = transactionDAO.getTransactionById(transactionId);
            if (transaction == null) {
                throw new BusinessException("Transaction not found");
            }
            
            if (!"ACTIVE".equals(transaction.getStatus())) {
                throw new BusinessException("Transaction is already closed");
            }
            
            LocalDate returnDate = LocalDate.now();
            boolean returned = transactionDAO.returnBook(transactionId, returnDate);
            
            if (!returned) {
                throw new BusinessException("Failed to return book");
            }
            
            // Get updated transaction
            return transactionDAO.getTransactionById(transactionId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error returning book: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all transactions
     */
    public List<Transaction> getAllTransactions() throws BusinessException {
        try {
            return transactionDAO.getAllTransactions();
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting transactions: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get transactions by member ID
     */
    public List<Transaction> getTransactionsByMemberId(int memberId) throws BusinessException {
        try {
            return transactionDAO.getTransactionsByMemberId(memberId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting member transactions: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get active transactions by member ID
     */
    public List<Transaction> getActiveTransactionsByMemberId(int memberId) throws BusinessException {
        try {
            return transactionDAO.getActiveTransactionsByMemberId(memberId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting active transactions: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get overdue transactions
     */
    public List<Transaction> getOverdueTransactions() throws BusinessException {
        try {
            return transactionDAO.getOverdueTransactions();
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting overdue transactions: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get overdue transactions by member ID
     */
    public List<Transaction> getOverdueTransactionsByMember(int memberId) throws BusinessException {
        try {
            List<Transaction> allTransactions = transactionDAO.getTransactionsByMemberId(memberId);
            LocalDate today = LocalDate.now();
            
            return allTransactions.stream()
                    .filter(t -> "ACTIVE".equals(t.getStatus()))
                    .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(today))
                    .collect(Collectors.toList());
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting overdue transactions: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get transactions grouped by status
     * Demonstrates Collections (Map) and Streams
     */
    public Map<String, List<Transaction>> getTransactionsByStatus() throws BusinessException {
        try {
            List<Transaction> allTransactions = transactionDAO.getAllTransactions();
            
            return allTransactions.stream()
                    .filter(t -> t.getStatus() != null)
                    .collect(Collectors.groupingBy(
                        Transaction::getStatus,
                        Collectors.toList()
                    ));
        } catch (DatabaseException e) {
            throw new BusinessException("Error grouping transactions by status: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get transaction statistics
     * Demonstrates Collections (Map) usage
     */
    public Map<String, Object> getTransactionStatistics() throws BusinessException {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<Transaction> allTransactions = transactionDAO.getAllTransactions();
            
            long totalTransactions = allTransactions.size();
            long activeTransactions = allTransactions.stream()
                    .filter(t -> "ACTIVE".equals(t.getStatus()))
                    .count();
            long returnedTransactions = allTransactions.stream()
                    .filter(t -> "RETURNED".equals(t.getStatus()))
                    .count();
            long overdueTransactions = getOverdueTransactions().size();
            
            BigDecimal totalFines = allTransactions.stream()
                    .filter(t -> t.getFineAmount() != null)
                    .map(Transaction::getFineAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            stats.put("totalTransactions", totalTransactions);
            stats.put("activeTransactions", activeTransactions);
            stats.put("returnedTransactions", returnedTransactions);
            stats.put("overdueTransactions", overdueTransactions);
            stats.put("totalFines", totalFines);
            
            return stats;
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting transaction statistics: " + e.getMessage(), e);
        }
    }
}

