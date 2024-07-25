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
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class LibrarySystemRegressionTest {

    private static Connection connection;
    private BookManagement bookManagement;
    private PatronManagement patronManagement;
    private TransactionManagement transactionManagement;
    private static final Logger LOGGER = Logger.getLogger(LibrarySystemRegressionTest.class.getName());

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @BeforeEach
    void init() {
        bookManagement = new BookManagement(connection);
        patronManagement = new PatronManagement(connection);
        transactionManagement = new TransactionManagement(connection);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        DatabaseConnection.releaseConnection(connection);
    }

    @Test
    void testFullLibraryProcess() throws SQLException {
        // Add a book
        Book book = new Book(0, "Regression Test Book", "Test Author", "9876543210", true, 1);
        int bookId = bookManagement.addBook(book);
        assertNotEquals(0, bookId, "Book ID should not be 0 after adding");
        LOGGER.info("Added book with ID: " + bookId);

        // Retrieve the added book to ensure it exists
        Book addedBook = bookManagement.getBookById(bookId);
        assertNotNull(addedBook, "Added book should not be null");
        assertEquals("Regression Test Book", addedBook.getTitle());

        // Add a patron
        Patron patron = new Patron(0, "Regression Test Patron", "regressi@test.com", "9876543210");
        int patronId = patronManagement.addPatron(patron);
        assertNotEquals(0, patronId, "Patron ID should not be 0 after adding");
        LOGGER.info("Added patron with ID: " + patronId);

        // Retrieve the added patron to ensure it exists
        Patron addedPatron = patronManagement.getPatronById(patronId);
        assertNotNull(addedPatron, "Added patron should not be null");
        assertEquals("Regression Test Patron", addedPatron.getName());

        // Borrow the book
        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = borrowDate.plusDays(14);
        try {
            transactionManagement.borrowBook(bookId, patronId, borrowDate, returnDate, "Borrowed", "LIB002");
        } catch (SQLException e) {
            LOGGER.severe("Error borrowing book: " + e.getMessage());
            throw e;
        }



        // Return the book
        List<Transaction> transactions = transactionManagement.getAllTransactions();
        Transaction borrowTransaction = transactions.stream()
                .filter(t -> t.getBookId() == bookId && t.getPatronId() == patronId)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Transaction not found after borrowing"));

        transactionManagement.returnBook(borrowTransaction.getId(), "LIB002");

        // Verify book is available again
        Book returnedBook = bookManagement.getBookById(bookId);
        assertTrue(returnedBook.isAvailable());

        // Update book information
        returnedBook.setAuthor("Updated Author");
        bookManagement.updateBook(returnedBook);

        Book updatedBook = bookManagement.getBookById(bookId);
        assertEquals("Updated Author", updatedBook.getAuthor());

        // Clean up
        transactionManagement.deleteTransactionsForBook(bookId);
        transactionManagement.deleteTransactionsForPatron(patronId);
        bookManagement.deleteBook(bookId);
        patronManagement.deletePatron(patronId);

        // Verify deletion
        assertNull(bookManagement.getBookById(bookId));
        assertNull(patronManagement.getPatronById(patronId));
    }
}