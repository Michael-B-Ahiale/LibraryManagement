package com.abmike.controller;

import com.abmike.model.Patron;
import com.abmike.service.PatronManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CreatePatronController {

    private PatronManagement patronManagement;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private TableView<Patron> patronTable;
    @FXML private TableColumn<Patron, Integer> idColumn;
    @FXML private TableColumn<Patron, String> nameColumn;
    @FXML private TableColumn<Patron, String> emailColumn;
    @FXML private TableColumn<Patron, String> phoneColumn;

    private ObservableList<Patron> patronList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        patronTable.setItems(patronList);
    }

    public void setPatronManagement(PatronManagement patronManagement) {
        this.patronManagement = patronManagement;
        loadPatrons();
    }

    private void loadPatrons() {
        try {
            patronList.clear();
            patronList.addAll(patronManagement.getAllPatrons());
        } catch (SQLException e) {
            showAlert("Error", "Failed to load patrons.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSavePatron(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        Patron patron = new Patron(0, name, email, phone);
        try {
            patronManagement.addPatron(patron);
            showAlert("Success", "Patron added successfully.");
            clearFields();
            loadPatrons();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while adding the patron.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}