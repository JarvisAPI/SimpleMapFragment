package com.simplexorg.mapfragment.model;

import android.location.Location;

import java.util.Locale;

public class GeoPoint {
    public final double latitude;
    public final double longitude;
    private static Location mLoc;
    private static Location mOtherLoc;

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
}
