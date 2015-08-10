package com.djibril.popularmovies.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.djibril.popularmovies.fragment.FavoriteMoviesFragment;
import com.djibril.popularmovies.fragment.MoviesFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence mTitles[]; // This will Store the mTitles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int mNumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence titles[], int numbOfTabs) {
        super(fm);

        this.mTitles = titles;
        this.mNumbOfTabs = numbOfTabs;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 : {
                MoviesFragment fragment1 = new MoviesFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MoviesFragment.SORT, MoviesFragment.POPULARITY_DESC);
                fragment1.setArguments(bundle);

                return fragment1;
            }
            case 1 : {
                MoviesFragment fragment2 = new MoviesFragment();
                Bundle bundle = new Bundle();
                bundle.putString(MoviesFragment.SORT, MoviesFragment.VOTE_AVERAGE_DESC);
                fragment2.setArguments(bundle);
                return fragment2;
            }
            default:
                return new FavoriteMoviesFragment();
        }

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return mNumbOfTabs;
    }
}