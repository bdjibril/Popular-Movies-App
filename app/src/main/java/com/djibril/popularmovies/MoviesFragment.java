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

    private static final String MOVIES_PARCELABLE_KEY = "movies";
    private MoviesAdapter mMoviesAdapter;
    private GridView mGridView;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMoviesAdapter = new MoviesAdapter(getActivity(),null);

        if(savedInstanceState == null || !savedInstanceState.containsKey(MOVIES_PARCELABLE_KEY)){

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = prefs.getString(this.getString(R.string.pref_sort_by_key),
                    this.getString(R.string.pref_sort_by_default));

            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity(), mMoviesAdapter);
            fetchMoviesTask.execute(sortBy);
        }
        else {
            mMoviesAdapter.mData = savedInstanceState.getParcelableArrayList(MOVIES_PARCELABLE_KEY);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_PARCELABLE_KEY, mMoviesAdapter.mData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(R.id.grid_view_movies);

        mGridView.setAdapter(mMoviesAdapter);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String extra = mMoviesAdapter.mData.get(position).movieDataSrting;
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, extra);

                startActivity(intent);
            }
        });


        return rootView;
    }

    public void onSortChanged(String sortBy) {

        Log.v("FRAG","SORT CHANGED");

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity(), mMoviesAdapter);
        fetchMoviesTask.execute(sortBy);
        mMoviesAdapter.notifyDataSetChanged();
        mGridView.smoothScrollToPosition(0);

    }
}
