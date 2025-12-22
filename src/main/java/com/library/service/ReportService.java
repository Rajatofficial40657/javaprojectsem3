package com.library.service;

import com.library.model.Book;
import com.library.model.Transaction;
import com.library.dao.BookDAO;
import com.library.dao.TransactionDAO;
import com.library.dao.impl.BookDAOImpl;
import com.library.dao.impl.TransactionDAOImpl;
import com.library.exception.DatabaseException;
import com.library.exception.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Service class for generating reports
 * Demonstrates Multithreading for report generation
 */
public class ReportService {
    private final BookDAO bookDAO;
    private final TransactionDAO transactionDAO;
    private final ExecutorService executorService;
    
    public ReportService() {
        this.bookDAO = new BookDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
        this.executorService = Executors.newFixedThreadPool(5);
    }
    
    /**
     * Generate inventory report (runs asynchronously)
     * Demonstrates multithreading
     */
    public CompletableFuture<Map<String, Object>> generateInventoryReport() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> report = new HashMap<>();
                
                List<Book> allBooks = bookDAO.getAllBooks();
                
                int totalBooks = allBooks.size();
                int totalCopies = allBooks.stream()
                        .mapToInt(Book::getTotalCopies)
                        .sum();
                int availableCopies = allBooks.stream()
                        .mapToInt(Book::getAvailableCopies)
                        .sum();
                int borrowedCopies = totalCopies - availableCopies;
                
                // Books by genre
                Map<String, Long> booksByGenre = allBooks.stream()
                        .filter(b -> b.getGenre() != null)
                        .collect(Collectors.groupingBy(
                            Book::getGenre,
                            Collectors.counting()
                        ));
                
                // Books by status
                Map<String, Long> booksByStatus = allBooks.stream()
                        .filter(b -> b.getStatus() != null)
                        .collect(Collectors.groupingBy(
                            Book::getStatus,
                            Collectors.counting()
                        ));
                
                report.put("totalBooks", totalBooks);
                report.put("totalCopies", totalCopies);
                report.put("availableCopies", availableCopies);
                report.put("borrowedCopies", borrowedCopies);
                report.put("booksByGenre", booksByGenre);
                report.put("booksByStatus", booksByStatus);
                report.put("allBooks", allBooks);
                
                return report;
            } catch (DatabaseException e) {
                throw new RuntimeException("Error generating inventory report: " + e.getMessage(), e);
            }
        }, executorService);
    }
    
    /**
     * Generate borrowing trends report (runs asynchronously)
     * Demonstrates multithreading
     */
    public CompletableFuture<Map<String, Object>> generateBorrowingTrendsReport(LocalDate startDate, LocalDate endDate) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> report = new HashMap<>();
                
                List<Transaction> transactions = transactionDAO.getTransactionsByDateRange(startDate, endDate);
                
                long totalTransactions = transactions.size();
                long borrowTransactions = transactions.stream()
                        .filter(t -> "BORROW".equals(t.getTransactionType()))
                        .count();
                long returnTransactions = transactions.stream()
                        .filter(t -> "RETURN".equals(t.getTransactionType()))
                        .count();
                
                // Transactions by status
                Map<String, Long> transactionsByStatus = transactions.stream()
                        .filter(t -> t.getStatus() != null)
                        .collect(Collectors.groupingBy(
                            Transaction::getStatus,
                            Collectors.counting()
                        ));
                
                // Most borrowed books
                Map<String, Long> mostBorrowedBooks = transactions.stream()
                        .filter(t -> t.getBook() != null && "BORROW".equals(t.getTransactionType()))
                        .collect(Collectors.groupingBy(
                            t -> t.getBook().getTitle(),
                            Collectors.counting()
                        ));
                
                BigDecimal totalFines = transactions.stream()
                        .filter(t -> t.getFineAmount() != null)
                        .map(Transaction::getFineAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                report.put("startDate", startDate);
                report.put("endDate", endDate);
                report.put("totalTransactions", totalTransactions);
                report.put("borrowTransactions", borrowTransactions);
                report.put("returnTransactions", returnTransactions);
                report.put("transactionsByStatus", transactionsByStatus);
                report.put("mostBorrowedBooks", mostBorrowedBooks);
                report.put("totalFines", totalFines);
                
                return report;
            } catch (DatabaseException e) {
                throw new RuntimeException("Error generating borrowing trends report: " + e.getMessage(), e);
            }
        }, executorService);
    }
    
    /**
     * Generate overdue books report
     */
    public Map<String, Object> generateOverdueBooksReport() throws BusinessException {
        try {
            Map<String, Object> report = new HashMap<>();
            
            List<Transaction> overdueTransactions = transactionDAO.getOverdueTransactions();
            
            long totalOverdue = overdueTransactions.size();
            BigDecimal totalFines = overdueTransactions.stream()
                    .filter(t -> t.getFineAmount() != null)
                    .map(Transaction::getFineAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Overdue by member
            Map<String, Long> overdueByMember = overdueTransactions.stream()
                    .filter(t -> t.getMember() != null)
                    .collect(Collectors.groupingBy(
                        t -> t.getMember().getName(),
                        Collectors.counting()
                    ));
            
            report.put("totalOverdue", totalOverdue);
            report.put("totalFines", totalFines);
            report.put("overdueByMember", overdueByMember);
            report.put("overdueTransactions", overdueTransactions);
            
            return report;
        } catch (DatabaseException e) {
            throw new BusinessException("Error generating overdue books report: " + e.getMessage(), e);
        }
    }
    
    /**
     * Shutdown executor service
     */
    public void shutdown() {
        executorService.shutdown();
    }
}

