package com.abmike;

import com.abmike.model.Transaction;
import com.abmike.service.TransactionManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        transactionManagement = new TransactionManagement(mockConnection);
    }

    @Test
    void testSearchTransactions() throws SQLException {
        // Arrange
        String searchTerm = "test";
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getInt("bookId")).thenReturn(101, 102);
        when(mockResultSet.getInt("patronId")).thenReturn(201, 202);
        when(mockResultSet.getDate("collectionDate")).thenReturn(Date.valueOf(LocalDate.now()));
        when(mockResultSet.getDate("returnDate")).thenReturn(Date.valueOf(LocalDate.now().plusDays(7)));
        when(mockResultSet.getString("returnStatus")).thenReturn("Borrowed", "Returned");
        when(mockResultSet.getString("librarian_code")).thenReturn("LIB001", "LIB002");

        // Act
        List<Transaction> result = transactionManagement.searchTransactions(searchTerm);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockPreparedStatement, times(4)).setString(anyInt(), eq("%" + searchTerm + "%"));
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testSearchTransactionsNoResults() throws SQLException {
        // Arrange
        String searchTerm = "nonexistent";
        when(mockResultSet.next()).thenReturn(false);

        // Act
        List<Transaction> result = transactionManagement.searchTransactions(searchTerm);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTransactionsByPatronId() throws SQLException {
        // Arrange
        int patronId = 123;
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getInt("bookId")).thenReturn(101, 102);
        when(mockResultSet.getInt("patronId")).thenReturn(patronId, patronId);
        when(mockResultSet.getDate("collectionDate")).thenReturn(Date.valueOf(LocalDate.now()));
        when(mockResultSet.getDate("returnDate")).thenReturn(Date.valueOf(LocalDate.now().plusDays(7)));
        when(mockResultSet.getString("returnStatus")).thenReturn("Borrowed", "Returned");
        when(mockResultSet.getString("librarian_code")).thenReturn("LIB001", "LIB002");

        // Act
        List<Transaction> result = transactionManagement.getTransactionsByPatronId(patronId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockPreparedStatement).setInt(1, patronId);
        verify(mockPreparedStatement).executeQuery();
    }

    @Test
    void testGetTransactionsByPatronIdNoResults() throws SQLException {
        // Arrange
        int patronId = 456;
        when(mockResultSet.next()).thenReturn(false);

        // Act
        List<Transaction> result = transactionManagement.getTransactionsByPatronId(patronId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchTransactionsSQLException() throws SQLException {
        // Arrange
        String searchTerm = "test";
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(SQLException.class, () -> transactionManagement.searchTransactions(searchTerm));
    }

    @Test
    void testGetTransactionsByPatronIdSQLException() throws SQLException {
        // Arrange
        int patronId = 123;
        when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(SQLException.class, () -> transactionManagement.getTransactionsByPatronId(patronId));
    }
}
