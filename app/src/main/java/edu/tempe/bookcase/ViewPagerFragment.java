package edu.tempe.bookcase;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



public class ViewPagerFragment extends Fragment {
    String title, author, url;
    int id, published, duration;
    public ViewPagerFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager,container,false);
        final TextView bookTitle = view.findViewById(R.id.bookTitle);
        final TextView bookAuthor = view.findViewById(R.id.bookAuthor);
        final TextView bookPublished = view.findViewById(R.id.bookPublished);
        title = getArguments().getString("Title");
        author = getArguments().getString("Author");
        published = getArguments().getInt("Published");
        url = getArguments().getString("URL");
        id = getArguments().getInt("Id");
        duration = getArguments().getInt("Duration");
        bookTitle.setText(title);
        bookAuthor.setText(author);
        bookPublished.setText(Integer.toString(published));
        new DownloadImageTask((ImageView) view.findViewById(R.id.bookImage))
                .execute(url);
        return view;
    }
}
