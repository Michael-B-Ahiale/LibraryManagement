package com.abmike;

import com.abmike.model.Book;
import com.abmike.service.BookManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookManagementExceptionTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    private BookManagement bookManagement;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        bookManagement = new BookManagement(mockConnection);
    }


    @Test
    void testGetBookByIdNotFound() throws SQLException {
        // Simulate no results returned from the query
        when(mockPreparedStatement.executeQuery()).thenReturn(mock(ResultSet.class));

        // Verify that the method returns null when book is not found
        assertNull(bookManagement.getBookById(1));
    }
}