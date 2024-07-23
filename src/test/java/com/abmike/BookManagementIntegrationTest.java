package com.abmike;

import com.abmike.model.Book;
import com.abmike.service.BookManagement;
import com.abmike.util.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

class BookManagementIntegrationTest {

    private BookManagement bookManagement;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection();
        bookManagement = new BookManagement(connection);
    }

    @Test
    void testAddAndRetrieveBook() throws SQLException {
        Book newBook = new Book(0, "Integration Test Book", "Test Author", "1234567890", true, 1);
        bookManagement.addBook(newBook);

        List<Book> books = bookManagement.getAllBooks();
        assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("Integration Test Book")));

        Book retrievedBook = books.stream()
                .filter(b -> b.getTitle().equals("Integration Test Book"))
                .findFirst()
                .orElse(null);

        assertNotNull(retrievedBook);
        assertEquals("Test Author", retrievedBook.getAuthor());
        assertEquals("1234567890", retrievedBook.getIsbn());
        assertTrue(retrievedBook.isAvailable());
        assertEquals(1, retrievedBook.getCount());

        // Clean up
        bookManagement.deleteBook(retrievedBook.getId());
    }

    @Test
    void testUpdateBook() throws SQLException {
        Book newBook = new Book(0, "Update Test Book", "Original Author", "1234567890", true, 1);
        bookManagement.addBook(newBook);

        List<Book> books = bookManagement.getAllBooks();
        Book bookToUpdate = books.stream()
                .filter(b -> b.getTitle().equals("Update Test Book"))
                .findFirst()
                .orElse(null);

        assertNotNull(bookToUpdate);

        bookToUpdate.setAuthor("Updated Author");
        bookToUpdate.setCount(2);
        bookManagement.updateBook(bookToUpdate);

        Book updatedBook = bookManagement.getBookById(bookToUpdate.getId());
        assertEquals("Updated Author", updatedBook.getAuthor());
        assertEquals(2, updatedBook.getCount());

        // Clean up
        bookManagement.deleteBook(updatedBook.getId());
    }
}