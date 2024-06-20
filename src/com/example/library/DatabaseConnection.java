package com.example.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/library_db";
    private static final String jdbcUser = "root"; // Replace with your MySQL username
    private static final String jdbcPassword = ""; // Replace with your MySQL password

    public static void main(String[] args) {
        try {
            // Step 1: Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Establish Java MySQL connection
            System.out.println("Connecting to database...");
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

            if (connection != null) {
                System.out.println("Connected to the database!");
                connection.close(); // Close the connection
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console");
            e.printStackTrace();
        }
    }
}
