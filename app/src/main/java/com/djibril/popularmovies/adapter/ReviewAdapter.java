package com.djibril.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.djibril.popularmovies.R;
import com.djibril.popularmovies.object.Review;

import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {

    public Context mContext;
    public ArrayList <Review> mData;

    public ReviewAdapter(Context c, ArrayList<Review> d){
        mContext = c;
        mData = d;
    }

    @Override
    public int getCount() {
        if(mData == null) return 0;
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Review review = (Review) mData.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_review, parent, false);
        }
        // Lookup view
        TextView tvAuthor = (TextView) convertView.findViewById(R.id.review_author);
        TextView tvContent = (TextView) convertView.findViewById(R.id.review_content);

        // Populate the data into the template view using the data object
        tvAuthor.setText(review.mAuthor);
        tvContent.setText(review.mContent);

        // Return the completed view to render on screen
        return convertView;
    }

}
