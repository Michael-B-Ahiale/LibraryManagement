package com.example.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class BookManagerController {

    private BookManagement bookManagement;

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField isbnField;
    @FXML private TextField countField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, Boolean> availabilityColumn;
    @FXML private TableColumn<Book, Integer> countColumn;

    private boolean isEditMode = false;

    public void setBookManagement(BookManagement bookManagement) {
        this.bookManagement = bookManagement;
        loadBooks();
    }

    @FXML
    private void initialize() {
        System.out.println("Initializing BookManagerController...");
        System.out.println("TitleField: " + titleField);
        System.out.println("AuthorField: " + authorField);
        System.out.println("ISBNField: " + isbnField);
        System.out.println("CountField: " + countField);

        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        availabilityColumn.setCellValueFactory(cellData -> cellData.getValue().availableProperty());
        countColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty().asObject());

        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    @FXML
    private void handleAddNewBook(ActionEvent event) {
        clearFields();
        isEditMode = false;
        saveButton.setText("Add Book");
    }

    @FXML
    private void handleSaveBook(ActionEvent event) {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        int count;

        try {
            count = Integer.parseInt(countField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Count must be an integer.");
            return;
        }

        if (isEditMode) {
            Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
            selectedBook.setTitle(title);
            selectedBook.setAuthor(author);
            selectedBook.setIsbn(isbn);
            selectedBook.setCount(count);

            try {
                bookManagement.updateBook(selectedBook);
                showAlert("Success", "Book updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while updating the book.");
            }
        } else {
            Book book = new Book(0, title, author, isbn, true, count);
            try {
                bookManagement.addBook(book);
                showAlert("Success", "Book added successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while adding the book.");
            }
        }

        clearFields();
        loadBooks();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleEditBook(ActionEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            populateForm(selectedBook);
            isEditMode = true;
            saveButton.setText("Update Book");
        } else {
            showAlert("No Selection", "Please select a book to edit.");
        }
    }

    @FXML
    private void handleDeleteBook(ActionEvent event) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                bookManagement.deleteBook(selectedBook.getId());
                showAlert("Success", "Book deleted successfully.");
                loadBooks();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while deleting the book.");
            }
        } else {
            showAlert("No Selection", "Please select a book to delete.");
        }
    }

    private void populateForm(Book book) {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        countField.setText(String.valueOf(book.getCount()));
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        countField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loadBooks() {
        try {
            List<Book> books = bookManagement.getAllBooks();
            bookTable.getItems().setAll(books);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while loading the books.");
        }
    }
}