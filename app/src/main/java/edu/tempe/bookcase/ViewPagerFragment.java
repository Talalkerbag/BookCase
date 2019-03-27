package edu.tempe.bookcase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class ViewPagerFragment extends Fragment {
    private TextView textView;

    public ViewPagerFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager,container,false);
        textView = view.findViewById(R.id.txt_display);
        String message = getArguments().getString("message");
        textView.setText(message);
        return view;
    }
}
