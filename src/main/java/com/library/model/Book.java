package com.library.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Book model class representing a book in the library
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int bookId;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String publisher;
    private LocalDate publicationDate;
    private int totalCopies;
    private int availableCopies;
    private String status;
    private String description;
    
    public Book() {
        this.status = "AVAILABLE";
        this.totalCopies = 1;
        this.availableCopies = 1;
    }
    
    public Book(String title, String author, String isbn, String genre) {
        this();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
    }
    
    // Getters and Setters
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getPublisher() {
        return publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public LocalDate getPublicationDate() {
        return publicationDate;
    }
    
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }
    
    public String getPublicationDateString() {
        if (publicationDate != null) {
            return publicationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }
    
    public void setPublicationDateString(String dateString) {
        if (dateString != null && !dateString.isEmpty()) {
            this.publicationDate = LocalDate.parse(dateString);
        }
    }
    
    public int getTotalCopies() {
        return totalCopies;
    }
    
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }
    
    public int getAvailableCopies() {
        return availableCopies;
    }
    
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isAvailable() {
        return "AVAILABLE".equals(status) && availableCopies > 0;
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genre='" + genre + '\'' +
                ", availableCopies=" + availableCopies +
                ", status='" + status + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return bookId == book.bookId || (isbn != null && isbn.equals(book.isbn));
    }
    
    @Override
    public int hashCode() {
        return isbn != null ? isbn.hashCode() : bookId;
    }
}

