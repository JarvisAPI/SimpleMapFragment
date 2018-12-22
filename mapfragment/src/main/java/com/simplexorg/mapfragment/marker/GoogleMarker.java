package com.simplexorg.mapfragment.marker;

import com.google.android.gms.maps.model.Marker;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.util.MapFactory;

public class GoogleMarker extends BaseMarker {
    private Marker mMarker;
    private GeoPoint mPos;
    private String mTitle;
    private String mSnippet;

    public GoogleMarker(Marker marker) {
        mMarker = marker;
        mPos = MapFactory.getInstance().createGeoPoint(mMarker.getPosition());
    }

    @Override
    public GeoPoint getPosition() {
        mPos = MapFactory.getInstance().createGeoPoint(mMarker.getPosition());
        return mPos;
    }

    @Override
    public void setAlpha(float alpha) {
        mMarker.setAlpha(alpha);
    }

    @Override
    public float getAlpha() {
        return mMarker.getAlpha();
    }

    @Override
    public void remove() {
        mMarker.remove();
    }

    @Override
    public void setPosition(GeoPoint geoPoint) {
        mMarker.setPosition(MapFactory.getInstance().createLatLng(geoPoint));
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
        mMarker.setTitle(title);
    }

    @Override
    public void setSnippet(String snippet) {
        mSnippet = snippet;
        mMarker.setSnippet(snippet);
    }

    @Override
    public void showInfoWindow() {
        if (mTitle != null) {
            mMarker.setTitle(mTitle);
        }
        if (mSnippet != null) {
            mMarker.setSnippet(mSnippet);
        }
        mMarker.showInfoWindow();
    }

    @Override
    public void hideInfoWindow() {
        mMarker.hideInfoWindow();
        mMarker.setTitle(null);
        mMarker.setSnippet(null);
    }

    @Override
    public void setIcon(int resId) {
        try {
            mMarker.setIcon(MapFactory.getInstance().createBitmapDescriptor(resId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideMarker() {
        if (mMarker.getAlpha() >= 1) {
            MapFactory.getInstance().createExitAnimator(this).start();
        }
    }

    @Override
    public void displayMarker() {
        if (mMarker.getAlpha() <= 0) {
            MapFactory.getInstance().createEnterAnimator(this).start();
        }
    }
}
