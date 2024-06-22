package com.example.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TransactionController {

    private TransactionManagement transactionManagement;

    @FXML private TextField patronIdField;
    @FXML private TextField bookIdField;
    @FXML private DatePicker collectionDatePicker;
    @FXML private DatePicker returnDatePicker;
    @FXML private TextField librarianCodeField;
    @FXML private Button borrowButton;
    @FXML private Button returnButton;
    @FXML private Button refreshButton;
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, Integer> bookIdColumn;
    @FXML private TableColumn<Transaction, Integer> patronIdColumn;
    @FXML private TableColumn<Transaction, LocalDate> collectionDateColumn;
    @FXML private TableColumn<Transaction, LocalDate> returnDateColumn;
    @FXML private TableColumn<Transaction, String> returnStatusColumn;

    public void setTransactionManagement(TransactionManagement transactionManagement) {
        this.transactionManagement = transactionManagement;
        initializeTable();
    }

    @FXML
    private void initialize() {
        collectionDatePicker.setValue(LocalDate.now());
        returnDatePicker.setValue(LocalDate.now().plusDays(14));
    }

    @FXML
    private void handleBorrowBook(ActionEvent event) {
        try {
            int patronId = Integer.parseInt(patronIdField.getText());
            int bookId = Integer.parseInt(bookIdField.getText());
            LocalDate collectionDate = collectionDatePicker.getValue();
            LocalDate returnDate = returnDatePicker.getValue();
            String librarianCode = librarianCodeField.getText();

            if (collectionDate == null || returnDate == null || librarianCode.isEmpty()) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            String returnStatus = "Borrowed";
            transactionManagement.borrowBook(bookId, patronId, collectionDate, returnDate, returnStatus, librarianCode);
            showAlert("Success", "Book borrowed successfully.");
            refreshTransactionTable();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Error", "Patron ID and Book ID must be numbers.");
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while borrowing the book.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReturnBook(ActionEvent event) {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction == null) {
            showAlert("Error", "Please select a transaction to return.");
            return;
        }

        try {
            String librarianCode = librarianCodeField.getText();
            if (librarianCode.isEmpty()) {
                showAlert("Error", "Please enter the librarian code.");
                return;
            }
            transactionManagement.returnBook(selectedTransaction.getId(), librarianCode);
            showAlert("Success", "Book returned successfully.");
            refreshTransactionTable();
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while returning the book.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        refreshTransactionTable();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshTransactionTable();
        } else {
            try {
                List<Transaction> transactions = transactionManagement.searchTransactions(searchTerm);
                ObservableList<Transaction> transactionData = FXCollections.observableArrayList(transactions);
                transactionTable.setItems(transactionData);
            } catch (SQLException e) {
                showAlert("Error", "An error occurred while searching transactions.");
                e.printStackTrace();
            }
        }
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        patronIdColumn.setCellValueFactory(new PropertyValueFactory<>("patronId"));
        collectionDateColumn.setCellValueFactory(new PropertyValueFactory<>("collectionDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        refreshTransactionTable();
    }

    private void refreshTransactionTable() {
        try {
            List<Transaction> transactions = transactionManagement.getAllTransactions();
            ObservableList<Transaction> transactionData = FXCollections.observableArrayList(transactions);
            transactionTable.setItems(transactionData);
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while fetching transactions.");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        patronIdField.clear();
        bookIdField.clear();
        collectionDatePicker.setValue(LocalDate.now());
        returnDatePicker.setValue(LocalDate.now().plusDays(14));
        librarianCodeField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}