package com.example.books;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.app.LoaderManager.LoaderCallbacks;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public String BOOKS_API= "https://www.googleapis.com/books/v1/volumes?q=";

    private EditText searchName;
    private ImageButton searchButton;
    private ListView listView;
    private ArrayList<Book>bookList;
    private BookAdapter bookAdapter;
    private TextView empty;
    private ProgressBar progressBar;
    private TextView start;
    ConnectivityManager cm;
    Network n;

    InputMethodManager inputMethodManager;

    LoaderManager loaderManager = getLoaderManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchName = (EditText)findViewById(R.id.search_text);
        searchButton = (ImageButton) findViewById(R.id.search_button);
        listView = (ListView) findViewById(R.id.list);
        start = (TextView) findViewById(R.id.start_view);
        bookAdapter = new BookAdapter(this,new ArrayList<Book>());
        listView.setAdapter(bookAdapter);

        empty = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(empty);

        progressBar = (ProgressBar) findViewById(R.id.progresss);
        progressBar.setVisibility(View.GONE);

        cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        //when search button clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        //Search when enter is clicked
        searchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    search();
                }
                return true;
            }
        });

        //listView item click listener
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book current = bookAdapter.getItem(i);
                Uri bookInfoUrl = Uri.parse(current.getBookInfoUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookInfoUrl);
                startActivity(websiteIntent);
            }
            //Handled through CardView
        });*/
    }
    public void search() {
        //Hide the keyboard on button Click
        inputMethodManager.hideSoftInputFromWindow(searchButton.getApplicationWindowToken(),0);

        String check = searchName.getText().toString().trim();
        if(TextUtils.isEmpty(check)){
            Toast.makeText(getApplicationContext(),"Enter a Book Name",Toast.LENGTH_SHORT).show();
            return;
        }
        //start.setVisibility(View.GONE);
        if(loaderManager!=null)
            loaderManager.destroyLoader(1);
        loaderManager.initLoader(1, null, MainActivity.this);

        //Make ProgressBar Invisible
        progressBar.setVisibility(View.VISIBLE);
        empty.setText("");

        start.setVisibility(View.GONE);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        String s = searchName.getText().toString().trim();
        Log.i("message from creater","created");
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.googleapis.com")
                .appendPath("books")
                .appendPath("v1")
                .appendPath("volumes")
                .appendQueryParameter("q",s)
                .appendQueryParameter("maxResults", "40");
        String myUrl = builder.build().toString();
        return new BookLoader(this,myUrl);

    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        progressBar.setVisibility(View.GONE);
        bookAdapter.clear();
        if (books != null && !books.isEmpty()) {
            bookAdapter.addAll(books);
        }
        else{
            //Check Internet Connection and Is Books are returned
            n = cm.getActiveNetwork();
            if(n == null)
                empty.setText("No Internet Connection");
            else {
                final NetworkCapabilities nc = cm.getNetworkCapabilities(n);
                boolean connect = nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                if (connect == false){
                    empty.setText("No Internet Connection");
                }
                else
                    empty.setText("No Book Found");
            }
        }
        Log.i("message from finish","finished");
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookAdapter.clear();
        Log.i("message from reset","reset");
    }

    //When Screen Rotates
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!bookAdapter.isEmpty() ||(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            bookAdapter.notifyDataSetChanged();
            Log.i("orientation changed", "changed");
        }

    }
}
