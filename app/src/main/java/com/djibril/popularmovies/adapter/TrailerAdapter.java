package com.djibril.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.djibril.popularmovies.R;
import com.djibril.popularmovies.object.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends BaseAdapter {

    public Context mContext;
    public ArrayList <Trailer> mData;

    public TrailerAdapter(Context c, ArrayList<Trailer> d){
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
        Trailer trailer = (Trailer) mData.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_trailer, parent, false);
        }
        // Lookup view
        TextView tvName = (TextView) convertView.findViewById(R.id.trailer_title);

        // Populate the data into the template view using the data object
        tvName.setText(trailer.mName);

        // Return the completed view to render on screen
        return convertView;
    }

}
