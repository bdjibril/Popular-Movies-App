package com.djibril.popularmovies.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.djibril.popularmovies.R;
import com.djibril.popularmovies.Utils;
import com.djibril.popularmovies.adapter.ReviewAdapter;
import com.djibril.popularmovies.adapter.TrailerAdapter;
import com.djibril.popularmovies.data.MovieContract;
import com.djibril.popularmovies.object.Movie;
import com.djibril.popularmovies.task.FetchReviewsTask;
import com.djibril.popularmovies.task.FetchTrailersTask;


/*
 */
public class DetailActivityFragment extends Fragment {

    TrailerAdapter mTrailersAdapter;
    ReviewAdapter mReviewsAdapter;

    ListView mTrailersListView;
    ListView mReviewssListView;

    DetailViewHolder mViewHolder;

    String moviePostId;
    String moviePostImageName;
    String movieTitle;
    String movieYear;
    String movieRating;
    String movieDescription;

    Movie mMovie;

    boolean mGotData = false;


    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();



        if (null != intent && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mGotData = true;
            String stringExtra = intent.getStringExtra(Intent.EXTRA_TEXT);

            mMovie = new Movie(stringExtra);

        }
        else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                mGotData = true;
                mMovie = new Movie(arguments.getString(Intent.EXTRA_TEXT));
            }
        }

        if(mGotData){
            String moviePostId = Utils.extractValueFromMovieInfo(Utils.MOVIE_ID_FIELD, mMovie);

            mTrailersAdapter = new TrailerAdapter(getActivity(), null);
            fetchTrailers(moviePostId);

            mReviewsAdapter = new ReviewAdapter(getActivity(), null);
            fetchReviews(moviePostId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(mGotData) {

            moviePostId = Utils.extractValueFromMovieInfo(Utils.MOVIE_ID_FIELD, mMovie);
            moviePostImageName = Utils.extractValueFromMovieInfo(Utils.MOVIE_POSTER_FIELD, mMovie);
            movieTitle = Utils.extractValueFromMovieInfo(Utils.MOVIE_TITLE_FIELD, mMovie);
            movieYear = Utils.extractValueFromMovieInfo(Utils.MOVIE_YEAR_FIELD, mMovie).length() >= 4 ?
                    Utils.extractValueFromMovieInfo(Utils.MOVIE_YEAR_FIELD, mMovie).substring(0, 4) :
                    "";
            movieRating = Utils.extractValueFromMovieInfo(Utils.MOVIE_RATING_FIELD, mMovie);
            movieDescription = Utils.extractValueFromMovieInfo(Utils.MOVIE_DESCRIPTTION_FIELD, mMovie);

            String src = Utils.buildPosterImageUrl(moviePostImageName);

            mViewHolder = new DetailViewHolder(rootView);
            Glide.with(getActivity()).load(src).into(mViewHolder.posterImageView);

            mViewHolder.titleTextView.setText(movieTitle);
            mViewHolder.yearTextView.setText(movieYear);
            mViewHolder.ratingTextView.setRating(Float.valueOf(movieRating) / 2);
            mViewHolder.descriptionTextView.setText(movieDescription);

            mViewHolder.favoriteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setAsFavorite(v);
                }
            });
            mViewHolder.removeFavoriteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    removeFromFavorites(v);
                }
            });

            // Handling showing and hiding button
            handleButtons();

            mTrailersListView = (ListView) rootView.findViewById(R.id.trailer_list);

            mTrailersListView.setAdapter(mTrailersAdapter);

            mTrailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String source = mTrailersAdapter.mData.get(position).mSource;
                    Utils.watchYoutubeVideo(source, getActivity());
                }
            });

            mTrailersAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    Utils.setListViewHeightBasedOnChildren(mTrailersListView);
                }
            });

            mReviewssListView = (ListView) rootView.findViewById(R.id.reviews_list);

            mReviewssListView.setAdapter(mReviewsAdapter);

            mReviewsAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    Utils.setListViewHeightBasedOnChildren(mReviewssListView);
                }
            });

        }

        return rootView;
    }

    private boolean fetchTrailers(String movieId) {
        if (getActivity() == null) return false;

        if (!Utils.isNetworkAvailable(getActivity())) {
            // show toast if no network
            String text = "No Internet Connectivity";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
            return false;
        }

        // Network is present
        FetchTrailersTask fetchTrailersTask = new FetchTrailersTask(getActivity(), mTrailersAdapter);
        fetchTrailersTask.execute(movieId);

        return true;
    }

    private boolean fetchReviews(String movieId) {
        if (getActivity() == null) return false;

        if (!Utils.isNetworkAvailable(getActivity())) {
            // show toast if no network
            String text = "No Internet Connectivity";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();
            return false;
        }

        // Network is present
        FetchReviewsTask fetchReviewsTask = new FetchReviewsTask(getActivity(), mReviewsAdapter);
        fetchReviewsTask.execute(movieId);

        return true;
    }


    /**
     * Cache of the views for a Movie  item.
     */
    public static class DetailViewHolder {
        public final ImageView posterImageView;
        public final TextView titleTextView;
        public final TextView yearTextView;
        public final RatingBar ratingTextView;
        public final TextView descriptionTextView;
        public final Button favoriteButton;
        public final Button removeFavoriteButton;

        public DetailViewHolder(View view) {
            posterImageView = (ImageView) view.findViewById(R.id.movies_detail_image);
            titleTextView = (TextView) view.findViewById(R.id.movies_detail_title);
            yearTextView = (TextView) view.findViewById(R.id.movies_detail_year);
            ratingTextView = (RatingBar) view.findViewById(R.id.movies_detail_rating);
            descriptionTextView = (TextView) view.findViewById(R.id.movies_detail_description);
            favoriteButton = (Button) view.findViewById(R.id.favorite_button);
            removeFavoriteButton = (Button) view.findViewById(R.id.remove_favorite_button);
        }
    }

    public void setAsFavorite(View view) {
        long movieId;

        //First, check if a movie with the id exists
        Cursor movieCursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{
                        MovieContract.MovieEntry._ID,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID
                },
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{moviePostId},
                null);

        if (movieCursor.moveToFirst()) {
            int movieIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry._ID);
            movieId = movieCursor.getLong(movieIdIndex);
        } else {
            // Create the movie content values to insert
            ContentValues movieValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            movieValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, movieDescription);
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, moviePostId);
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER, moviePostImageName);
            movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, movieRating);
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieTitle);
            movieValues.put(MovieContract.MovieEntry.COLUMN_YEAR, movieYear);

            // Finally, insert data into the database.
            Uri insertedUri = getActivity().getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    movieValues
            );

            // The resulting URI contains the ID for the row.  Extract the movieId from the Uri.
            movieId = ContentUris.parseId(insertedUri);
        }

        movieCursor.close();
        handleButtons();
    }

    public void removeFromFavorites(View view) {

        // First, check if a movie with the id exists
        Cursor movieCursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{
                        MovieContract.MovieEntry._ID,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID
                },
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{moviePostId},
                null);

        if (movieCursor.moveToFirst()) {
            // Delete the movies from favorites
            getActivity().getContentResolver().delete(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{moviePostId}
            );
        }

        movieCursor.close();

        handleButtons();
    }

    private boolean isFavorite() {

        boolean isFavorite = false;
        // First, check if a movie with the id exists
        Cursor movieCursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{
                        MovieContract.MovieEntry._ID,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID
                },
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{moviePostId},
                null);

        if (movieCursor.moveToFirst()) {
            isFavorite = true;
        }

        movieCursor.close();

        return isFavorite;
    }

    private void handleButtons() {
        if (isFavorite()) {
            mViewHolder.favoriteButton.setVisibility(View.GONE);
            mViewHolder.removeFavoriteButton.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.removeFavoriteButton.setVisibility(View.GONE);
            mViewHolder.favoriteButton.setVisibility(View.VISIBLE);
        }
    }


}
