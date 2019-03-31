package edu.tempe.bookcase;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
//setId(id);
//setCoverURL(coverURL);
//setPublished(published);
//setTitle(title);
//setAuthor(author);


public class FetchData extends AsyncTask<Void,Void,Void> {
    String data = "";

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://kamorris.com/lab/audlib/booksearch.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray JA = new JSONArray(data);
            for(int i = 0; i < JA.length(); i++){
                JSONObject JO = (JSONObject) JA.get(i);
                MainActivity.Books.add(new Book(Integer.parseInt(JO.getString("book_id")),JO.getString("cover_url"),
                        Integer.parseInt(JO.getString("published")),JO.getString("title"),
                        JO.getString("author")));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainActivity.JsonData = this.data;
    }
}
