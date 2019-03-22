package edu.tempe.bookcase;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

public class BookListFragment extends Fragment {
    BookFragmentInterface bookListener;

    public interface BookFragmentInterface {
        void fragmentClicked(int id);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_booklist,container,false);
        Resources res = getResources();
        String[] bookTitles = res.getStringArray(R.array.book_titles);
        ListView listView = (ListView) v.findViewById(R.id.bookListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),
                android.R.layout.simple_list_item_1, bookTitles);
        listView.setAdapter(adapter);

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bookListener.fragmentClicked(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        return v;
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
