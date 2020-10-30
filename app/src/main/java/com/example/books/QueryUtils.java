package com.example.books;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){}

    public static List<Book> fetchBookData(String stringUrl){
        URL url = makeUrl(stringUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPrequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Book> bookList = extractBooks(jsonResponse);
        return bookList;
    }

    private static URL makeUrl(String stringUrl){
        URL url = null;
        try {
            url =new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static  String makeHTTPrequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<Book> extractBooks(String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse)) return  null;
        ArrayList<Book> books = new ArrayList<>();
        try {
            JSONObject reader =new JSONObject(jsonResponse);
            JSONArray items = reader.getJSONArray("items");

            for(int i=0;i<items.length();i++){
                JSONObject bookObject  = items.getJSONObject(i);
                JSONObject volumeInfo = bookObject.getJSONObject("volumeInfo");

                //title
                String title = volumeInfo.getString("title").toString();

                //rating
                //double rating = 0;// volumeInfo.getDouble("averageRating");

                //authors
                JSONArray authors;
                String author="";

                try {
                    authors = volumeInfo.getJSONArray("authors");
                    for(int j=0;j<authors.length();j++){
                        String temp = authors.getString(j).toString();

                        if(j!=authors.length()-1)
                            temp = temp + ", " ;

                        author = author + temp;
                    }
                }catch(Exception e){
                    Log.e("JSON error","Authors not found");
                    author="Unknown";
                }


                //url
                String url = volumeInfo.getString("infoLink");

                books.add(new Book(title,author,url));
            }

        } catch (Exception e) {
            Log.e("QueryUtils", "Problem parsing the Books JSON results", e);
        }

        return books;
    }


}
