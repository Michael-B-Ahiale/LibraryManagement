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

class TransactionManagementIntegrationTest {
    private static Connection connection;
    private TransactionManagement transactionManagement;

    @BeforeAll
    static void setUpClass() throws SQLException {
        // Use an in-memory H2 database for testing
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
    void testBorrowAndReturnBook() throws SQLException {
        // Borrow a book
        LocalDate collectionDate = LocalDate.now();
        LocalDate returnDate = collectionDate.plusDays(14);
        transactionManagement.borrowBook(101, 201, collectionDate, returnDate, "Borrowed", "LIB001");

        // Verify the transaction
        List<Transaction> transactions = transactionManagement.getAllTransactions();
        assertEquals(1, transactions.size());
        Transaction transaction = transactions.get(0);
        assertEquals(101, transaction.getBookId());
        assertEquals(201, transaction.getPatronId());
        assertEquals(collectionDate, transaction.getCollectionDate());
        assertEquals(returnDate, transaction.getReturnDate());
        assertEquals("Borrowed", transaction.getStatus());
        assertEquals("LIB001", transaction.getLibrarianCode());

        // Return the book
        transactionManagement.returnBook(transaction.getId(), "LIB002");

        // Verify the updated transaction
        transactions = transactionManagement.getAllTransactions();
        assertEquals(1, transactions.size());
        transaction = transactions.get(0);
        assertEquals("Returned", transaction.getStatus());
        assertEquals("LIB002", transaction.getLibrarianCode());
        assertEquals(LocalDate.now(), transaction.getReturnDate());
    }

    @Test
    void testGetTransactionsByPatronId() throws SQLException {
        // Borrow two books for the same patron
        LocalDate collectionDate = LocalDate.now();
        LocalDate returnDate = collectionDate.plusDays(14);
        transactionManagement.borrowBook(102, 202, collectionDate, returnDate, "Borrowed", "LIB001");
        transactionManagement.borrowBook(103, 202, collectionDate, returnDate, "Borrowed", "LIB001");

        // Verify transactions for the patron
        List<Transaction> transactions = transactionManagement.getTransactionsByPatronId(202);
        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().allMatch(t -> t.getPatronId() == 202));
    }

    @Test
    void testSearchTransactions() throws SQLException {
        // Borrow books with different librarian codes
        LocalDate collectionDate = LocalDate.now();
        LocalDate returnDate = collectionDate.plusDays(14);
        transactionManagement.borrowBook(104, 203, collectionDate, returnDate, "Borrowed", "LIB001");
        transactionManagement.borrowBook(105, 204, collectionDate, returnDate, "Borrowed", "LIB002");

        // Search for transactions with LIB001
        List<Transaction> transactions = transactionManagement.searchTransactions("LIB001");
        assertEquals(1, transactions.size());
        assertEquals("LIB001", transactions.get(0).getLibrarianCode());

        // Search for transactions with "Borrowed" status
        transactions = transactionManagement.searchTransactions("Borrowed");
        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().allMatch(t -> "Borrowed".equals(t.getStatus())));
    }
}
