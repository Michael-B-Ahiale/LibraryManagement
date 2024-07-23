package com.abmike;

import com.abmike.model.Book;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class BookParameterizedTest {

    @ParameterizedTest
    @CsvSource({
            "1, Test Book 1, Author 1, ISBN1, true, 5",
            "2, Test Book 2, Author 2, ISBN2, false, 0",
            "3, Test Book 3, Author 3, ISBN3, true, 10"
    })
    void testBookConstructorWithDifferentInputs(int id, String title, String author, String isbn, boolean available, int count) {
        Book book = new Book(id, title, author, isbn, available, count);

        assertEquals(id, book.getId());
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals(isbn, book.getIsbn());
        assertEquals(available, book.isAvailable());
        assertEquals(count, book.getCount());
    }

    @ParameterizedTest
    @CsvSource({
            "5, true, true, 4, true",
            "1, true, true, 0, false",
            "0, false, false, 0, false"
    })
    void testCheckOutWithDifferentScenarios(int initialCount, boolean initialAvailability, boolean expectedCheckOutResult, int expectedFinalCount, boolean expectedFinalAvailability) {
        Book book = new Book(1, "Test Book", "Test Author", "1234567890", initialAvailability, initialCount);

        assertEquals(expectedCheckOutResult, book.checkOut());
        assertEquals(expectedFinalCount, book.getCount());
        assertEquals(expectedFinalAvailability, book.isAvailable());
    }
}