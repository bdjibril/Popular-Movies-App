package com.djibril.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private MoviesAdapter mMoviesAdapter;
    private GridView mGridView;
    private FetchMoviesTask mFetchMoviesTask;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(R.id.grid_view_movies);

        mMoviesAdapter = new MoviesAdapter(getActivity(),null);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = prefs.getString(this.getString(R.string.pref_sort_by_key),
                this.getString(R.string.pref_sort_by_default));


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String extra =  mMoviesAdapter.mData[position];
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, extra);

                startActivity(intent);
            }
        });

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity(), mMoviesAdapter);
        fetchMoviesTask.execute(sortBy);

        mGridView.setAdapter(mMoviesAdapter);

        return rootView;
    }

    public void onSortChanged(String sortBy) {

        Log.v("FRAG","SORT CHANGED");

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity(), mMoviesAdapter);
        fetchMoviesTask.execute(sortBy);
        mGridView.smoothScrollToPosition(0);

    }
}
