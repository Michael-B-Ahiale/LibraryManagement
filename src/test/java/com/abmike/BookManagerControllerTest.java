//package com.abmike;
//
//import com.abmike.controller.BookManagerController;
//import com.abmike.model.Book;
//import com.abmike.service.BookManagement;
//import javafx.application.Platform;
//import javafx.embed.swing.JFXPanel;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.concurrent.CountDownLatch;
//
//import static org.mockito.Mockito.*;
//
//class BookManagerControllerTest {
//
//    @Mock
//    private BookManagement bookManagement;
//
//    private TextField titleField;
//    private TextField authorField;
//    private TextField isbnField;
//    private TextField countField;
//    private TableView<Book> bookTable;
//
//    private BookManagerController controller;
//
//    @BeforeAll
//    public static void setupJavaFX() {
//        // Initialize JavaFX toolkit
//        new JFXPanel();
//    }
//
//    @BeforeEach
//    void setUp() throws Exception {
//        MockitoAnnotations.openMocks(this);
//
//        CountDownLatch latch = new CountDownLatch(1);
//        Platform.runLater(() -> {
//            controller = new BookManagerController();
//            controller.setBookManagement(bookManagement);
//            titleField = new TextField();
//            authorField = new TextField();
//            isbnField = new TextField();
//            countField = new TextField();
//            bookTable = new TableView<>();
//
//            controller.titleField = titleField;
//            controller.authorField = authorField;
//            controller.isbnField = isbnField;
//            controller.countField = countField;
//            controller.bookTable = bookTable;
//
//            latch.countDown();
//        });
//        latch.await();
//    }
//
//    @Test
//    void testHandleSaveBook() throws SQLException {
//        Platform.runLater(() -> {
//            // Set up input fields
//            titleField.setText("Test Book");
//            authorField.setText("Test Author");
//            isbnField.setText("1234567890");
//            countField.setText("5");
//
//            // Mock bookManagement behavior
//            try {
//                doNothing().when(bookManagement).addBook(any(Book.class));
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            // Call the method to test
//            controller.handleSaveBook(null);
//
//            // Verify that addBook was called with correct parameters
//            try {
//                verify(bookManagement).addBook(argThat(book ->
//                        book.getTitle().equals("Test Book") &&
//                                book.getAuthor().equals("Test Author") &&
//                                book.getIsbn().equals("1234567890") &&
//                                book.getCount() == 5
//                ));
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            // Verify that fields were cleared
//            assert titleField.getText().isEmpty();
//            assert authorField.getText().isEmpty();
//            assert isbnField.getText().isEmpty();
//            assert countField.getText().isEmpty();
//        });
//    }
//
//    @Test
//    void testLoadBooks() throws SQLException {
//        // Mock the return value of getAllBooks
//        Book book1 = new Book(1, "Book 1", "Author 1", "ISBN1", true, 1);
//        Book book2 = new Book(2, "Book 2", "Author 2", "ISBN2", false, 0);
//        when(bookManagement.getAllBooks()).thenReturn(new LinkedList<>(Arrays.asList(book1, book2)));
//
//        Platform.runLater(() -> {
//            // Call the method to test
//            controller.loadBooks();
//
//            // Verify that the table was populated with the books
//            assert bookTable.getItems().size() == 2;
//            assert bookTable.getItems().contains(book1);
//            assert bookTable.getItems().contains(book2);
//        });
//    }
//}