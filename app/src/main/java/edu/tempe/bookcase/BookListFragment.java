package edu.tempe.bookcase;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.res.Resources;
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
        //Resources res = getResources();
        String[] bookTitles = new String[MainActivity.Books.size()];
        for(int i = 0; i < MainActivity.Books.size(); i++){
            bookTitles[i] = MainActivity.Books.get(i).getTitle();
        }
        ListView listView = v.findViewById(R.id.bookListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(),
                android.R.layout.simple_list_item_1, bookTitles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bookListener.fragmentClicked(position);
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
