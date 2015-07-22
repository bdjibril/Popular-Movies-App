package com.djibril.popularmovies;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.Arrays;

/**
 * Created by bah on 7/20/15.
 */
public class Utils {

    public static final String API_KEY = "YOUR_API_KEY";

    private static final String POSTER_NOT_FOUND_IMAGE = "https://d3a8mw37cqal2z.cloudfront.net/assets/f996aa2014d2ffddfda8463c479898a3/images/no-poster-w185.jpg";
    private static final String POSTER_BASE_PATH = "https://image.tmdb.org/t/p/w185/";

    // calculate the Aspect ratio of the image (From a sample image on the moviesDB api)
    private static final double MOVIE_IMAGE_ASPECT_RATIO = 278.00/184.00;

    public static final String MOVIE_POSTER_FIELD = "poster";
    public static final String MOVIE_TITLE_FIELD = "title";
    public static final String MOVIE_YEAR_FIELD = "year";
    public static final String MOVIE_RATING_FIELD = "rating";
    public static final String MOVIE_DESCRIPTTION_FIELD = "description";

    private static final String[] MOVIE_INFO_FIELDS = new String[]{
            MOVIE_POSTER_FIELD,
            MOVIE_TITLE_FIELD,
            MOVIE_YEAR_FIELD,
            MOVIE_RATING_FIELD,
            MOVIE_DESCRIPTTION_FIELD
    };

    public static String buildPosterImageUrl(final String posterImageName){
        String imageUrl = (
                posterImageName != null
                && !posterImageName.isEmpty()
                && !posterImageName.equals("null"))
                ? POSTER_BASE_PATH + posterImageName
                : POSTER_NOT_FOUND_IMAGE;
        return imageUrl;
    }

    public static void setPosterImageSizeParams(Context context, ImageView imageView){

        // Determine the right sizeForImage

        // Searched on Stack overflow about how to get with and height of the device in pixels
        // http://stackoverflow.com/questions/6465680/how-to-determine-the-screen-width-in-terms-of-dp-or-dip-at-runtime-in-android
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        // Get the number of rows from resources
        int numRows = context.getResources().getInteger(R.integer.grid_num_rows);

        // Use the display metrics and the number of columns to calculate the size of the images to use
        int movieThumbnailWidth = displayMetrics.widthPixels / numRows;
        int movieThumbnailHeight = (int) (movieThumbnailWidth * MOVIE_IMAGE_ASPECT_RATIO);

        GridView.LayoutParams layoutParams = new GridView.LayoutParams(movieThumbnailWidth, movieThumbnailHeight);

        // if it's not recycled, initialize some attributes
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        imageView.setLayoutParams(layoutParams);
    }

    public static String extractValueFromMovieInfo(final String infoToExtract, final String movieInfo){
        String[] movieInfoArray = movieInfo.split("\n");
        int position = Arrays.asList(MOVIE_INFO_FIELDS).indexOf(infoToExtract);
        return (movieInfoArray.length > position && !movieInfoArray[position].equals("null"))?movieInfoArray[position]:"";
    }
}
