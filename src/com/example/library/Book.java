package com.example.library;

import javafx.beans.property.*;

// Encapsulation: All fields are private and accessed through public methods
public class Book {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty isbn;
    private final BooleanProperty available;
    private final IntegerProperty count;

    // Constructor: Encapsulation and initialization
    public Book(int id, String title, String author, String isbn, boolean available, int count) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.available = new SimpleBooleanProperty(available);
        this.count = new SimpleIntegerProperty(count);
    }

    // Getter and setter methods: Encapsulation
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getTitle() { return title.get(); }
    public void setTitle(String title) { this.title.set(title); }
    public StringProperty titleProperty() { return title; }

    public String getAuthor() { return author.get(); }
    public void setAuthor(String author) { this.author.set(author); }
    public StringProperty authorProperty() { return author; }

    public String getIsbn() { return isbn.get(); }
    public void setIsbn(String isbn) { this.isbn.set(isbn); }
    public StringProperty isbnProperty() { return isbn; }

    public boolean isAvailable() { return available.get(); }
    public void setAvailable(boolean available) { this.available.set(available); }
    public BooleanProperty availableProperty() { return available; }

    public int getCount() { return count.get(); }
    public void setCount(int count) { this.count.set(count); }
    public IntegerProperty countProperty() { return count; }

    // Polymorphism: This method can be overridden in subclasses
    public String getItemType() {
        return "Book";
    }

    // Encapsulation: Providing a controlled way to check out a book
    public boolean checkOut() {
        if (isAvailable() && getCount() > 0) {
            setCount(getCount() - 1);
            if (getCount() == 0) {
                setAvailable(false);
            }
            return true;
        }
        return false;
    }

    // Encapsulation: Providing a controlled way to return a book
    public void returnBook() {
        setCount(getCount() + 1);
        setAvailable(true);
    }

    // Overriding Object's toString method: Polymorphism
    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", isbn='" + getIsbn() + '\'' +
                ", available=" + isAvailable() +
                ", count=" + getCount() +
                '}';
    }
}