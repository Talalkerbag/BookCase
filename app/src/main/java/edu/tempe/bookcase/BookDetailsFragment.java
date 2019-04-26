package edu.tempe.bookcase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class BookDetailsFragment extends Fragment {

    View v;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_bookdetails, container, false);
        if(MainActivity.playing){
            displayBook(MainActivity.Books.get(MainActivity.bookId - 1));
        }else{
            displayBook(MainActivity.Books.get(0));
        }
        return v;
    }

    public void displayBook(Book bookObject){
        TextView bookTile = v.findViewById(R.id.bookTitle);
        TextView bookAuthor = v.findViewById(R.id.bookAuthor);
        TextView bookPublished = v.findViewById(R.id.bookPublished);
        bookTile.setText(bookObject.getTitle());
        bookAuthor.setText(bookObject.getAuthor());
        bookPublished.setText(Integer.toString(bookObject.getPublished()));
        new DownloadImageTask((ImageView) v.findViewById(R.id.bookImage))
                .execute(bookObject.getCoverURL());
    }


}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}




