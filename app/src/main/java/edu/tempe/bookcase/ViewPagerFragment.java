package edu.tempe.bookcase;

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
    String title, author, published, url;
    public ViewPagerFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager,container,false);
        final TextView bookTitle = view.findViewById(R.id.bookTitle);
        final TextView bookAuthor = view.findViewById(R.id.bookAuthor);
        final TextView bookPublished = view.findViewById(R.id.bookPublished);
        title = getArguments().getString("Title");
        author = getArguments().getString("Author");
        published = getArguments().getString("Published");
        url = getArguments().getString("URL");
        bookTitle.setText(title);
        bookAuthor.setText(author);
        bookPublished.setText(published);
        new DownloadImageTask((ImageView) view.findViewById(R.id.bookImage))
                .execute(url);
        return view;
    }

}
