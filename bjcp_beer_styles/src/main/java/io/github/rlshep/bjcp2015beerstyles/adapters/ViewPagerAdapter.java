package io.github.rlshep.bjcp2015beerstyles.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.github.rlshep.bjcp2015beerstyles.CategoryListTab;
import io.github.rlshep.bjcp2015beerstyles.BookmarkedTab;
import io.github.rlshep.bjcp2015beerstyles.ColorTableTab;
import io.github.rlshep.bjcp2015beerstyles.FilterTab;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private final static int NUM_OF_TABS = 4; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[]) {
        super(fm);
        this.Titles = mTitles;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        if (position == 0) {
            fragment = new CategoryListTab();
        } else if (position == 1) {
            fragment = new BookmarkedTab();
        } else if (position == 2) {
            fragment = new ColorTableTab();
        } else {
            fragment = new FilterTab();
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NUM_OF_TABS;
    }
}
