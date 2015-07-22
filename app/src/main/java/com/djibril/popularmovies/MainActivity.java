package com.djibril.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;


public class MainActivity extends ActionBarActivity {

    ArrayAdapter <String> mMoviesAdapter;
    private String mSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSortBy = prefs.getString(this.getString(R.string.pref_sort_by_key),
                this.getString(R.string.pref_sort_by_default));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent setttingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(setttingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = prefs.getString(this.getString(R.string.pref_sort_by_key),
                this.getString(R.string.pref_sort_by_default));

        // update the location in our second pane using the fragment manager
        if (sortBy != null && !sortBy.equals(mSortBy)) {
            MoviesFragment mf = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.movies_fragment);
            if (null != mf) {
                mf.onSortChanged(sortBy);
            }
            mSortBy = sortBy;
        }
    }
}
