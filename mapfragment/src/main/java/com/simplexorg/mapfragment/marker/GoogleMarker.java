package com.simplexorg.mapfragment.marker;

import com.google.android.gms.maps.model.Marker;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.util.Factory;

public class GoogleMarker implements BaseMarker {
    private Marker mMarker;
    private GeoPoint mPos;

    public GoogleMarker(Marker marker) {
        mMarker = marker;
        mPos = Factory.getInstance().createGeoPoint(mMarker.getPosition());
    }

    @Override
    public GeoPoint getPosition() {
        mPos = Factory.getInstance().createGeoPoint(mMarker.getPosition());
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
        mMarker.setPosition(Factory.getInstance().createLatLng(geoPoint));
    }

    @Override
    public void setTitle(String title) {
        mMarker.setTitle(title);
    }

    @Override
    public void setSnippet(String snippet) {
        mMarker.setSnippet(snippet);
    }

    @Override
    public void showInfoWindow() {
        mMarker.showInfoWindow();
    }

    @Override
    public void hideInfoWindow() {
        mMarker.hideInfoWindow();
    }

    @Override
    public void setIcon(int resId) {
        mMarker.setIcon(Factory.getInstance().createBitmapDescriptor(resId));
    }

    @Override
    public void hideMarker() {
        if (mMarker.getAlpha() >= 1) {
            Factory.getInstance().createExitAnimator(this).start();
        }
    }

    @Override
    public void displayMarker() {
        if (mMarker.getAlpha() <= 0) {
            Factory.getInstance().createEnterAnimator(this).start();
        }
    }

    public Marker getRealMarker() {
        return mMarker;
    }
}
