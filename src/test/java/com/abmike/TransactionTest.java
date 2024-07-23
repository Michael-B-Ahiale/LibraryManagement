package com.abmike;

import com.abmike.model.Transaction;
import com.abmike.service.TransactionManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionTest {
    @Test
    void testTransactionConstructorAndGetters() {
        LocalDate collectionDate = LocalDate.now();
        LocalDate returnDate = collectionDate.plusDays(14);
        Transaction transaction = new Transaction(1, 101, 201, collectionDate, returnDate, "Borrowed", "LIB001");

        assertEquals(1, transaction.getId());
        assertEquals(101, transaction.getBookId());
        assertEquals(201, transaction.getPatronId());
        assertEquals(collectionDate, transaction.getCollectionDate());
        assertEquals(returnDate, transaction.getReturnDate());
        assertEquals("Borrowed", transaction.getStatus());
        assertEquals("LIB001", transaction.getLibrarianCode());
    }

    @Test
    void testTransactionSetters() {
        Transaction transaction = new Transaction(1, 101, 201, LocalDate.now(), LocalDate.now().plusDays(14), "Borrowed", "LIB001");

        transaction.setId(2);
        transaction.setBookId(102);
        transaction.setPatronId(202);
        LocalDate newCollectionDate = LocalDate.now().minusDays(1);
        LocalDate newReturnDate = LocalDate.now().plusDays(13);
        transaction.setCollectionDate(newCollectionDate);
        transaction.setReturnDate(newReturnDate);
        transaction.setStatus("Returned");
        transaction.setLibrarianCode("LIB002");

        assertEquals(2, transaction.getId());
        assertEquals(102, transaction.getBookId());
        assertEquals(202, transaction.getPatronId());
        assertEquals(newCollectionDate, transaction.getCollectionDate());
        assertEquals(newReturnDate, transaction.getReturnDate());
        assertEquals("Returned", transaction.getStatus());
        assertEquals("LIB002", transaction.getLibrarianCode());
    }
}

class TransactionManagementTest {
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private TransactionManagement transactionManagement;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        transactionManagement = new TransactionManagement(mockConnection);
    }

    @Test
    void testBorrowBook() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        LocalDate collectionDate = LocalDate.now();
        LocalDate returnDate = collectionDate.plusDays(14);
        transactionManagement.borrowBook(101, 201, collectionDate, returnDate, "Borrowed", "LIB001");

        verify(mockPreparedStatement).setInt(1, 101);
        verify(mockPreparedStatement).setInt(2, 201);
        verify(mockPreparedStatement).setDate(3, java.sql.Date.valueOf(collectionDate));
        verify(mockPreparedStatement).setDate(4, java.sql.Date.valueOf(returnDate));
        verify(mockPreparedStatement).setString(5, "Borrowed");
        verify(mockPreparedStatement).setString(6, "LIB001");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testReturnBook() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        transactionManagement.returnBook(1, "LIB001");

        verify(mockPreparedStatement).setDate(1, java.sql.Date.valueOf(LocalDate.now()));
        verify(mockPreparedStatement).setString(2, "Returned");
        verify(mockPreparedStatement).setString(3, "LIB001");
        verify(mockPreparedStatement).setInt(4, 1);
        verify(mockPreparedStatement).executeUpdate();
    }

}