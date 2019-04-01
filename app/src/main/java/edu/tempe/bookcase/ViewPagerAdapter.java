package edu.tempe.bookcase;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Title", MainActivity.Books.get(i).getTitle());
        bundle.putString("Author", MainActivity.Books.get(i).getAuthor());
        bundle.putString("Published", Integer.toString(MainActivity.Books.get(i).getPublished()));
        bundle.putString("URL", MainActivity.Books.get(i).getCoverURL());

        viewPagerFragment.setArguments(bundle);

        return viewPagerFragment;
    }

    @Override
    public int getCount() {
        return 7;
    }
}
