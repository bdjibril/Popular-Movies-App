package com.djibril.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bah on 7/31/15.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.djibril.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.djibril.popularmovies/movies is a valid path for
    // looking at weather data. content://com.djibril.popularmovies/givemeroot will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIE = "movie";

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movieid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_DESCRIPTION = "description";


        public static  final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
}
