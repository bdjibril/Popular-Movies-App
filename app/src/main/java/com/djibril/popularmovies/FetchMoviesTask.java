package com.djibril.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by bah on 7/19/15.
 */
public class FetchMoviesTask extends AsyncTask <String, Void, ArrayList<Movie>> {

    Context mContext;
    MoviesAdapter mMoviesAdapter;

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();


    FetchMoviesTask(Context context, MoviesAdapter adapter){
        mContext = context;
        mMoviesAdapter = adapter;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        String sortBy = "vote_average.desc";

        // If there's no sortby param
        if (params.length != 0) {
            sortBy = params[0];
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String moviesJsonStr = null;

        try {
            // Construct the URL for the API
            final String MOVIES_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sortBy)
                    .appendQueryParameter(API_KEY_PARAM, Utils.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Movies string: " + moviesJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movies data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the movie.
        return null;
    }

    private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String ROOT_MOVIES_LIST = "results";

        // Define json paths
        final String MOVIE_POSTER = "poster_path";
        final String ORIGINAL_TITLE = "original_title";
        final String PLOT_SYNOPSIS = "overview";
        final String USER_RATING = "vote_average";
        final String RELEASE_DATE = "release_date";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray movieArray = moviesJson.getJSONArray(ROOT_MOVIES_LIST);

        ArrayList<Movie> resultMovies = new ArrayList<Movie>(movieArray.length());
        for(int i = 0; i < movieArray.length(); i++) {
            String imageLink;
            String originalTitle;
            String releaseDate;
            String duration;
            String userRating;
            String plotSynopsis;

            // Get the JSON object representing the movie
            JSONObject movieObject = movieArray.getJSONObject(i);

            imageLink = movieObject.getString(MOVIE_POSTER);
            originalTitle = movieObject.getString(ORIGINAL_TITLE);
            releaseDate = movieObject.getString(RELEASE_DATE);
            userRating = movieObject.getString(USER_RATING);
            plotSynopsis = movieObject.getString(PLOT_SYNOPSIS);

            resultMovies.add(new Movie(imageLink + "\n" +
                            originalTitle + "\n" +
                            releaseDate + "\n" +
                            userRating + "\n" +
                            plotSynopsis))
            ;

        }

        for (Movie s : resultMovies) {
            Log.v(LOG_TAG, "Movie Poster: " + s);
        }
        return resultMovies;

    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {

        Log.v(LOG_TAG, "TASK POST EXECUTE");

        mMoviesAdapter.mData = result;
        mMoviesAdapter.notifyDataSetChanged();

    }
}
