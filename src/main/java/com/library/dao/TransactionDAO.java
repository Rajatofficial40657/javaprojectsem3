package com.library.dao;

import com.library.model.Transaction;
import com.library.exception.DatabaseException;
import java.time.LocalDate;
import java.util.List;

/**
 * DAO interface for Transaction operations
 */
public interface TransactionDAO {
    /**
     * Add a new transaction
     */
    boolean addTransaction(Transaction transaction) throws DatabaseException;
    
    /**
     * Update an existing transaction
     */
    boolean updateTransaction(Transaction transaction) throws DatabaseException;
    
    /**
     * Get transaction by ID
     */
    Transaction getTransactionById(int transactionId) throws DatabaseException;
    
    /**
     * Get all transactions
     */
    List<Transaction> getAllTransactions() throws DatabaseException;
    
    /**
     * Get transactions by member ID
     */
    List<Transaction> getTransactionsByMemberId(int memberId) throws DatabaseException;
    
    /**
     * Get transactions by book ID
     */
    List<Transaction> getTransactionsByBookId(int bookId) throws DatabaseException;
    
    /**
     * Get active transactions (not returned)
     */
    List<Transaction> getActiveTransactions() throws DatabaseException;
    
    /**
     * Get active transactions by member ID
     */
    List<Transaction> getActiveTransactionsByMemberId(int memberId) throws DatabaseException;
    
    /**
     * Get overdue transactions
     */
    List<Transaction> getOverdueTransactions() throws DatabaseException;
    
    /**
     * Get transactions by date range
     */
    List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) throws DatabaseException;
    
    /**
     * Return a book (update transaction)
     */
    boolean returnBook(int transactionId, LocalDate returnDate) throws DatabaseException;
}

