package com.simplexorg.mapfragment.util;

import android.location.Location;

import com.simplexorg.mapfragment.model.GeoPoint;

public class Util {
    private static Util mUtil;

    private Util() {

    }

    public double distance(GeoPoint geoPoint, GeoPoint otherGeoPoint) {
        Location loc = new Location("geoPoint");
        Location otherLoc = new Location("otherGeoPoint");
        loc.setLatitude(geoPoint.latitude);
        loc.setLongitude(geoPoint.longitude);
        otherLoc.setLatitude(otherGeoPoint.latitude);
        otherLoc.setLongitude(otherGeoPoint.longitude);
        return loc.distanceTo(otherLoc);
    }

    public static Util getInstance() {
        if (mUtil == null) {
            mUtil = new Util();
        }
        return mUtil;
    }

    public static void setUtil(Util util) {
        mUtil = util;
    }
}
