package com.djibril.popularmovies.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bah on 7/23/15.
 */
public class Trailer implements Parcelable {

    public String mName;
    public String mSize;
    public String mSource;
    public String mType;

    public Trailer(String name, String size, String source, String type) {
        mName = name;
        mSize = size;
        mSource = source;
        mType = type;
    }

    private Trailer(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);

        mName = data[0];
        mSize = data[1];
        mSource = data[2];
        mType = data[3];

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                mName,
                mSize,
                mSource,
                mType
        });
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {

        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };
}
