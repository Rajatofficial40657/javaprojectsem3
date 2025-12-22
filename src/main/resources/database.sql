-- Library Management System Database Schema
-- Create Database
CREATE DATABASE IF NOT EXISTS librarydb;
USE librarydb;

-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS members;

-- Create Members Table
CREATE TABLE members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    membership_id VARCHAR(50) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    registration_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    user_type VARCHAR(20) DEFAULT 'MEMBER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_membership_id (membership_id),
    INDEX idx_user_type (user_type)
);

-- Create Books Table
CREATE TABLE books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(200) NOT NULL,
    isbn VARCHAR(50) UNIQUE NOT NULL,
    genre VARCHAR(100),
    publisher VARCHAR(200),
    publication_date DATE,
    total_copies INT DEFAULT 1,
    available_copies INT DEFAULT 1,
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_author (author),
    INDEX idx_isbn (isbn),
    INDEX idx_genre (genre),
    INDEX idx_status (status)
);

-- Create Transactions Table
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL, -- 'BORROW' or 'RETURN'
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ACTIVE', -- 'ACTIVE', 'RETURNED', 'OVERDUE'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
    INDEX idx_book_id (book_id),
    INDEX idx_member_id (member_id),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date)
);

-- Create Notifications Table
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT,
    notification_type VARCHAR(50) NOT NULL, -- 'DUE_DATE', 'NEW_ARRIVAL', 'OVERDUE', 'GENERAL'
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
    INDEX idx_member_id (member_id),
    INDEX idx_is_read (is_read),
    INDEX idx_notification_type (notification_type)
);

-- Insert Default Librarian Account
-- Password: admin123 (hashed)
INSERT INTO members (name, email, password, membership_id, user_type, registration_date) 
VALUES ('Admin Librarian', 'admin@library.com', 'admin123', 'LIB001', 'LIBRARIAN', CURDATE());

-- Insert Sample Books
INSERT INTO books (title, author, isbn, genre, publisher, total_copies, available_copies) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '978-0-7432-7356-5', 'Fiction', 'Scribner', 3, 3),
('To Kill a Mockingbird', 'Harper Lee', '978-0-06-112008-4', 'Fiction', 'J.B. Lippincott', 2, 2),
('1984', 'George Orwell', '978-0-452-28423-4', 'Dystopian Fiction', 'Secker & Warburg', 4, 4),
('Pride and Prejudice', 'Jane Austen', '978-0-14-143951-8', 'Romance', 'T. Egerton', 2, 2),
('The Catcher in the Rye', 'J.D. Salinger', '978-0-316-76948-0', 'Fiction', 'Little, Brown and Company', 2, 2);

-- Insert Sample Member
-- Password: member123
INSERT INTO members (name, email, password, membership_id, phone, registration_date) 
VALUES ('John Doe', 'john.doe@email.com', 'member123', 'MEM001', '123-456-7890', CURDATE());

