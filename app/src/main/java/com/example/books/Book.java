package com.example.books;

public class Book {
    private String bookTitle;
    private String bookAuthor;
    private double bookRating;
    private String bookInfoUrl;

    public Book(String bookTitle, String bookAuthor, String bookInfoUrl) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookInfoUrl = bookInfoUrl;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookInfoUrl(){
        return bookInfoUrl;
    }

    public double getBookRating() {
        return bookRating;
    }
}
