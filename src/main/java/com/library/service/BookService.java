package com.library.service;

import com.library.model.Book;
import com.library.dao.BookDAO;
import com.library.dao.impl.BookDAOImpl;
import com.library.exception.DatabaseException;
import com.library.exception.BusinessException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Service class for Book operations
 * Demonstrates Collections and Generics usage
 */
public class BookService {
    private final BookDAO bookDAO;
    
    public BookService() {
        this.bookDAO = new BookDAOImpl();
    }
    
    /**
     * Add a new book
     */
    public boolean addBook(Book book) throws BusinessException {
        try {
            // Validate book data
            validateBook(book);
            
            // Check if ISBN already exists
            Book existingBook = bookDAO.getBookByIsbn(book.getIsbn());
            if (existingBook != null) {
                throw new BusinessException("Book with ISBN " + book.getIsbn() + " already exists");
            }
            
            return bookDAO.addBook(book);
        } catch (DatabaseException e) {
            throw new BusinessException("Error adding book: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update a book
     */
    public boolean updateBook(Book book) throws BusinessException {
        try {
            validateBook(book);
            
            Book existingBook = bookDAO.getBookById(book.getBookId());
            if (existingBook == null) {
                throw new BusinessException("Book not found");
            }
            
            // Check ISBN uniqueness if changed
            if (!existingBook.getIsbn().equals(book.getIsbn())) {
                Book bookWithIsbn = bookDAO.getBookByIsbn(book.getIsbn());
                if (bookWithIsbn != null && bookWithIsbn.getBookId() != book.getBookId()) {
                    throw new BusinessException("ISBN already exists for another book");
                }
            }
            
            return bookDAO.updateBook(book);
        } catch (DatabaseException e) {
            throw new BusinessException("Error updating book: " + e.getMessage(), e);
        }
    }
    
    /**
     * Delete a book
     */
    public boolean deleteBook(int bookId) throws BusinessException {
        try {
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                throw new BusinessException("Book not found");
            }
            
            // Check if book is currently borrowed
            if (book.getTotalCopies() != book.getAvailableCopies()) {
                throw new BusinessException("Cannot delete book. Some copies are currently borrowed");
            }
            
            return bookDAO.deleteBook(bookId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error deleting book: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get book by ID
     */
    public Book getBookById(int bookId) throws BusinessException {
        try {
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                throw new BusinessException("Book not found");
            }
            return book;
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting book: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all books
     */
    public List<Book> getAllBooks() throws BusinessException {
        try {
            return bookDAO.getAllBooks();
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting books: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search books using generic search
     * Demonstrates Collections usage
     */
    public List<Book> searchBooks(String keyword) throws BusinessException {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return new ArrayList<>(); // Return empty list instead of null
            }
            
            return bookDAO.searchBooks(keyword.trim());
        } catch (DatabaseException e) {
            throw new BusinessException("Error searching books: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get books grouped by genre
     * Demonstrates Collections (Map) and Streams
     */
    public Map<String, List<Book>> getBooksByGenre() throws BusinessException {
        try {
            List<Book> allBooks = bookDAO.getAllBooks();
            
            return allBooks.stream()
                    .filter(book -> book.getGenre() != null && !book.getGenre().isEmpty())
                    .collect(Collectors.groupingBy(
                        Book::getGenre,
                        Collectors.toList()
                    ));
        } catch (DatabaseException e) {
            throw new BusinessException("Error grouping books by genre: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get books grouped by author
     * Demonstrates Collections (Map) and Streams
     */
    public Map<String, List<Book>> getBooksByAuthor() throws BusinessException {
        try {
            List<Book> allBooks = bookDAO.getAllBooks();
            
            return allBooks.stream()
                    .filter(book -> book.getAuthor() != null && !book.getAuthor().isEmpty())
                    .collect(Collectors.groupingBy(
                        Book::getAuthor,
                        Collectors.toList()
                    ));
        } catch (DatabaseException e) {
            throw new BusinessException("Error grouping books by author: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get available books count by genre
     * Demonstrates Collections (Map) with aggregation
     */
    public Map<String, Integer> getAvailableBooksCountByGenre() throws BusinessException {
        try {
            List<Book> allBooks = bookDAO.getAllBooks();
            
            Map<String, Integer> genreCount = new HashMap<>();
            
            for (Book book : allBooks) {
                if (book.getGenre() != null && book.isAvailable()) {
                    genreCount.merge(book.getGenre(), book.getAvailableCopies(), Integer::sum);
                }
            }
            
            return genreCount;
        } catch (DatabaseException e) {
            throw new BusinessException("Error counting books by genre: " + e.getMessage(), e);
        }
    }
    
    /**
     * Validate book data
     */
    private void validateBook(Book book) throws BusinessException {
        if (book == null) {
            throw new BusinessException("Book cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new BusinessException("Book title is required");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new BusinessException("Book author is required");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new BusinessException("Book ISBN is required");
        }
        if (book.getTotalCopies() < 0) {
            throw new BusinessException("Total copies cannot be negative");
        }
        if (book.getAvailableCopies() < 0) {
            throw new BusinessException("Available copies cannot be negative");
        }
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new BusinessException("Available copies cannot exceed total copies");
        }
    }
    
    /**
     * Get available books
     */
    public List<Book> getAvailableBooks() throws BusinessException {
        try {
            return bookDAO.getAvailableBooks();
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting available books: " + e.getMessage(), e);
        }
    }
}

