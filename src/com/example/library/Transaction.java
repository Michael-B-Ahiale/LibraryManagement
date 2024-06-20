package com.example.library;

import java.util.Date;

public class Transaction {
    private int id;
    private int patronId;
    private int bookId;
    private Date borrowDate;
    private Date returnDate;

    // Constructors
    public Transaction() {}

    public Transaction(int id, int patronId, int bookId, Date borrowDate, Date returnDate) {
        this.id = id;
        this.patronId = patronId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
