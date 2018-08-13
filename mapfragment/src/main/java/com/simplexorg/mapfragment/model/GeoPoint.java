package com.simplexorg.mapfragment.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class GeoPoint implements Parcelable {
    public final double latitude;
    public final double longitude;
    private static Location mLoc;
    private static Location mOtherLoc;

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

    public static double distance(GeoPoint geoPoint, GeoPoint otherGeoPoint) {
        if (mLoc == null || mOtherLoc == null) {
            mLoc = new Location("geoPoint");
            mOtherLoc = new Location("otherGeoPoint");
        }
        mLoc.setLatitude(geoPoint.latitude);
        mLoc.setLongitude(geoPoint.longitude);
        mOtherLoc.setLatitude(otherGeoPoint.latitude);
        mOtherLoc.setLongitude(otherGeoPoint.longitude);
        return mLoc.distanceTo(mOtherLoc);
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
