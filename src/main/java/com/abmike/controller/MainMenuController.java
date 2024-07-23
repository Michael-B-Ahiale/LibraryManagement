package com.abmike.controller;

import com.abmike.service.BookManagement;
import com.abmike.service.PatronManagement;
import com.abmike.service.TransactionManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private VBox contentArea;

    @FXML
    private Button manageBooksBtn;

    @FXML
    private Button createPatronBtn;

    @FXML
    private Button transactionsBtn;

    private BookManagement bookManagement;
    private PatronManagement patronManagement;
    private TransactionManagement transactionManagement;

    public void setBookManagement(BookManagement bookManagement) {
        this.bookManagement = bookManagement;
    }

    public void setPatronManagement(PatronManagement patronManagement) {
        this.patronManagement = patronManagement;
    }

    public void setTransactionManagement(TransactionManagement transactionManagement) {
        this.transactionManagement = transactionManagement;
    }

    @FXML
    private void handleManageBooks(ActionEvent event) throws IOException {
        loadContent("/BookManager.fxml");
        setActiveButton(manageBooksBtn);
    }

    @FXML
    private void handleCreatePatron(ActionEvent event) throws IOException {
        loadContent("/CreatePatron.fxml");
        setActiveButton(createPatronBtn);
    }

    @FXML
    private void handleTransactions(ActionEvent event) throws IOException {
        loadContent("/TransactionPage.fxml");
        setActiveButton(transactionsBtn);
    }

    private void loadContent(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();

        if (fxml.equals("/BookManager.fxml")) {
            BookManagerController controller = loader.getController();
            controller.setBookManagement(bookManagement);
        } else if (fxml.equals("/CreatePatron.fxml")) {
            CreatePatronController controller = loader.getController();
            controller.setPatronManagement(patronManagement);
        } else if (fxml.equals("/TransactionPage.fxml")) {
            TransactionController controller = loader.getController();
            controller.setTransactionManagement(transactionManagement);
        }

        contentArea.getChildren().clear();
        contentArea.getChildren().add(root);
    }

    private void setActiveButton(Button activeButton) {
        String inactiveStyle = "-fx-background-color: #f8f9fa; -fx-text-fill: #007bff; -fx-border-color: #007bff; -fx-border-radius: 5;";
        String activeStyle = "-fx-background-color: #007bff; -fx-text-fill: white; -fx-border-color: #007bff; -fx-border-radius: 5;";

        manageBooksBtn.setStyle(inactiveStyle);
        createPatronBtn.setStyle(inactiveStyle);
        transactionsBtn.setStyle(inactiveStyle);
        activeButton.setStyle(activeStyle);
    }
}