package com.simplexorg.mapfragment.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class GeoPoint implements Parcelable {
    public final double latitude;
    public final double longitude;

    public static final Creator<GeoPoint> CREATOR = new Creator<GeoPoint>() {
        @Override
        public GeoPoint createFromParcel(Parcel parcel) {
            return new GeoPoint(parcel);
        }

        @Override
        public GeoPoint[] newArray(int i) {
            return new GeoPoint[i];
        }
    };

    private GeoPoint(Parcel parcel) {
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
    }

    public GeoPoint(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    @Override
    public String toString() {
        return String.format(Locale.CHINA, "latitude: %f, longitude: %f", latitude, longitude);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
