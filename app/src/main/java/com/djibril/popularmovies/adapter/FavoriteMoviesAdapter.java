package com.djibril.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.djibril.popularmovies.Utils;
import com.djibril.popularmovies.fragment.FavoriteMoviesFragment;


/*
 */
public class FavoriteMoviesAdapter extends CursorAdapter {

    public FavoriteMoviesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    private static final int VIEW_TYPE = 0;
    private static final int VIEW_TYPE_COUNT = 1;

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     *
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // if it's not recycled, create and set the attributes
        ImageView imageItemView = new ImageView(context);
        Utils.setPosterImageSizeParams(context, imageItemView);
        return imageItemView;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String imageName = cursor.getString(FavoriteMoviesFragment.COL_MOVIE_POSTER);

        Glide.with(context).load(Utils.buildPosterImageUrl(imageName)).into((ImageView)view);
    }
}