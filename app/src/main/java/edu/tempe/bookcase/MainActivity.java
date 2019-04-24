package edu.tempe.bookcase;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookFragmentInterface, ServiceConnection {
    long queueid;
    DownloadManager dm;
    private BookDetailsFragment bookDetailsFragment;
    private BookListFragment bookListFragment;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private ArrayList<ViewPagerFragment> bookArray;
    private EditText editSearch;
    public static Button btnSearch, btnPlay, btnStop, btnDownload;
    public static SeekBar mSeekBar;
    public static boolean searchValue = false;
    public static AudiobookService.MediaControlBinder mediaControlBinder;
    public static ArrayList<Integer> booksToShow = new ArrayList<Integer>();
    public static ArrayList<Book> Books = new ArrayList<Book>();
    public static ArrayList<Integer> booksPlaying = new ArrayList<Integer>();
    public static ArrayList<Integer> booksPlayingProgress = new ArrayList<Integer>();
    public static DownloadedBooks downloadedBooks = new DownloadedBooks();
    public static String JsonData;
    public static boolean JsonReady = false;
    public boolean connected = false;
    public static int bookId = 1;
    public static boolean playing = false;
    public static int duration = 0;
    public static boolean startedNew = false;
    private static final String TAG = "Audiobook Service";
    public static boolean searchedBooks = false;
    public static boolean pp = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Book Case");
        bookArray = new ArrayList<ViewPagerFragment>();
        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);

        Intent intent = new Intent(MainActivity.this, AudiobookService.class);
        startService(intent);
        bindService(intent, MainActivity.this, BIND_AUTO_CREATE);
        FetchData process = new FetchData();
        process.execute();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            while(!JsonReady){
                //Delay until process.execute() is finished.
            }
            btnPlay = findViewById(R.id.btnPlay);
            btnStop = findViewById(R.id.btnStop);
            btnDownload = findViewById(R.id.btnDownload);
            mSeekBar = findViewById(R.id.seekBar);
            viewPager = findViewById(R.id.bookPager);

            if(!playing){
                playing = false;
                duration = Books.get(0).getDuration();
                startedNew = false;
                bookId = 1;
            }else{
                bookArray.clear();
                Bundle bundle = new Bundle();
                ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                bundle.putString("Title", MainActivity.Books.get(bookId - 1).getTitle());
                bundle.putString("Author", MainActivity.Books.get(bookId - 1).getAuthor());
                bundle.putInt("Published",MainActivity.Books.get(bookId - 1).getPublished());
                bundle.putString("URL", MainActivity.Books.get(bookId - 1).getCoverURL());
                bundle.putInt("Duration", MainActivity.Books.get(bookId - 1).getDuration());
                bundle.putInt("Id", bookId - 1);
                viewPagerFragment.setArguments(bundle);
                bookArray.add(viewPagerFragment);
            }


            viewPager.addOnPageChangeListener(
                    new ViewPager.OnPageChangeListener() {

                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }

                        @Override
                        public void onPageSelected(int position) {
                            startedNew = true;
                            if(playing){
                                booksPlayingProgress.set(booksPlaying.indexOf(bookId),mSeekBar.getProgress());
                                mediaControlBinder.stop();
                                playing = false;
                                btnPlay.setBackgroundResource(R.drawable.play_icon);
                            }
                            mSeekBar.setProgress(0);
                            bookId = position + 1;
                            duration = MainActivity.Books.get(position).getDuration();
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });


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
                        Log.i(TAG, "duration: "+ duration + " -- Progress: " + progress + " -- position: " + duration*progress/100);
                    }else if(!startedNew){
                        mediaControlBinder.play(bookId,duration*progress/100);
                        Log.i(TAG, "duration: "+ duration + " -- Progress: " + progress + " -- position: " + duration*progress/100);
                    }
                }
            });

            btnStop.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(booksPlaying.size() > 0 && booksPlayingProgress.size() > 0){
                        System.out.println("books playing position: " + booksPlaying.indexOf(bookId));
                        int index = booksPlaying.indexOf(bookId);
                        booksPlaying.remove(index);
                        booksPlayingProgress.remove(index);
                    }
                    mSeekBar.setProgress(0);
                    mediaControlBinder.stop();
                    playing = false;
                    btnPlay.setBackgroundResource(R.drawable.play_icon);
                }
            });

            btnPlay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(!playing){
                        if(booksPlaying.contains(bookId)){
                            System.out.println("book in booksPlaying");
                            int start = (duration*booksPlayingProgress.get(booksPlaying.indexOf(bookId))/100)-10;
                            System.out.println("starting: " + start);
                            if(start < 0){
                                start = 0;
                            }
                            mediaControlBinder.play(bookId,start);
                            playing = true;
                            btnPlay.setBackgroundResource(R.drawable.pause_icon);
                        }else{
                            System.out.println("Added book to booksPlaying");
                            booksPlaying.add(bookId);
                            booksPlayingProgress.add(mSeekBar.getProgress());
                            mediaControlBinder.play(bookId);
                            playing = true;
                            btnPlay.setBackgroundResource(R.drawable.pause_icon);
                        }
                    }else{
                        mediaControlBinder.pause();
                        if(pp){
                            btnPlay.setBackgroundResource(R.drawable.pause_icon);
                            pp = false;
                        }else{
                            btnPlay.setBackgroundResource(R.drawable.play_icon);
                            pp = true;
                        }
                    }

                }
            });

            btnDownload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            });
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
                            bundle.putString("Title", Books.get(i).getTitle());
                            bundle.putString("Author", Books.get(i).getAuthor());
                            bundle.putInt("Published", Books.get(i).getPublished());
                            bundle.putString("URL", Books.get(i).getCoverURL());
                            bundle.putInt("Duration", Books.get(i).getDuration());
                            bundle.putInt("Id", i);
                            viewPagerFragment.setArguments(bundle);
                            bookArray.add(viewPagerFragment);
                        }
                        bookId = 1;
                        duration = Books.get(bookId - 1).getDuration();
                        playing = false;
                        btnPlay.setBackgroundResource(R.drawable.play_icon);
                        mediaControlBinder.stop();
                    } else {
                        bookArray.clear();
                        for (int i = 0; i < booksToShow.size(); i++) {
                            Bundle bundle = new Bundle();
                            ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                            bundle.putString("Title", Books.get(booksToShow.get(i)).getTitle());
                            bundle.putString("Author", Books.get(booksToShow.get(i)).getAuthor());
                            bundle.putInt("Published",Books.get(booksToShow.get(i)).getPublished());
                            bundle.putString("URL", Books.get(booksToShow.get(i)).getCoverURL());
                            bundle.putInt("Duration", Books.get(i).getDuration());
                            bundle.putInt("Id", i);
                            bookId = i;
                            duration = Books.get(i).getDuration();
                            viewPagerFragment.setArguments(bundle);
                            bookArray.add(viewPagerFragment);
                        }
                    }
                    playing = false;
                    btnPlay.setBackgroundResource(R.drawable.play_icon);
                    mediaControlBinder.stop();
                    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), bookArray);
                    viewPager.setAdapter(viewPagerAdapter);
                }
            });
            if(!searchValue && !playing){
                bookArray.clear();
                for(int i = 0; i < Books.size(); i++){
                    Bundle bundle = new Bundle();
                    ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                    bundle.putString("Title", Books.get(i).getTitle());
                    bundle.putString("Author", Books.get(i).getAuthor());
                    bundle.putInt("Published", Books.get(i).getPublished());
                    bundle.putString("URL", Books.get(i).getCoverURL());
                    bundle.putInt("Duration", Books.get(i).getDuration());
                    bundle.putInt("Id", i);
                    viewPagerFragment.setArguments(bundle);
                    bookArray.add(viewPagerFragment);
                }
            }else if(!playing) {
                bookArray.clear();
                for (int i = 0; i < booksToShow.size(); i++) {
                    Bundle bundle = new Bundle();
                    ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                    bundle.putString("Title", Books.get(booksToShow.get(i)).getTitle());
                    bundle.putString("Author", Books.get(booksToShow.get(i)).getAuthor());
                    bundle.putInt("Published", Books.get(booksToShow.get(i)).getPublished());
                    bundle.putString("URL", Books.get(booksToShow.get(i)).getCoverURL());
                    bundle.putInt("Duration", Books.get(i).getDuration());
                    bundle.putInt("Id", i);
                    viewPagerFragment.setArguments(bundle);
                    bookArray.add(viewPagerFragment);
                }
            }
            viewPager = findViewById(R.id.bookPager);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), bookArray);
            viewPager.setAdapter(viewPagerAdapter);

        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            btnPlay = findViewById(R.id.btnPlay);
            btnStop = findViewById(R.id.btnStop);
            mSeekBar = findViewById(R.id.seekBar);
            btnDownload = findViewById(R.id.btnDownload);

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
                        Log.i(TAG, "duration: "+ duration + " -- Progress: " + progress + " -- position: " + duration*progress/100);
                    }else if(!startedNew){
                        mediaControlBinder.play(bookId,duration*progress/100);
                        Log.i(TAG, "duration: "+ duration + " -- Progress: " + progress + " -- position: " + duration*progress/100);
                    }
                }
            });

            btnStop.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(booksPlaying.size() > 0 && booksPlayingProgress.size() > 0){
                        System.out.println("books playing position: " + booksPlaying.indexOf(bookId));
                        int index = booksPlaying.indexOf(bookId);
                        booksPlaying.remove(index);
                        booksPlayingProgress.remove(index);
                    }
                    mSeekBar.setProgress(0);
                    mediaControlBinder.stop();
                    playing = false;
                    btnPlay.setBackgroundResource(R.drawable.play_icon);
                }
            });

            btnPlay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(!playing){
                        if(booksPlaying.contains(bookId)){
                            System.out.println("book in booksPlaying");
                            int start = (duration*booksPlayingProgress.get(booksPlaying.indexOf(bookId))/100)-10;
                            System.out.println("starting: " + start);
                            if(start < 0){
                                start = 0;
                            }
                            mediaControlBinder.play(bookId,start);
                            playing = true;
                            btnPlay.setBackgroundResource(R.drawable.pause_icon);
                        }else{
                            System.out.println("Added book to booksPlaying");
                            booksPlaying.add(bookId);
                            booksPlayingProgress.add(mSeekBar.getProgress());
                            mediaControlBinder.play(bookId);
                            playing = true;
                            btnPlay.setBackgroundResource(R.drawable.pause_icon);
                        }
                    }else{
                        mediaControlBinder.pause();
                        if(pp){
                            btnPlay.setBackgroundResource(R.drawable.pause_icon);
                            pp = false;
                        }else{
                            btnPlay.setBackgroundResource(R.drawable.play_icon);
                            pp = true;
                        }
                    }

                }
            });

            btnDownload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            });
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
