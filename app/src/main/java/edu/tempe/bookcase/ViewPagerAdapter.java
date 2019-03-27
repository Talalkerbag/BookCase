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
        String[] bookTitles = {"The Great Gatsby","To Kill a Mockingbird", "Invisible Man", "Thinks Fall Apart", "Heart of Darkness" ,
        "The Sun Also Rises", "To the Lighthouse", "The Catcher in the Rye", "Hamlet","In Search of Lost Time"};
        bundle.putString("message", bookTitles[i]);
        viewPagerFragment.setArguments(bundle);

        return viewPagerFragment;
    }

    @Override
    public int getCount() {
        return 10;
    }
}
