package com.example.android.list_o_books;

/**
 * Created by Hendercine on 7/19/16.
 */
public class Book {

    private String mBookTitle;
    private String mBookAuthor;

    public Book(String bookTitle, String bookAuthor) {
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getBookAuthor() {
        return mBookAuthor;
    }

}

