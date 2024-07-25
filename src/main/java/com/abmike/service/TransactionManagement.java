package com.abmike.service;

import com.abmike.model.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionManagement {

    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(TransactionManagement.class.getName());

    public TransactionManagement(Connection connection) {
        this.connection = connection;
    }

    public void borrowBook(int bookId, int patronId, LocalDate collectionDate, LocalDate returnDate, String returnStatus, String librarianCode) throws SQLException {
        // First, check if the book and patron exist
        if (!bookExists(bookId) || !patronExists(patronId)) {
            throw new SQLException("Book or Patron does not exist");
        }

        String query = "INSERT INTO transactions (bookId, patronId, collectionDate, returnDate, returnStatus, librarian_code) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.setInt(2, patronId);
            stmt.setDate(3, Date.valueOf(collectionDate));
            stmt.setDate(4, returnDate != null ? Date.valueOf(returnDate) : null);
            stmt.setString(5, returnStatus);
            stmt.setString(6, librarianCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error borrowing book", e);
            throw e;
        }
    }

    public void returnBook(int transactionId, String librarianCode) throws SQLException {
        String query = "UPDATE transactions SET returnDate = ?, returnStatus = ?, librarian_code = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setString(2, "Returned");
            stmt.setString(3, librarianCode);
            stmt.setInt(4, transactionId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating transaction failed, no rows affected.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error returning book", e);
            throw e;
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all transactions", e);
            throw e;
        }
        return transactions;
    }

    public List<Transaction> getTransactionsByPatronId(int patronId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE patronId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, patronId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(extractTransactionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting transactions by patron ID", e);
            throw e;
        }
        return transactions;
    }

    public List<Transaction> searchTransactions(String searchTerm) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE " +
                "bookId LIKE ? OR patronId LIKE ? OR librarian_code LIKE ? OR returnStatus LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String wildcardTerm = "%" + searchTerm + "%";
            for (int i = 1; i <= 4; i++) {
                stmt.setString(i, wildcardTerm);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(extractTransactionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching transactions", e);
            throw e;
        }
        return transactions;
    }

    public void deleteTransactionsForBook(int bookId) throws SQLException {
        String query = "DELETE FROM transactions WHERE bookId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting transactions for book", e);
            throw e;
        }
    }

    public void deleteTransactionsForPatron(int patronId) throws SQLException {
        String query = "DELETE FROM transactions WHERE patronId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, patronId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting transactions for patron", e);
            throw e;
        }
    }

    private Transaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("id"),
                rs.getInt("bookId"),
                rs.getInt("patronId"),
                rs.getDate("collectionDate").toLocalDate(),
                rs.getDate("returnDate") != null ? rs.getDate("returnDate").toLocalDate() : null,
                rs.getString("returnStatus"),
                rs.getString("librarian_code")
        );
    }

    private boolean bookExists(int bookId) throws SQLException {
        String query = "SELECT COUNT(*) FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean patronExists(int patronId) throws SQLException {
        String query = "SELECT COUNT(*) FROM patron WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, patronId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}