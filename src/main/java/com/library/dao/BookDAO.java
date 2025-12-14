package com.library.dao;

import com.library.model.Book;
import com.library.exception.DatabaseException;
import java.util.List;

/**
 * DAO interface for Book operations
 * Demonstrates Interface usage
 */
public interface BookDAO {
    /**
     * Add a new book to the database
     */
    boolean addBook(Book book) throws DatabaseException;
    
    /**
     * Update an existing book
     */
    boolean updateBook(Book book) throws DatabaseException;
    
    /**
     * Delete a book by ID
     */
    boolean deleteBook(int bookId) throws DatabaseException;
    
    /**
     * Get a book by ID
     */
    Book getBookById(int bookId) throws DatabaseException;
    
    /**
     * Get a book by ISBN
     */
    Book getBookByIsbn(String isbn) throws DatabaseException;
    
    /**
     * Get all books
     */
    List<Book> getAllBooks() throws DatabaseException;
    
    /**
     * Search books by title
     */
    List<Book> searchByTitle(String title) throws DatabaseException;
    
    /**
     * Search books by author
     */
    List<Book> searchByAuthor(String author) throws DatabaseException;
    
    /**
     * Search books by genre
     */
    List<Book> searchByGenre(String genre) throws DatabaseException;
    
    /**
     * Search books by title, author, or genre
     */
    List<Book> searchBooks(String keyword) throws DatabaseException;
    
    /**
     * Get available books
     */
    List<Book> getAvailableBooks() throws DatabaseException;
    
    /**
     * Update book copies when borrowed
     */
    boolean decrementAvailableCopies(int bookId) throws DatabaseException;
    
    /**
     * Update book copies when returned
     */
    boolean incrementAvailableCopies(int bookId) throws DatabaseException;
}

