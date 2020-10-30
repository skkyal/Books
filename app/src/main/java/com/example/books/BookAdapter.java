package com.example.books;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class BookAdapter extends ArrayAdapter<Book> {
    Context context;
    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context,0,books);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item,parent,false);
        }
        final Book currentBook = getItem(position);

        TextView title = (TextView)listItemView.findViewById(R.id.book_title);
        title.setText(currentBook.getBookTitle());

        TextView author = (TextView)listItemView.findViewById(R.id.book_author);
        author.setText(currentBook.getBookAuthor());

        MaterialCardView card = (MaterialCardView)listItemView.findViewById(R.id.card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri bookInfoUrl = Uri.parse(currentBook.getBookInfoUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookInfoUrl);
                context.startActivity(websiteIntent);
            }
        });

        /*TextView rating = (TextView)listItemView.findViewById(R.id.book_rating);
        DecimalFormat formatter = new DecimalFormat("0.0");
        String rat = formatter.format(currentBook.getBookRating());
        rating.setText(rat);*/

        return listItemView;
    }
}
