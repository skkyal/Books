package com.example.books;

public class Book {
    private String bookTitle;
    private String bookAuthor;
    private String bookImage;
    private String bookInfoUrl;

    public Book(String bookTitle, String bookAuthor, String bookInfoUrl, String bookImage) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookInfoUrl = bookInfoUrl;
        this.bookImage = bookImage;
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

    public String getBookImage() {
        return bookImage;
    }


}
