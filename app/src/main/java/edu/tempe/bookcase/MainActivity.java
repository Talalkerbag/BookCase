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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private ArrayList<ViewPagerFragment> bookArray;
    private EditText editSearch;
    private Button btnSearch;
    private boolean searchValue = false;
    private ArrayList<Integer> booksToShow = new ArrayList<Integer>();
    public static ArrayList<Book> Books = new ArrayList<Book>();
    public static String JsonData;
    public static boolean JsonReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Book Case");

        bookArray = new ArrayList<ViewPagerFragment>();
        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);



        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            FetchData process = new FetchData();
            process.execute();
            while(!JsonReady){
                //Delay until process.execute() is finished.
            }
            btnSearch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    booksToShow.clear();
                    searchValue = false;
                    String search = editSearch.getText().toString();
                    System.out.println(Books);
                    for (int i = 0; i < Books.size(); i++) {
                        if (search.equals(Books.get(i).getTitle()) || search.equals(Books.get(i).getAuthor()) || search.equals(Integer.toString(Books.get(i).getPublished()))) {
                            booksToShow.add(i);
                            System.out.println(booksToShow);
                            searchValue = true;
                        }
                    }
                    if (!searchValue) {
                        bookArray.clear();
                        for (int i = 0; i < Books.size(); i++) {
                            Bundle bundle = new Bundle();
                            ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                            bundle.putString("Title", MainActivity.Books.get(i).getTitle());
                            bundle.putString("Author", MainActivity.Books.get(i).getAuthor());
                            bundle.putString("Published", Integer.toString(MainActivity.Books.get(i).getPublished()));
                            bundle.putString("URL", MainActivity.Books.get(i).getCoverURL());
                            viewPagerFragment.setArguments(bundle);
                            bookArray.add(viewPagerFragment);
                        }
                    } else {
                        bookArray.clear();
                        for (int i = 0; i < booksToShow.size(); i++) {
                            Bundle bundle = new Bundle();
                            ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                            bundle.putString("Title", MainActivity.Books.get(booksToShow.get(i)).getTitle());
                            bundle.putString("Author", MainActivity.Books.get(booksToShow.get(i)).getAuthor());
                            bundle.putString("Published", Integer.toString(MainActivity.Books.get(booksToShow.get(i)).getPublished()));
                            bundle.putString("URL", MainActivity.Books.get(booksToShow.get(i)).getCoverURL());
                            viewPagerFragment.setArguments(bundle);
                            bookArray.add(viewPagerFragment);
                        }
                    }
                    viewPager = findViewById(R.id.bookPager);
                    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), bookArray);
                    viewPager.setAdapter(viewPagerAdapter);
                }
            });
            if(!searchValue){
                bookArray.clear();
                for(int i = 0; i < Books.size(); i++){
                    Bundle bundle = new Bundle();
                    ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                    bundle.putString("Title", MainActivity.Books.get(i).getTitle());
                    bundle.putString("Author", MainActivity.Books.get(i).getAuthor());
                    bundle.putString("Published", Integer.toString(MainActivity.Books.get(i).getPublished()));
                    bundle.putString("URL", MainActivity.Books.get(i).getCoverURL());
                    viewPagerFragment.setArguments(bundle);
                    bookArray.add(viewPagerFragment);
                }
            }else {
                bookArray.clear();
                for (int i = 0; i < booksToShow.size(); i++) {
                    Bundle bundle = new Bundle();
                    ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                    bundle.putString("Title", MainActivity.Books.get(booksToShow.get(i)).getTitle());
                    bundle.putString("Author", MainActivity.Books.get(booksToShow.get(i)).getAuthor());
                    bundle.putString("Published", Integer.toString(MainActivity.Books.get(booksToShow.get(i)).getPublished()));
                    bundle.putString("URL", MainActivity.Books.get(booksToShow.get(i)).getCoverURL());
                    viewPagerFragment.setArguments(bundle);
                    bookArray.add(viewPagerFragment);
                }
            }
            viewPager = findViewById(R.id.bookPager);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), bookArray);
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
