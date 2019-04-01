package edu.tempe.bookcase;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements BookListFragment.BookFragmentInterface{
    private BookDetailsFragment bookDetailsFragment;
    private BookListFragment bookListFragment;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    public static ArrayList<Book> Books = new ArrayList<Book>();
    public static String JsonData;
    public static boolean JsonReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Book Case");
        FetchData process = new FetchData();
        process.execute();

        while(!JsonReady){
            //Delay
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            viewPager = findViewById(R.id.bookPager);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapter);
        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            bookListFragment = new BookListFragment();
            bookDetailsFragment = new BookDetailsFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_bookList, bookListFragment)
                    .replace(R.id.container_bookDetails, bookDetailsFragment)
                    .commit();
        }
    }


    @Override
    public void fragmentClicked(int id) {
        Resources res = getResources();
        //String[] bookTitles = res.getStringArray(R.array.book_titles);
        bookDetailsFragment.displayBook(Books.get(id));
    }

    public void delay(int seconds){
        final int milliseconds = seconds * 1000;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("XXX");                 //add your code here
                    }
                }, milliseconds);
            }
        });
    }


}
