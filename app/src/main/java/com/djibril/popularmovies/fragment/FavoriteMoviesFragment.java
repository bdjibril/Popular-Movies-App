package com.djibril.popularmovies.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.djibril.popularmovies.R;
import com.djibril.popularmovies.adapter.FavoriteMoviesAdapter;
import com.djibril.popularmovies.data.MovieContract;
import com.djibril.popularmovies.task.FetchMoviesTask;


/**
 *
 */
public class FavoriteMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String MOVIES_PARCELABLE_KEY = "movies";
    public static final String DESC = "DESC";
    private FavoriteMoviesAdapter mFavoriteMoviesAdapter;
    private GridView mGridView;

    private static final int MOVIES_LOADER = 0;

    private static final String[] FAVORITE_MOVIES_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_DESCRIPTION,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_YEAR
    };

    // These indices are tied to MOVIES_COLS.  If MOVIES_COLS changes, these
    // must change.
    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_MOVIE_TITLE = 2;
    public static final int COL_MOVIE_DESCRIPTION = 3;
    public static final int COL_MOVIE_RATING = 4;
    public static final int COL_MOVIE_POSTER = 5;
    public static final int COL_MOVIE_YEAR = 6;

    public FavoriteMoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle Menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The CursorAdapter will take data from our cursor and populate the ListView.
        mFavoriteMoviesAdapter = new FavoriteMoviesAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(R.id.grid_view_movies);

        mGridView.setAdapter(mFavoriteMoviesAdapter);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mFavoriteMoviesAdapter.getCursor();

                String movieId = cursor.getString(COL_MOVIE_ID);
                String moviePoster = cursor.getString(COL_MOVIE_POSTER);
                String movieTitle = cursor.getString(COL_MOVIE_TITLE);
                Integer movieYear = cursor.getInt(COL_MOVIE_YEAR);
                Float movieRating = cursor.getFloat(COL_MOVIE_RATING);
                String movieDescription= cursor.getString(COL_MOVIE_DESCRIPTION);

                String extra = movieId + FetchMoviesTask.SEPERATOR +
                        moviePoster + FetchMoviesTask.SEPERATOR +
                        movieTitle + FetchMoviesTask.SEPERATOR +
                        movieYear + FetchMoviesTask.SEPERATOR +
                        movieRating + FetchMoviesTask.SEPERATOR +
                        movieDescription;

                ((MoviesFragment.Callback) getActivity())
                        .onItemSelected(extra);
            }
        });


        return rootView;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Sort order:  Ascending, by date.
        String sortOrder = MovieContract.MovieEntry._ID + " " + DESC;

        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                FAVORITE_MOVIES_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mFavoriteMoviesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
