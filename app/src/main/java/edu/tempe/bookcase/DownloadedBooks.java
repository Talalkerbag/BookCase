package edu.tempe.bookcase;

import java.util.ArrayList;

public class DownloadedBooks {
    private ArrayList<Integer> ids = new ArrayList<Integer>();

    public DownloadedBooks(){

    }

    public void addToDownloadedBooks(int id) {
        this.ids.add(id);
    }

    public void removeFromDownloads(int id){
        this.ids.remove(this.ids.indexOf(id));
    }

    public boolean isDownloaded(int id){
        if(this.ids.contains(id)){
            return true;
        }else{
            return false;
        }
    }

}
