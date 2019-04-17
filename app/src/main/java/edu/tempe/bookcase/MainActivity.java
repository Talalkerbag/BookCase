package edu.tempe.bookcase;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookFragmentInterface, ServiceConnection {
    private BookDetailsFragment bookDetailsFragment;
    private BookListFragment bookListFragment;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private ArrayList<ViewPagerFragment> bookArray;
    private EditText editSearch;
    private Button btnSearch, btnPlay, btnStop;
    private SeekBar mSeekBar;
    private boolean searchValue = false;
    private AudiobookService.MediaControlBinder mediaControlBinder;
    private ArrayList<Integer> booksToShow = new ArrayList<Integer>();
    public static ArrayList<Book> Books = new ArrayList<Book>();
    public static String JsonData;
    public static boolean JsonReady = false;
    public boolean connected = false;
    private int bookId = 1;
    private boolean playing = false;
    private int duration = 0;
    private boolean startedNew = false;
    int amoungToupdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Book Case");
        bookArray = new ArrayList<ViewPagerFragment>();
        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        mSeekBar = findViewById(R.id.seekBar);
        viewPager = findViewById(R.id.bookPager);

        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                startedNew = true;
                if(playing){
                    mediaControlBinder.stop();
                    playing = false;
                }
                mSeekBar.setProgress(0);
                bookId = position + 1;
                duration = MainActivity.Books.get(position).getDuration();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        Timer mTimer = new Timer();
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        amoungToupdate = progressHandler.obtainMessage().what;
//                        if (!(amoungToupdate * mSeekBar.getProgress() >= duration)) {
//                            int p = mSeekBar.getProgress();
//                            p += 1;
//                            mSeekBar.setProgress(p);
//                        }
//                    }
//                });
//            };
//        }, amoungToupdate);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(!playing){
                    startedNew = false;
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                if(playing){
                    mediaControlBinder.seekTo(duration * progress / 100);
                }else if(!startedNew){
                    mediaControlBinder.play(bookId,duration*progress/600);
                    playing = true;
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mSeekBar.setProgress(0);
                mediaControlBinder.stop();
                playing = false;
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!playing){
                    mediaControlBinder.play(bookId);
                    playing = true;
                }else{
                    mediaControlBinder.pause();
                }
            }
        });

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            FetchData process = new FetchData();
            process.execute();
            while(!JsonReady){
                //Delay until process.execute() is finished.
            }
            new Thread(){
                public void run(){
                    Intent intent = new Intent(MainActivity.this, AudiobookService.class);
                    startService(intent);
                    bindService(intent, MainActivity.this, BIND_AUTO_CREATE);
                }
            }.start();

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
                            bundle.putInt("Published", MainActivity.Books.get(i).getPublished());
                            bundle.putString("URL", MainActivity.Books.get(i).getCoverURL());
                            bundle.putInt("Duration", MainActivity.Books.get(i).getDuration());
                            bundle.putInt("Id", i);
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
                            bundle.putInt("Published",MainActivity.Books.get(booksToShow.get(i)).getPublished());
                            bundle.putString("URL", MainActivity.Books.get(booksToShow.get(i)).getCoverURL());
                            bundle.putInt("Duration", MainActivity.Books.get(i).getDuration());
                            bundle.putInt("Id", i);
                            viewPagerFragment.setArguments(bundle);
                            bookArray.add(viewPagerFragment);
                        }
                    }
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
                    bundle.putInt("Published", MainActivity.Books.get(i).getPublished());
                    bundle.putString("URL", MainActivity.Books.get(i).getCoverURL());
                    bundle.putInt("Duration", MainActivity.Books.get(i).getDuration());
                    bundle.putInt("Id", i);
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
                    bundle.putInt("Published", MainActivity.Books.get(booksToShow.get(i)).getPublished());
                    bundle.putString("URL", MainActivity.Books.get(booksToShow.get(i)).getCoverURL());
                    bundle.putInt("Duration", MainActivity.Books.get(i).getDuration());
                    bundle.putInt("Id", i);
                    viewPagerFragment.setArguments(bundle);
                    bookArray.add(viewPagerFragment);
                }
            }
            viewPager = findViewById(R.id.bookPager);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), bookArray);
            viewPager.setAdapter(viewPagerAdapter);
            duration = Books.get(0).getDuration();

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
                        System.out.println("XXX");
                    }
                }, milliseconds);
            }
        });
    }

    private Handler progressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //msg.what will return an integer value.. number of seconds passed
            return false;
        }
    });

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mediaControlBinder = (AudiobookService.MediaControlBinder) service;
        mediaControlBinder.setProgressHandler(progressHandler);
        connected = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


}
