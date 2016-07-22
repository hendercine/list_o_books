package com.example.android.list_o_books;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hendercine on 7/19/16.
 */
public class Book implements Parcelable {

    private String mBookTitle;
    private String mBookAuthor;

    public Book(String bookTitle, String bookAuthor) {
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
    }

    private Book(Parcel in) {
        mBookTitle = in.readString();
        mBookAuthor = in.readString();
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getBookAuthor() {
        return mBookAuthor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mBookTitle);
        out.writeString(mBookAuthor);
    }
    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}

