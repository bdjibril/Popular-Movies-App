package com.djibril.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.Override;
import java.lang.String;

/**
 * Created by bah on 7/23/15.
 */
public class Movie implements Parcelable {

    String movieDataSrting;

    public Movie(String data) {
        movieDataSrting = data;
    }

    private Movie(Parcel in) {
        movieDataSrting = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieDataSrting);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
