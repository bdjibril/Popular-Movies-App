package com.djibril.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.djibril.popularmovies.R;
import com.djibril.popularmovies.adapter.ViewPagerAdapter;
import com.djibril.popularmovies.fragment.DetailActivityFragment;
import com.djibril.popularmovies.fragment.MoviesFragment;
import com.djibril.popularmovies.widget.SlidingTabLayout;


public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {
    private static final CharSequence MOST_POPULAR = "Popular";
    private static final CharSequence HIGHEST_RATED = "Highest Rated";
    private static final CharSequence FAVORITES = "Favorites";
    private static final int SECOND_PAGER_TAB_INDEX = 1;
    private static final CharSequence Titles[] = {MOST_POPULAR, HIGHEST_RATED, FAVORITES};
    private static final int NUMBOFTABS = 3;

    // Declaring Your View and Variables

    Toolbar mToolbar;
    ViewPager mPager;
    ViewPagerAdapter mPagerAdapter;
    SlidingTabLayout mTabs;

    private boolean mTwoPane;

    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    //private String mSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSortBy = prefs.getString(this.getString(R.string.pref_sort_by_key),
                this.getString(R.string.pref_sort_by_default));*/


        // Creating The Toolbar and setting it as the Toolbar for the activity

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        // Example taken from the SUNSHINE example app

        // Hide the title
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            /*if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }*/
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, NUMBOFTABS);

        // Assigning ViewPager View and setting the mPagerAdapter
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);


        // When the tab changes

        // Unused
        /*mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadMoviesInCurrentTab();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        // Assiging the Sliding Tab Layout View
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the mTabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.icons);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        mTabs.setViewPager(mPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMoviesInCurrentTab();
    }

    private void loadMoviesInCurrentTab(){

        if(!(mPagerAdapter.getItem(mPager.getCurrentItem()) instanceof MoviesFragment) ) return;

        String sortBy = getString(R.string.most_popular_value);

        if(mPager.getCurrentItem() == SECOND_PAGER_TAB_INDEX ) sortBy = getString(R.string.highest_rated_value);

        MoviesFragment currentMovieFragment = (MoviesFragment) mPagerAdapter.getItem(mPager.getCurrentItem());
        if (null != currentMovieFragment) currentMovieFragment.onSortChanged(sortBy);
    }

    @Override
    public void onItemSelected(String movieData) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(Intent.EXTRA_TEXT, movieData);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, movieData);
            startActivity(intent);
        }
    }
}
