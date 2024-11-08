package com.abmike;

import com.abmike.model.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    @Test
    void testTransactionConstructorAndGetters() {
        LocalDate collectionDate = LocalDate.now();
        LocalDate returnDate = collectionDate.plusDays(14);
        Transaction transaction = new Transaction(1, 101, 201, collectionDate, returnDate, "Borrowed", "LIB001");

        assertEquals(1, transaction.getId());
        assertEquals(101, transaction.getBookId());
        assertEquals(201, transaction.getPatronId());
        assertEquals(collectionDate, transaction.getCollectionDate());
        assertEquals(returnDate, transaction.getReturnDate());
        assertEquals("Borrowed", transaction.getStatus());
        assertEquals("LIB001", transaction.getLibrarianCode());
    }

    @Test
    void testTransactionSetters() {
        Transaction transaction = new Transaction(1, 101, 201, LocalDate.now(), LocalDate.now().plusDays(14), "Borrowed", "LIB001");

        transaction.setId(2);
        transaction.setBookId(102);
        transaction.setPatronId(202);
        LocalDate newCollectionDate = LocalDate.now().minusDays(1);
        LocalDate newReturnDate = LocalDate.now().plusDays(13);
        transaction.setCollectionDate(newCollectionDate);
        transaction.setReturnDate(newReturnDate);
        transaction.setStatus("Returned");
        transaction.setLibrarianCode("LIB002");

        assertEquals(2, transaction.getId());
        assertEquals(102, transaction.getBookId());
        assertEquals(202, transaction.getPatronId());
        assertEquals(newCollectionDate, transaction.getCollectionDate());
        assertEquals(newReturnDate, transaction.getReturnDate());
        assertEquals("Returned", transaction.getStatus());
        assertEquals("LIB002", transaction.getLibrarianCode());
    }
}
