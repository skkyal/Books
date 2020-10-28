package com.example.books;

import android.content.Context;

import android.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String stringUrl;

    public BookLoader(Context context,String s){
        super(context);
        stringUrl=s;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if(stringUrl==null)
        return null;

        List<Book> books = QueryUtils.fetchBookData(stringUrl);
        return books;

    }
}
