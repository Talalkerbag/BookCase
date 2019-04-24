package edu.tempe.bookcase;

import java.util.ArrayList;

public class BookPlayingInformation {
    private int bookPlaying;
    private int bookProgress;

    public BookPlayingInformation(int id){
        setBookPlaying(id);
    }

    public void setBookPlaying(int id){
        this.bookPlaying = id;
    }

    public int getBookPlaying(){
        return this.bookPlaying;
    }

    public void setBookProgress(int position){
        this.bookProgress = position;
    }

    public int getBookProgress(){
        return this.bookProgress;
    }


}
