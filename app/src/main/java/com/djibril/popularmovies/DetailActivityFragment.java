package com.djibril.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**

 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        if(null != intent && intent.hasExtra(Intent.EXTRA_TEXT)){
            String stringExtra = intent.getStringExtra(Intent.EXTRA_TEXT);

            Movie movie = new Movie(stringExtra);

            String moviePostImageName = Utils.extractValueFromMovieInfo(Utils.MOVIE_POSTER_FIELD, movie);
            String movieTitle = Utils.extractValueFromMovieInfo(Utils.MOVIE_TITLE_FIELD, movie);
            String movieYear = Utils.extractValueFromMovieInfo(Utils.MOVIE_YEAR_FIELD, movie).substring(0, 4);
            String movieRating = Utils.extractValueFromMovieInfo(Utils.MOVIE_RATING_FIELD, movie);
            String movieDescription = Utils.extractValueFromMovieInfo(Utils.MOVIE_DESCRIPTTION_FIELD, movie);

            String src = Utils.buildPosterImageUrl(moviePostImageName);

            DetailViewHolder viewHolder = new DetailViewHolder(rootView);
            Picasso.with(getActivity()).load(src).into(viewHolder.posterImageView);

            viewHolder.titleTextView.setText(movieTitle);
            viewHolder.yearTextView.setText(movieYear);
            viewHolder.ratingTextView.setRating(Float.valueOf(movieRating)/2);
            viewHolder.descriptionTextView.setText(movieDescription);
        }

        return rootView;
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

        public DetailViewHolder(View view) {
            posterImageView = (ImageView) view.findViewById(R.id.movies_detail_image);
            titleTextView = (TextView) view.findViewById(R.id.movies_detail_title);
            yearTextView = (TextView) view.findViewById(R.id.movies_detail_year);
            ratingTextView = (RatingBar) view.findViewById(R.id.movies_detail_rating);
            descriptionTextView = (TextView) view.findViewById(R.id.movies_detail_description);
        }
    }
}
