package com.example.library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatronManagement {

    private Connection connection;

    public PatronManagement(Connection connection) {
        this.connection = connection;
    }

    public void addPatron(Patron patron) throws SQLException {
        String query = "INSERT INTO patron (name, email, phoneNumber) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patron.getName());
            stmt.setString(2, patron.getEmail());
            stmt.setString(3, patron.getPhone());
            stmt.executeUpdate();
        }
    }

    public void updatePatron(Patron patron) throws SQLException {
        String query = "UPDATE patrons SET name = ?, email = ?, phoneNumber = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patron.getName());
            stmt.setString(2, patron.getEmail());
            stmt.setString(3, patron.getPhone());
            stmt.setInt(4, patron.getId());
            stmt.executeUpdate();
        }
    }

    public void deletePatron(int id) throws SQLException {
        String query = "DELETE FROM patrons WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Patron> getAllPatrons() throws SQLException {
        List<Patron> patrons = new ArrayList<>();
        String query = "SELECT * FROM patron";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Patron patron = new Patron(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phoneNumber")
                );
                patrons.add(patron);
            }
        }
        return patrons;
    }

    public Patron getPatronById(int id) throws SQLException {
        String query = "SELECT * FROM patron WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patron(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phoneNumber")
                    );
                }
            }
        }
        return null;
    }
}
