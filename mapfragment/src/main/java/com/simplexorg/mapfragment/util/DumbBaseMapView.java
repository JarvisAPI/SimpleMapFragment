package com.simplexorg.mapfragment.util;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;

import com.simplexorg.mapfragment.map.BaseCancelableCallback;
import com.simplexorg.mapfragment.map.BaseMapPresenter;
import com.simplexorg.mapfragment.map.BaseMapView;
import com.simplexorg.mapfragment.map.BaseOnCameraIdleListener;
import com.simplexorg.mapfragment.map.BaseOnInfoWindowClickListener;
import com.simplexorg.mapfragment.map.BaseOnMapClickListener;
import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.marker.BaseOnMarkerClickListener;
import com.simplexorg.mapfragment.model.GeoPoint;

/**
 * Used as place holder for map view until real map view is instantiated.
 * This avoids null checks and null pointer crashes due to accessing a null map view.
 */
public class DumbBaseMapView implements BaseMapView {
    private static final String TAG = DumbBaseMapView.class.getSimpleName();
    private static final String PREFIX = "Uninitialized: default implementation called: ";

    @Override
    public boolean setMapStyle(Object... objects) {
        Log.d(TAG,  PREFIX + "setMapStyle(Object... objects)");
        return false;
    }

    @Override
    public OnClickListener getMyLocationClickListener() {
        Log.d(TAG,  PREFIX + "getMyLocationClickListener()");
        return null;
    }

    @Override
    public Point projectToScreenLocation(GeoPoint geoPoint) {
        Log.d(TAG,  PREFIX + "projectToScreenLocation(GeoPoint geoPoint)");
        return null;
    }

    @Override
    public GeoPoint projectFromScreenLocation(Point point) {
        Log.d(TAG,  PREFIX + "projectFromScreenLocation(Point point)");
        return null;
    }

    @Override
    public GeoPoint getCameraLocationCenter() {
        Log.d(TAG,  PREFIX + "getCameraLocationCenter()");
        return null;
    }

    @Override
    public void animateCamera(GeoPoint geoPoint, float zoomLevel) {
        Log.d(TAG,  PREFIX + "animateCamera(GeoPoint geoPoint, float zoomLevel)");
    }

    @Override
    public void animateCamera(GeoPoint geoPoint, float zoomLevel, BaseCancelableCallback callback) {
        Log.d(TAG,  PREFIX + "animateCamera(GeoPoint geoPoint, float zoomLevel, BaseCancelableCallback callback)");
    }

    @Override
    public float getCameraZoomLevel() {
        Log.d(TAG,  PREFIX + "getCameraZoomLevel()");
        return 0;
    }

    @Override
    public BaseMarker addMarker(BaseMarkerOptions options) {
        Log.d(TAG,  PREFIX + "addMarker(BaseMarkerOptions options)");
        return null;
    }

    @Override
    public void setOnMapClickListener(BaseOnMapClickListener listener) {
        Log.d(TAG,  PREFIX + "setOnMapClickListener(BaseOnMapClickListener listener)");
    }

    @Override
    public void setOnMarkerClickListener(BaseOnMarkerClickListener listener) {
        Log.d(TAG,  PREFIX + "setOnMarkerClickListener(BaseOnMarkerClickListener listener)");
    }

    @Override
    public void setOnCameraIdleListener(BaseOnCameraIdleListener listener) {
        Log.d(TAG,  PREFIX + "setOnCameraIdleListener(BaseOnCameraIdleListener listener)");
    }

    @Override
    public void setOnInfoWindowClickListener(BaseOnInfoWindowClickListener listener) {
        Log.d(TAG,  PREFIX + "setOnInfoWindowClickListener(BaseOnInfoWindowClickListener listener)");
    }

    @Override
    public void setPresenter(BaseMapPresenter presenter) {
        Log.d(TAG,  PREFIX + "setPresenter(BaseMapPresenter presenter)");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, PREFIX + "onSaveInstanceState(Bundle outState)");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, PREFIX + "onRestoreInstanceState(Bundle savedInstanceState)");
    }
}
