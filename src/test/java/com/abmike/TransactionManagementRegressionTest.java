package com.abmike;

import com.abmike.model.Transaction;
import com.abmike.service.TransactionManagement;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TransactionManagementRegressionTest {
    private static Connection connection;
    private TransactionManagement transactionManagement;

    @BeforeAll
    static void setUpClass() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        connection.close();
    }

    @BeforeEach
    void setUp() {
        transactionManagement = new TransactionManagement(connection);
    }

    @Test
    void testCompleteTransactionLifecycle() throws SQLException {
        // Borrow a book
        LocalDate collectionDate = LocalDate.now();
        LocalDate returnDate = collectionDate.plusDays(14);
        transactionManagement.borrowBook(101, 201, collectionDate, returnDate, "Borrowed", "LIB001");

        // Verify the transaction
        List<Transaction> transactions = transactionManagement.getAllTransactions();
        assertEquals(1, transactions.size());
        Transaction transaction = transactions.get(0);

        // Return the book
        transactionManagement.returnBook(transaction.getId(), "LIB002");

        // Verify the updated transaction
        transactions = transactionManagement.getAllTransactions();
        assertEquals(1, transactions.size());
        transaction = transactions.get(0);
        assertEquals("Returned", transaction.getStatus());

        // Borrow another book
        transactionManagement.borrowBook(102, 201, collectionDate, returnDate, "Borrowed", "LIB003");

        // Verify total transactions
        transactions = transactionManagement.getAllTransactions();
        assertEquals(2, transactions.size());

        // Search for transactions
        List<Transaction> searchResults = transactionManagement.searchTransactions("LIB003");
        assertEquals(1, searchResults.size());
        assertEquals("LIB003", searchResults.get(0).getLibrarianCode());

        // Get transactions by patron
        List<Transaction> patronTransactions = transactionManagement.getTransactionsByPatronId(201);
        assertEquals(2, patronTransactions.size());
        assertTrue(patronTransactions.stream().allMatch(t -> t.getPatronId() == 201));
    }

    @Test
    void testEdgeCases() throws SQLException {
        // Test borrowing with past return date
        LocalDate pastDate = LocalDate.now().minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> {
            transactionManagement.borrowBook(103, 202, LocalDate.now(), pastDate, "Borrowed", "LIB001");
        });

        // Test returning a non-existent transaction
        assertThrows(SQLException.class, () -> {
            transactionManagement.returnBook(999, "LIB001");
        });

        // Test searching with empty string
        List<Transaction> emptySearchResults = transactionManagement.searchTransactions("");
        assertEquals(0, emptySearchResults.size());

        // Test getting transactions for non-existent patron
        List<Transaction> nonExistentPatronTransactions = transactionManagement.getTransactionsByPatronId(999);
        assertEquals(0, nonExistentPatronTransactions.size());
    }

    @Test
    void testPerformance() throws SQLException {
        // Add a large number of transactions
        LocalDate collectionDate = LocalDate.now();
        LocalDate returnDate = collectionDate.plusDays(14);
        for (int i = 0; i < 1000; i++) {
            transactionManagement.borrowBook(i, i % 100, collectionDate, returnDate, "Borrowed", "LIB" + (i % 10));
        }

        // Measure time for getAllTransactions
        long startTime = System.nanoTime();
        List<Transaction> allTransactions = transactionManagement.getAllTransactions();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;  // Convert to milliseconds

        assertEquals(1000, allTransactions.size());
        assertTrue(duration < 1000, "getAllTransactions took too long: " + duration + "ms");

        // Measure time for searchTransactions
        startTime = System.nanoTime();
        List<Transaction> searchResults = transactionManagement.searchTransactions("LIB5");
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1_000_000;

        assertEquals(100, searchResults.size());
        assertTrue(duration < 500, "searchTransactions took too long: " + duration + "ms");
    }
}
