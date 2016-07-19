package com.example.android.list_o_books;

/**
 * Created by Hendercine on 7/19/16.
 */
public class Book {

    private String mBookName;
    private String mBookAuthor;

    public Book() {
    }

    public Book(String bookName, String bookAuthor) {
        mBookName = bookName;
        mBookAuthor = bookAuthor;
    }

    public String getBookName() {
        return mBookName;
    }

    public String getBookAuthor() {
        return mBookAuthor;
    }

}

