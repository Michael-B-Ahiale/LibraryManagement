package com.abmike;

import com.abmike.model.Patron;
import com.abmike.service.PatronManagement;
import com.abmike.util.DatabaseConnection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PatronManagementTest {

    private PatronManagement patronManagement;
    private static Connection connection;
    private String testEmailSuffix;

    @BeforeAll
    static void setUpClass() throws SQLException {
        connection = DatabaseConnection.getConnection();
        connection.setAutoCommit(false);  // Turn off auto-commit
    }

    @BeforeEach
    void setUp() throws SQLException {
        patronManagement = new PatronManagement(connection);
        testEmailSuffix = UUID.randomUUID().toString() + ".test";
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Delete only the test data
        String deleteTestData = "DELETE FROM patron WHERE email LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteTestData)) {
            pstmt.setString(1, "%" + testEmailSuffix);
            pstmt.executeUpdate();
        }
        connection.commit();  // Commit the deletion of test data
    }

    @AfterAll
    static void tearDownClass() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.setAutoCommit(true);  // Reset auto-commit
            connection.close();
        }
    }

    private Patron createTestPatron(String name, String email, String phone) {
        return new Patron(0, name, email + "." + testEmailSuffix, phone);
    }

    @Test
    void testAddPatron() throws SQLException {
        Patron newPatron = createTestPatron("Kofi", "kofi@gmail.com", "0554192323");
        patronManagement.addPatron(newPatron);

        List<Patron> patrons = patronManagement.getAllPatrons();
        Patron addedPatron = patrons.stream()
                .filter(p -> p.getEmail().endsWith(testEmailSuffix))
                .findFirst()
                .orElse(null);
        assertNotNull(addedPatron);
        assertEquals("Kofi", addedPatron.getName());
    }

    @Test
    void testUpdatePatron() throws SQLException {
        Patron patron = createTestPatron("Jane", "jane@example.com", "9876543210");
        patronManagement.addPatron(patron);

        List<Patron> patrons = patronManagement.getAllPatrons();
        Patron addedPatron = patrons.stream()
                .filter(p -> p.getEmail().endsWith(testEmailSuffix))
                .findFirst()
                .orElse(null);
        assertNotNull(addedPatron);

        Patron updatedPatron = new Patron(addedPatron.getId(), "Jane Ama", "janeama@example.com" + "." + testEmailSuffix, "5555555555");
        patronManagement.updatePatron(updatedPatron);

        Patron retrievedPatron = patronManagement.getPatronById(addedPatron.getId());
        assertEquals("Jane Ama", retrievedPatron.getName());
        assertTrue(retrievedPatron.getEmail().startsWith("janeama@example.com"));
    }

    @Test
    void testDeletePatron() throws SQLException {
        Patron patron = createTestPatron("Alice", "alice@example.com", "1112223333");
        patronManagement.addPatron(patron);

        List<Patron> patrons = patronManagement.getAllPatrons();
        Patron addedPatron = patrons.stream()
                .filter(p -> p.getEmail().endsWith(testEmailSuffix))
                .findFirst()
                .orElse(null);
        assertNotNull(addedPatron);

        patronManagement.deletePatron(addedPatron.getId());

        patrons = patronManagement.getAllPatrons();
        boolean patronStillExists = patrons.stream()
                .anyMatch(p -> p.getEmail().endsWith(testEmailSuffix));
        assertFalse(patronStillExists);
    }
}