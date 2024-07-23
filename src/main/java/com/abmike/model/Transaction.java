package com.abmike.model;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private int bookId;
    private int patronId;
    private LocalDate collectionDate;
    private LocalDate returnDate;
    private String status;
    private String librarianCode;

    public Transaction(int id, int bookId, int patronId, LocalDate collectionDate, LocalDate returnDate, String status, String librarianCode) {
        this.id = id;
        this.bookId = bookId;
        this.patronId = patronId;
        this.collectionDate = collectionDate;
        this.returnDate = returnDate;
        this.status = status;
        this.librarianCode = librarianCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public LocalDate getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLibrarianCode() {
        return librarianCode;
    }

    public void setLibrarianCode(String librarianCode) {
        this.librarianCode = librarianCode;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", patronId=" + patronId +
                ", collectionDate=" + collectionDate +
                ", returnDate=" + returnDate +
                ", returnStatus='" + status + '\'' +
                ", librarianCode='" + librarianCode + '\'' +
                '}';
    }
}