package edu.tempe.bookcase;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.temple.audiobookplayer.AudiobookService;

public class BookListFragment extends Fragment {
    BookFragmentInterface bookListener;
    private ArrayList<Integer> booksToShow = new ArrayList<Integer>();
    private boolean searchValue = false;
    private static final String TAG = "Audiobook Service";

    public interface BookFragmentInterface {
        void fragmentClicked(int id);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booklist,container,false);

        final ListView listView = view.findViewById(R.id.bookListView);
        final EditText editSearch = view.findViewById(R.id.editSearch);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        System.out.println(MainActivity.Books);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                booksToShow.clear();
                searchValue = false;
                String search = editSearch.getText().toString();
                for (int i = 0; i < MainActivity.Books.size(); i++) {
                    if (search.equals(MainActivity.Books.get(i).getTitle()) || search.equals(MainActivity.Books.get(i).getAuthor()) || search.equals(Integer.toString(MainActivity.Books.get(i).getPublished()))) {
                        booksToShow.add(i);
                        System.out.println(booksToShow);
                        searchValue = true;
                    }
                }
                if (!searchValue) {
                    String[] bookTitles = new String[MainActivity.Books.size()];
                    for(int i = 0; i < MainActivity.Books.size(); i++){
                        bookTitles[i] = MainActivity.Books.get(i).getTitle();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(),
                            android.R.layout.simple_list_item_1, bookTitles);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            bookListener.fragmentClicked(position);
                            MainActivity.bookId = position + 1;
                            MainActivity.duration = MainActivity.Books.get(position).getDuration();
                        }
                    });
                } else {
                    String[] bookTitles = new String[booksToShow.size()];
                    for(int i = 0; i < booksToShow.size(); i++){
                        bookTitles[i] = MainActivity.Books.get(booksToShow.get(i)).getTitle();
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(),
                            android.R.layout.simple_list_item_1, bookTitles);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            bookListener.fragmentClicked(position);
                            MainActivity.bookId = position + 1;
                            MainActivity.duration = MainActivity.Books.get(position).getDuration();
                        }
                    });
                }
            }
        });
        String[] bookTitles = new String[MainActivity.Books.size()];
        for(int i = 0; i < MainActivity.Books.size(); i++){
            bookTitles[i] = MainActivity.Books.get(i).getTitle();
        }
        ListView listView2 = view.findViewById(R.id.bookListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, bookTitles);
        listView2.setAdapter(adapter);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bookListener.fragmentClicked(position);
                MainActivity.bookId = position + 1;
                MainActivity.duration = MainActivity.Books.get(position).getDuration();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof BookFragmentInterface) {
            bookListener = (BookFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ColorFragmentInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bookListener = null;
    }
}
