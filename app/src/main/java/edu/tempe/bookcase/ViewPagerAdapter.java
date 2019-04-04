package edu.tempe.bookcase;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private ArrayList<ViewPagerFragment> bookArray;
    public ViewPagerAdapter(FragmentManager fm, ArrayList<ViewPagerFragment> bookArray) {
        super(fm);
        this.bookArray = bookArray;
    }
    @Override
    public Fragment getItem(int i) {
        return bookArray.get(i);
    }

    @Override
    public int getCount() {
        return bookArray.size();
    }
}
