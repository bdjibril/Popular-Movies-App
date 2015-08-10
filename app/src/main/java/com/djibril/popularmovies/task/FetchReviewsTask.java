package com.djibril.popularmovies.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.djibril.popularmovies.Utils;
import com.djibril.popularmovies.adapter.ReviewAdapter;
import com.djibril.popularmovies.object.Review;

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
public class FetchReviewsTask extends AsyncTask <String, Void, ArrayList<Review>> {

    Context mContext;
    ReviewAdapter mReviewsAdapter;

    private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();


    public FetchReviewsTask(Context context, ReviewAdapter adapter){
        mContext = context;
        mReviewsAdapter = adapter;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {

        String movieId;

        // If there's no sortby param
        if (params.length == 0) {
            return null;
        }

        movieId = params[0];

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String trailersJsonStr = null;

        try {
            // Construct the URL for the API
            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";

            final String API_KEY_PARAM = "api_key";

            final String REVIEWS_PATH = "reviews";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(REVIEWS_PATH)
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
            trailersJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Reviews string: " + trailersJsonStr);
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
            return getReviewsDataFromJson(trailersJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the trailers Data.
        return null;
    }

    private ArrayList<Review> getReviewsDataFromJson(String reviewsJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String ROOT_REVIEWS_LIST = "results";

        // Define json paths
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";

        JSONObject reviewJson = new JSONObject(reviewsJsonStr);
        JSONArray reviewArray = reviewJson.getJSONArray(ROOT_REVIEWS_LIST);

        ArrayList<Review> reviews = new ArrayList<Review>(reviewArray.length());
        for(int i = 0; i < reviewArray.length(); i++) {
            String author;
            String content;

            // Get the JSON object representing the movie
            JSONObject trailerObject = reviewArray.getJSONObject(i);

            author = trailerObject.getString(REVIEW_AUTHOR);
            content = trailerObject.getString(REVIEW_CONTENT);

            reviews.add(new Review(author, content));
        }

        for (Review review : reviews) {
            Log.v(LOG_TAG, "Review author: " + review.mAuthor);
            Log.v(LOG_TAG, "Review content: " + review.mContent);
        }
        return reviews;

    }

    @Override
    protected void onPostExecute(ArrayList<Review> result) {

        Log.v(LOG_TAG, "TASK POST EXECUTE");

        mReviewsAdapter.mData = result;
        mReviewsAdapter.notifyDataSetChanged();

    }
}
