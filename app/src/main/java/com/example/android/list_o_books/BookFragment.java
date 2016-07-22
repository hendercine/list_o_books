package com.example.android.list_o_books;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment {

    private BookAdapter adapter;
    private ArrayList<Book> books;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        books = new ArrayList<>();
        adapter = new BookAdapter(getActivity(), books);//
        adapter.clear();
        final View rootView = inflater.inflate(R.layout.book_list, container, false);
        // Inflate the layout for this fragment
        Button button = (Button) rootView.findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText) getActivity().findViewById(R.id.search_field);
                if (searchText.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.message), Toast.LENGTH_LONG).show();
                    searchText.setError(getString(R.string.no_input));
                } else {
                    String FetchBookData = searchText.getText().toString();
                    FetchBooksTask BooksTask = new FetchBooksTask();
                    ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        BooksTask.execute(FetchBookData);
                        adapter.clear();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(),
                                getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        ListView listView = (ListView) rootView.findViewById(R.id.list);//
        listView.setAdapter(adapter);//
        return rootView;
    }

    public class FetchBooksTask extends AsyncTask<String, Void, ArrayList<Book>> {

        private final String LOG_TAG = FetchBooksTask.class.getSimpleName();

        private ArrayList<Book> getBooksDataFromJson(String booksJsonStr) throws JSONException {
            final String VOLUME_ITEMS = getResources().getString(R.string.items);
            final String VOLUME_INFO = getResources().getString(R.string.volumeInfo);
            final String VOLUME_TITLE = getResources().getString(R.string.title);
            final String VOLUME_AUTHOR = getResources().getString(R.string.authors);
            final String INPUT_ERROR = getResources().getString(R.string.message);
            final String VOLUME_TOTAL_ITEMS = getResources().getString(R.string.totalItems);
            books = new ArrayList<>();
            JSONObject booksJson = new JSONObject(booksJsonStr);

            int totalItems = booksJson.optInt(VOLUME_TOTAL_ITEMS);
            if (totalItems == 0) {
                Toast.makeText(getActivity().getApplication(), INPUT_ERROR, Toast.LENGTH_LONG).show();
            } else {
                JSONArray booksArray = booksJson.optJSONArray(VOLUME_ITEMS);
                for (int i = 0; i < booksArray.length(); i++) {
                    String bookAuthor = getResources().getString(R.string.written_by);
                    String bookTitle;
                    JSONObject bookDataObject = booksArray.getJSONObject(i);
                    JSONObject volumeInfo = bookDataObject.getJSONObject(VOLUME_INFO);
                    bookTitle = volumeInfo.optString(VOLUME_TITLE);
                    StringBuilder authorBuild = new StringBuilder();
                    JSONArray authorArray = volumeInfo.getJSONArray(VOLUME_AUTHOR);
                    for (int a = 0; a < authorArray.length(); a++) {
                        if (a > 0)
                            authorBuild.append(", ");
                        authorBuild.append(authorArray.getString(a));
                    }
                    bookAuthor += authorBuild.toString();
                    books.add(i, new Book(bookTitle, bookAuthor));
                }
            }
            return books;
        }

        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String booksJsonStr = null;
            int numBooks = 15;

            try {
                final String FETCH_BOOKS_URL = getResources().getString(R.string.url) + params[0] + getResources().getString(R.string.projection);
                final String APP_ID_PARAM = getResources().getString(R.string.appId);
                final String MAX_RESULTS_PARAM = getResources().getString(R.string.maxResults);

                Uri builtUri = Uri.parse(FETCH_BOOKS_URL).buildUpon()
                        .appendQueryParameter(MAX_RESULTS_PARAM, String.valueOf(numBooks))
                        .appendQueryParameter(APP_ID_PARAM, BuildConfig.OPEN_GOOGLE_BOOKS_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString().replace(" ", "%20"));

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                booksJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.v(LOG_TAG, "ERROR", e);
                Log.e(LOG_TAG, e.getMessage(), e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getBooksDataFromJson(booksJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if (books != null) {
                adapter.addAll(books);
            }

        }

    }

}
