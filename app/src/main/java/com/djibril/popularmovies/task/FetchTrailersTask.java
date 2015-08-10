package com.djibril.popularmovies.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.djibril.popularmovies.Utils;
import com.djibril.popularmovies.adapter.TrailerAdapter;
import com.djibril.popularmovies.object.Trailer;

import org.apache.http.client.methods.HttpGet;
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
public class FetchTrailersTask extends AsyncTask <String, Void, ArrayList<Trailer>> {

    Context mContext;
    TrailerAdapter mTrailersAdapter;

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();


    public FetchTrailersTask(Context context, TrailerAdapter adapter){
        mContext = context;
        mTrailersAdapter = adapter;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {

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

            final String TRAILERS_PATH = "trailers";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(TRAILERS_PATH)
                    .appendQueryParameter(API_KEY_PARAM, Utils.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HttpGet.METHOD_NAME);
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

            Log.v(LOG_TAG, "Trailers string: " + trailersJsonStr);
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
            return getTrailersDataFromJson(trailersJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the trailers Data.
        return null;
    }

    private ArrayList<Trailer> getTrailersDataFromJson(String moviesJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String ROOT_TRAILERS_LIST = "youtube";

        // Define json paths
        final String TRAILER_NAME = "name";
        final String TRAILER_SIZE = "size";
        final String TRAILER_SOURCE = "source";
        final String TRAILER_TYPE = "type";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray movieArray = moviesJson.getJSONArray(ROOT_TRAILERS_LIST);

        ArrayList<Trailer> resulTrailers = new ArrayList<Trailer>(movieArray.length());
        for(int i = 0; i < movieArray.length(); i++) {
            String name;
            String size;
            String source;
            String type;

            // Get the JSON object representing the movie
            JSONObject trailerObject = movieArray.getJSONObject(i);

            name = trailerObject.getString(TRAILER_NAME);
            size = trailerObject.getString(TRAILER_SIZE);
            source = trailerObject.getString(TRAILER_SOURCE);
            type = trailerObject.getString(TRAILER_TYPE);

            resulTrailers.add(new Trailer(name,size,source,type));
        }

        for (Trailer trailer : resulTrailers) {
            Log.v(LOG_TAG, "Trailer source: " + trailer.mSource);
        }
        return resulTrailers;

    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> result) {

        Log.v(LOG_TAG, "TASK POST EXECUTE");

        mTrailersAdapter.mData = result;
        mTrailersAdapter.notifyDataSetChanged();

    }
}
