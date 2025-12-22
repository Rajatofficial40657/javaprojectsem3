package com.library.dao.impl;

import com.library.dao.TransactionDAO;
import com.library.model.Transaction;
import com.library.model.Book;
import com.library.model.Member;
import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.util.DatabaseConnection;
import com.library.exception.DatabaseException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of TransactionDAO
 */
public class TransactionDAOImpl implements TransactionDAO {
    private final DatabaseConnection dbConnection;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    
    public TransactionDAOImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.bookDAO = new BookDAOImpl();
        this.memberDAO = new MemberDAOImpl();
    }
    
    @Override
    public boolean addTransaction(Transaction transaction) throws DatabaseException {
        String sql = "INSERT INTO transactions (book_id, member_id, transaction_type, " +
                     "borrow_date, due_date, return_date, fine_amount, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, transaction.getBookId());
            pstmt.setInt(2, transaction.getMemberId());
            pstmt.setString(3, transaction.getTransactionType());
            pstmt.setDate(4, transaction.getBorrowDate() != null ? 
                         Date.valueOf(transaction.getBorrowDate()) : Date.valueOf(LocalDate.now()));
            pstmt.setDate(5, transaction.getDueDate() != null ? 
                         Date.valueOf(transaction.getDueDate()) : 
                         Date.valueOf(LocalDate.now().plusDays(14)));
            pstmt.setDate(6, transaction.getReturnDate() != null ? 
                         Date.valueOf(transaction.getReturnDate()) : null);
            pstmt.setBigDecimal(7, transaction.getFineAmount() != null ? 
                               transaction.getFineAmount() : BigDecimal.ZERO);
            pstmt.setString(8, transaction.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    transaction.setTransactionId(generatedKeys.getInt(1));
                }
            }
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error adding transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateTransaction(Transaction transaction) throws DatabaseException {
        String sql = "UPDATE transactions SET book_id=?, member_id=?, transaction_type=?, " +
                     "borrow_date=?, due_date=?, return_date=?, fine_amount=?, status=? " +
                     "WHERE transaction_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transaction.getBookId());
            pstmt.setInt(2, transaction.getMemberId());
            pstmt.setString(3, transaction.getTransactionType());
            pstmt.setDate(4, transaction.getBorrowDate() != null ? 
                         Date.valueOf(transaction.getBorrowDate()) : null);
            pstmt.setDate(5, transaction.getDueDate() != null ? 
                         Date.valueOf(transaction.getDueDate()) : null);
            pstmt.setDate(6, transaction.getReturnDate() != null ? 
                         Date.valueOf(transaction.getReturnDate()) : null);
            pstmt.setBigDecimal(7, transaction.getFineAmount() != null ? 
                               transaction.getFineAmount() : BigDecimal.ZERO);
            pstmt.setString(8, transaction.getStatus());
            pstmt.setInt(9, transaction.getTransactionId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating transaction: " + e.getMessage(), e);
        }
    }
    
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException, DatabaseException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setBookId(rs.getInt("book_id"));
        transaction.setMemberId(rs.getInt("member_id"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        
        Date borrowDate = rs.getDate("borrow_date");
        if (borrowDate != null) {
            transaction.setBorrowDate(borrowDate.toLocalDate());
        }
        
        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            transaction.setDueDate(dueDate.toLocalDate());
        }
        
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            transaction.setReturnDate(returnDate.toLocalDate());
        }
        
        transaction.setFineAmount(rs.getBigDecimal("fine_amount"));
        transaction.setStatus(rs.getString("status"));
        
        // Load associated book and member
        try {
            Book book = bookDAO.getBookById(transaction.getBookId());
            transaction.setBook(book);
            
            Member member = memberDAO.getMemberById(transaction.getMemberId());
            transaction.setMember(member);
        } catch (DatabaseException e) {
            // Log but don't fail if associated objects can't be loaded
            System.err.println("Warning: Could not load associated objects: " + e.getMessage());
        }
        
        return transaction;
    }
    
    @Override
    public Transaction getTransactionById(int transactionId) throws DatabaseException {
        String sql = "SELECT * FROM transactions WHERE transaction_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting transaction by ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Transaction> getAllTransactions() throws DatabaseException {
        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all transactions: " + e.getMessage(), e);
        }
        
        return transactions;
    }
    
    @Override
    public List<Transaction> getTransactionsByMemberId(int memberId) throws DatabaseException {
        String sql = "SELECT * FROM transactions WHERE member_id=? ORDER BY created_at DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting transactions by member ID: " + e.getMessage(), e);
        }
        
        return transactions;
    }
    
    @Override
    public List<Transaction> getTransactionsByBookId(int bookId) throws DatabaseException {
        String sql = "SELECT * FROM transactions WHERE book_id=? ORDER BY created_at DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting transactions by book ID: " + e.getMessage(), e);
        }
        
        return transactions;
    }
    
    @Override
    public List<Transaction> getActiveTransactions() throws DatabaseException {
        String sql = "SELECT * FROM transactions WHERE status='ACTIVE' ORDER BY due_date ASC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting active transactions: " + e.getMessage(), e);
        }
        
        return transactions;
    }
    
    @Override
    public List<Transaction> getActiveTransactionsByMemberId(int memberId) throws DatabaseException {
        String sql = "SELECT * FROM transactions WHERE member_id=? AND status='ACTIVE' ORDER BY due_date ASC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting active transactions by member ID: " + e.getMessage(), e);
        }
        
        return transactions;
    }
    
    @Override
    public List<Transaction> getOverdueTransactions() throws DatabaseException {
        String sql = "SELECT * FROM transactions WHERE status='ACTIVE' AND due_date < CURDATE() ORDER BY due_date ASC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Transaction transaction = mapResultSetToTransaction(rs);
                transaction.setStatus("OVERDUE");
                transactions.add(transaction);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting overdue transactions: " + e.getMessage(), e);
        }
        
        return transactions;
    }
    
    @Override
    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) throws DatabaseException {
        String sql = "SELECT * FROM transactions WHERE borrow_date >= ? AND borrow_date <= ? ORDER BY borrow_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting transactions by date range: " + e.getMessage(), e);
        }
        
        return transactions;
    }
    
    @Override
    public boolean returnBook(int transactionId, LocalDate returnDate) throws DatabaseException {
        String sql = "UPDATE transactions SET return_date=?, status='RETURNED' WHERE transaction_id=? AND status='ACTIVE'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(returnDate != null ? returnDate : LocalDate.now()));
            pstmt.setInt(2, transactionId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get transaction to update book copies
                Transaction transaction = getTransactionById(transactionId);
                if (transaction != null) {
                    bookDAO.incrementAvailableCopies(transaction.getBookId());
                    
                    // Calculate fine if overdue
                    if (transaction.getDueDate() != null && returnDate != null) {
                        if (returnDate.isAfter(transaction.getDueDate())) {
                            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(
                                transaction.getDueDate(), returnDate);
                            BigDecimal fine = BigDecimal.valueOf(daysOverdue * 2.0); // $2 per day
                            
                            String fineSql = "UPDATE transactions SET fine_amount=? WHERE transaction_id=?";
                            try (PreparedStatement fineStmt = conn.prepareStatement(fineSql)) {
                                fineStmt.setBigDecimal(1, fine);
                                fineStmt.setInt(2, transactionId);
                                fineStmt.executeUpdate();
                            }
                        }
                    }
                }
            }
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error returning book: " + e.getMessage(), e);
        }
    }
}

