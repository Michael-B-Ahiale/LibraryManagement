package com.abmike.service;

import com.abmike.model.Book;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class BookManagement {

    private Connection connection;
    private LinkedList<Book> bookCache;
    private Queue<Book> processingQueue;

    public BookManagement(Connection connection) {
        this.connection = connection;
        this.bookCache = new LinkedList<>();
        this.processingQueue = new LinkedList<>();
    }

    public void queueBookForProcessing(Book book) {
        processingQueue.offer(book);
    }

    public Book processNextBook() {
        return processingQueue.poll();
    }

    public int getProcessingQueueSize() {
        return processingQueue.size();
    }

    public int addBook(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author, isbn, available, count) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setBoolean(4, book.isAvailable());
            stmt.setInt(5, book.getCount());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    book.setId(id);
                    bookCache.add(book);
                    return id;
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
        }
    }

    public void updateBook(Book book) throws SQLException {
        String query = "UPDATE books SET title = ?, author = ?, isbn = ?, available = ?, count = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setBoolean(4, book.isAvailable());
            stmt.setInt(5, book.getCount());
            stmt.setInt(6, book.getId());
            stmt.executeUpdate();
        }
        updateBookInCache(book); // Update the book in the cache
    }

    public void deleteBook(int id) throws SQLException {
        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        removeBookFromCache(id); // Remove the book from the cache
    }

    public LinkedList<Book> getAllBooks() throws SQLException {
        if (bookCache.isEmpty()) {
            loadBooksIntoCache();
        }
        return new LinkedList<>(bookCache); // Return a copy of the cache
    }

    public Book getBookById(int id) throws SQLException {
        // First, try to find the book in the cache
        for (Book book : bookCache) {
            if (book.getId() == id) {
                return book;
            }
        }

        // If not found in cache, query the database
        String query = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("available"),
                            rs.getInt("count")
                    );
                    bookCache.add(book); // Add to cache for future queries
                    return book;
                }
            }
        }
        return null;
    }

    private void loadBooksIntoCache() throws SQLException {
        bookCache.clear();
        String query = "SELECT * FROM books";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getBoolean("available"),
                        rs.getInt("count")
                );
                bookCache.add(book);
            }
        }
    }

    private void updateBookInCache(Book updatedBook) {
        for (int i = 0; i < bookCache.size(); i++) {
            if (bookCache.get(i).getId() == updatedBook.getId()) {
                bookCache.set(i, updatedBook);
                break;
            }
        }
    }

    private void removeBookFromCache(int id) {
        bookCache.removeIf(book -> book.getId() == id);
    }
}