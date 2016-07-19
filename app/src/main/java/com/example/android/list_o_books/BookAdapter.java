package com.example.android.list_o_books;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hendercine on 7/19/16.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView bookNameTextView = (TextView) listItemView.findViewById(R.id.text_title);
        bookNameTextView.setText(currentBook.getBookName());

        TextView bookAuthorTextView = (TextView) listItemView.findViewById(R.id.text_author);
        bookAuthorTextView.setText(currentBook.getBookAuthor());

        return listItemView;
    }

}