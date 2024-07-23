package com.abmike;

import com.abmike.model.Book;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookConstructorAndGetters() {
        Book book = new Book(1, "Test Book", "Test Author", "1234567890", true, 5);

        assertEquals(1, book.getId());
        assertEquals("Test Book", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("1234567890", book.getIsbn());
        assertTrue(book.isAvailable());
        assertEquals(5, book.getCount());
    }

    @Test
    void testSetters() {
        Book book = new Book(1, "Test Book", "Test Author", "1234567890", true, 5);

        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setIsbn("0987654321");
        book.setAvailable(false);
        book.setCount(3);

        assertEquals("New Title", book.getTitle());
        assertEquals("New Author", book.getAuthor());
        assertEquals("0987654321", book.getIsbn());
        assertFalse(book.isAvailable());
        assertEquals(3, book.getCount());
    }

    @Test
    void testCheckOut() {
        Book book = new Book(1, "Test Book", "Test Author", "1234567890", true, 2);

        assertTrue(book.checkOut());
        assertEquals(1, book.getCount());
        assertTrue(book.isAvailable());

        assertTrue(book.checkOut());
        assertEquals(0, book.getCount());
        assertFalse(book.isAvailable());

        assertFalse(book.checkOut());  // Should fail as no more books available
    }

    @Test
    void testReturnBook() {
        Book book = new Book(1, "Test Book", "Test Author", "1234567890", false, 0);

        book.returnBook();
        assertEquals(1, book.getCount());
        assertTrue(book.isAvailable());
    }
}