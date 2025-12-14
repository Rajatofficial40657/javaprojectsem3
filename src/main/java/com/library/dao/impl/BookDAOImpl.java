package com.library.dao.impl;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.util.DatabaseConnection;
import com.library.exception.DatabaseException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of BookDAO
 * Demonstrates JDBC operations and exception handling
 */
public class BookDAOImpl implements BookDAO {
    private final DatabaseConnection dbConnection;
    
    public BookDAOImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public boolean addBook(Book book) throws DatabaseException {
        String sql = "INSERT INTO books (title, author, isbn, genre, publisher, " +
                     "publication_date, total_copies, available_copies, status, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getGenre());
            pstmt.setString(5, book.getPublisher());
            pstmt.setDate(6, book.getPublicationDate() != null ? 
                         Date.valueOf(book.getPublicationDate()) : null);
            pstmt.setInt(7, book.getTotalCopies());
            pstmt.setInt(8, book.getAvailableCopies());
            pstmt.setString(9, book.getStatus());
            pstmt.setString(10, book.getDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error adding book: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateBook(Book book) throws DatabaseException {
        String sql = "UPDATE books SET title=?, author=?, isbn=?, genre=?, publisher=?, " +
                     "publication_date=?, total_copies=?, available_copies=?, status=?, description=? " +
                     "WHERE book_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getGenre());
            pstmt.setString(5, book.getPublisher());
            pstmt.setDate(6, book.getPublicationDate() != null ? 
                         Date.valueOf(book.getPublicationDate()) : null);
            pstmt.setInt(7, book.getTotalCopies());
            pstmt.setInt(8, book.getAvailableCopies());
            pstmt.setString(9, book.getStatus());
            pstmt.setString(10, book.getDescription());
            pstmt.setInt(11, book.getBookId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating book: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteBook(int bookId) throws DatabaseException {
        String sql = "DELETE FROM books WHERE book_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting book: " + e.getMessage(), e);
        }
    }
    
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setGenre(rs.getString("genre"));
        book.setPublisher(rs.getString("publisher"));
        Date pubDate = rs.getDate("publication_date");
        if (pubDate != null) {
            book.setPublicationDate(pubDate.toLocalDate());
        }
        book.setTotalCopies(rs.getInt("total_copies"));
        book.setAvailableCopies(rs.getInt("available_copies"));
        book.setStatus(rs.getString("status"));
        book.setDescription(rs.getString("description"));
        return book;
    }
    
    @Override
    public Book getBookById(int bookId) throws DatabaseException {
        String sql = "SELECT * FROM books WHERE book_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting book by ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Book getBookByIsbn(String isbn) throws DatabaseException {
        String sql = "SELECT * FROM books WHERE isbn=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting book by ISBN: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Book> getAllBooks() throws DatabaseException {
        String sql = "SELECT * FROM books ORDER BY title";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all books: " + e.getMessage(), e);
        }
        
        return books;
    }
    
    @Override
    public List<Book> searchByTitle(String title) throws DatabaseException {
        String sql = "SELECT * FROM books WHERE title LIKE ? ORDER BY title";
        return searchBooks(sql, "%" + title + "%");
    }
    
    @Override
    public List<Book> searchByAuthor(String author) throws DatabaseException {
        String sql = "SELECT * FROM books WHERE author LIKE ? ORDER BY author";
        return searchBooks(sql, "%" + author + "%");
    }
    
    @Override
    public List<Book> searchByGenre(String genre) throws DatabaseException {
        String sql = "SELECT * FROM books WHERE genre LIKE ? ORDER BY title";
        return searchBooks(sql, "%" + genre + "%");
    }
    
    @Override
    public List<Book> searchBooks(String keyword) throws DatabaseException {
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR genre LIKE ? OR isbn LIKE ? ORDER BY title";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error searching books: " + e.getMessage(), e);
        }
        
        return books;
    }
    
    @Override
    public List<Book> getAvailableBooks() throws DatabaseException {
        String sql = "SELECT * FROM books WHERE status='AVAILABLE' AND available_copies > 0 ORDER BY title";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting available books: " + e.getMessage(), e);
        }
        
        return books;
    }
    
    @Override
    public boolean decrementAvailableCopies(int bookId) throws DatabaseException {
        String sql = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id=? AND available_copies > 0";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Check if all copies are borrowed
                Book book = getBookById(bookId);
                if (book != null && book.getAvailableCopies() == 0) {
                    String updateStatusSql = "UPDATE books SET status='UNAVAILABLE' WHERE book_id=?";
                    try (PreparedStatement statusStmt = conn.prepareStatement(updateStatusSql)) {
                        statusStmt.setInt(1, bookId);
                        statusStmt.executeUpdate();
                    }
                }
            }
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error decrementing available copies: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean incrementAvailableCopies(int bookId) throws DatabaseException {
        String sql = "UPDATE books SET available_copies = available_copies + 1, status='AVAILABLE' WHERE book_id=?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error incrementing available copies: " + e.getMessage(), e);
        }
    }
    
    public List<Book> searchBooks(String sql, String pattern) throws DatabaseException {
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, pattern);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error searching books: " + e.getMessage(), e);
        }
        
        return books;
    }
}

