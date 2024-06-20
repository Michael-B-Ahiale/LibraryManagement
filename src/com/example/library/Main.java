package com.example.library;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        BookManagement bookManagement = new BookManagement();

        // Add a new book
        Book book1 = new Book(0, "The Great Gatsby", "F. Scott Fitzgerald", "1234567890", true);
        bookManagement.addBook(book1);

        // Add another book
        Book book2 = new Book(0, "To Kill a Mockingbird", "Harper Lee", "1234567891", true);
        bookManagement.addBook(book2);

        // Get a book by ID
        Book retrievedBook = bookManagement.getBook(1);
        if (retrievedBook != null) {
            System.out.println("Retrieved Book: " + retrievedBook.getTitle());
        }

        // Update a book
        if (retrievedBook != null) {
            retrievedBook.setTitle("The Great Gatsby (Updated)");
            bookManagement.updateBook(retrievedBook);

            // Verify update
            Book updatedBook = bookManagement.getBook(1);
            if (updatedBook != null) {
                System.out.println("Updated Book: " + updatedBook.getTitle());
            }
        }

        // Delete a book
        bookManagement.deleteBook(2);

        // Get all books
        List<Book> books = bookManagement.getAllBooks();
        System.out.println("Books in Library:");
        for (Book book : books) {
            System.out.println(book.getTitle());
        }
    }
}
