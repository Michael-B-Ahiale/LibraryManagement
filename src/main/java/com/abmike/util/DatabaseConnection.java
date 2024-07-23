package com.abmike.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "mike";
    private static final int MAX_CONNECTIONS = 10; // Maximum number of connections in the pool

    private static List<Connection> connectionPool = new ArrayList<>();

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    // Method to get a connection from the pool
    public static synchronized Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            // If no connections are available, create a new one
            return createNewConnection();
        } else {
            // Remove and return the last connection from the pool
            return connectionPool.remove(connectionPool.size() - 1);
        }
    }

    // Method to return a connection to the pool
    public static synchronized void releaseConnection(Connection connection) throws SQLException {
        if (connectionPool.size() < MAX_CONNECTIONS) {
            connectionPool.add(connection);
        } else {
            // If the pool is full, close the connection
            connection.close();
        }
    }

    // Private method to create a new database connection
    private static Connection createNewConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found", e);
        }
    }

    // Method to close all connections in the pool
    public static void closeAllConnections() throws SQLException {
        for (Connection conn : connectionPool) {
            conn.close();
        }
        connectionPool.clear();
    }
}