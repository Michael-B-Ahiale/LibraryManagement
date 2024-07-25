package com.abmike.service;

import com.abmike.model.Patron;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatronManagement {

    private Connection connection;

    public PatronManagement(Connection connection) {
        this.connection = connection;
    }

    public int addPatron(Patron patron) throws SQLException {
        String query = "INSERT INTO patron (name, email, phoneNumber) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patron.getName());
            stmt.setString(2, patron.getEmail());
            stmt.setString(3, patron.getPhone());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating patron failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    patron.setId(id);
                    return id;
                } else {
                    throw new SQLException("Creating patron failed, no ID obtained.");
                }
            }
        }
    }

    public void updatePatron(Patron patron) throws SQLException {
        String query = "UPDATE patron SET name = ?, email = ?, phoneNumber = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patron.getName());
            stmt.setString(2, patron.getEmail());
            stmt.setString(3, patron.getPhone());
            stmt.setInt(4, patron.getId());
            stmt.executeUpdate();
        }
    }

    public void deletePatron(int id) throws SQLException {
        String query = "DELETE FROM patron WHERE id = ?";
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
