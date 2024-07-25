package com.abmike;

import com.abmike.model.Book;
import com.abmike.model.Patron;
import com.abmike.model.Transaction;
import com.abmike.service.BookManagement;
import com.abmike.service.PatronManagement;
import com.abmike.service.TransactionManagement;
import com.abmike.util.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionIntegrationTest {

    private static Connection connection;
    private TransactionManagement transactionManagement;
    private BookManagement bookManagement;
    private PatronManagement patronManagement;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @BeforeEach
    void init() {
        transactionManagement = new TransactionManagement(connection);
        bookManagement = new BookManagement(connection);
        patronManagement = new PatronManagement(connection);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        DatabaseConnection.releaseConnection(connection);
    }

    @Test
    void testBorrowAndReturnBook() throws SQLException {
        // Add a test book
        Book book = new Book(0, "Test Book", "Test Author", "1234567890", true, 1);
        int bookId = bookManagement.addBook(book);
        assertNotEquals(0, bookId, "Book ID should not be 0 after adding");

        // Add a test patron
        Patron patron = new Patron(0, "Test Patron", "test@exampl.com", "1234567890");
        int patronId = patronManagement.addPatron(patron);
        assertNotEquals(0, patronId, "Patron ID should not be 0 after adding");

        // Borrow the book
        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = borrowDate.plusDays(14);
        transactionManagement.borrowBook(bookId, patronId, borrowDate, returnDate, "Borrowed", "LIB001");



        // Get the transaction
        List<Transaction> transactions = transactionManagement.getAllTransactions();
        Transaction borrowTransaction = transactions.stream()
                .filter(t -> t.getBookId() == bookId && t.getPatronId() == patronId)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Transaction not found after borrowing"));

        // Return the book
        transactionManagement.returnBook(borrowTransaction.getId(), "LIB001");

        // Check if the book is available again
        Book returnedBook = bookManagement.getBookById(bookId);
        assertTrue(returnedBook.isAvailable(), "Book should be available after returning");

        // Clean up
        transactionManagement.deleteTransactionsForBook(bookId);
        transactionManagement.deleteTransactionsForPatron(patronId);
        bookManagement.deleteBook(bookId);
        patronManagement.deletePatron(patronId);

        // Verify deletion
        assertNull(bookManagement.getBookById(bookId), "Book should be null after deletion");
        assertNull(patronManagement.getPatronById(patronId), "Patron should be null after deletion");
    }
}