package edu.tempe.bookcase;

import java.util.ArrayList;

public class BookPlayingInformation {
    private int bookPlaying;
    private int bookPlayingPosition;

    public BookPlayingInformation(){

    }

    public void setBookPlaying(int id){
        this.bookPlaying = id;
    }

    public int getBookPlaying(){
        return this.bookPlaying;
    }

    public void bookPlayingPosition(int position){
        this.bookPlayingPosition = position;
    }

    public int bookPlayingPosition(){
        return this.bookPlayingPosition;
    }


}
