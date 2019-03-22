package edu.tempe.bookcase;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookFragmentInterface{
    private BookDetailsFragment bookDetailsFragment;
    private BookListFragment bookListFragment;
    private ViewPagerFragment viewPagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Book Case");

        bookListFragment = new BookListFragment();
        bookDetailsFragment = new BookDetailsFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_bookList, bookListFragment)
                .replace(R.id.container_bookDetails, bookDetailsFragment)
                .commit();
    }

    @Override
    public void fragmentClicked(int id) {
        Resources res = getResources();
        String[] bookTitles = res.getStringArray(R.array.book_titles);
        bookDetailsFragment.displayBook(bookTitles[id]);
    }
}
