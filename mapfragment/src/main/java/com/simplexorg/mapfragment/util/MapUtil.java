package com.simplexorg.mapfragment.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.location.Location;
import android.support.annotation.ColorInt;

import com.simplexorg.mapfragment.model.GeoPoint;

public class MapUtil {
    private static MapUtil mUtil;

    private MapUtil() {

    }

    /**
     * Get the distance between two locations on a map.
     * @param geoPoint first point.
     * @param otherGeoPoint second point.
     * @return the distance between the two points in meters.
     */
    public double distance(GeoPoint geoPoint, GeoPoint otherGeoPoint) {
        Location loc = new Location("geoPoint");
        Location otherLoc = new Location("otherGeoPoint");
        loc.setLatitude(geoPoint.latitude);
        loc.setLongitude(geoPoint.longitude);
        otherLoc.setLatitude(otherGeoPoint.latitude);
        otherLoc.setLongitude(otherGeoPoint.longitude);
        return loc.distanceTo(otherLoc);
    }

    public Bitmap tintImage(Bitmap bitmap, @ColorInt int color) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC_IN));
        Bitmap bitmapResult = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapResult);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmapResult;
    }

    public static MapUtil getInstance() {
        if (mUtil == null) {
            mUtil = new MapUtil();
        }
        return mUtil;
    }

    public static void setUtil(MapUtil util) {
        mUtil = util;
    }
}
