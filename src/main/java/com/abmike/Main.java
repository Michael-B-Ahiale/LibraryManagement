package com.abmike;

import com.abmike.controller.MainMenuController;
import com.abmike.service.BookManagement;
import com.abmike.service.PatronManagement;
import com.abmike.service.TransactionManagement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "mike";

    private Connection connection;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            establishConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
        Parent root = loader.load();
        MainMenuController controller = loader.getController();
        controller.setBookManagement(new BookManagement(connection));
        controller.setPatronManagement(new PatronManagement(connection));
        controller.setTransactionManagement(new TransactionManagement(connection));

        Scene scene = new Scene(root, 800, 600); // Set the scene size to 800x600
        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true); // Allow resizing if needed
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private void establishConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}