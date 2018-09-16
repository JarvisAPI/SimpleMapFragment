package com.simplexorg.mapfragment.map;


import android.graphics.Point;
import android.os.Bundle;
import android.view.View.OnClickListener;

import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.marker.BaseOnMarkerClickListener;
import com.simplexorg.mapfragment.model.GeoPoint;

/**
 * Manages the map and all the markers on it.
 */
public interface BaseMapView {
    /**
     * Optional.
     * @param objects arguments.
     * @return true if style set false otherwise.
     */
    boolean setMapStyle(Object... objects);

    OnClickListener getMyLocationClickListener();

    Point projectToScreenLocation(GeoPoint geoPoint);

    GeoPoint projectFromScreenLocation(Point point);

    GeoPoint getCameraLocationCenter();

    void animateCamera(GeoPoint geoPoint, float zoomLevel);

    void animateCamera(GeoPoint geoPoint, float zoomLevel, BaseCancelableCallback callback);

    float getCameraZoomLevel();

    BaseMarker addMarker(BaseMarkerOptions options);

    void setOnMapClickListener(BaseOnMapClickListener listener);

    void setOnMarkerClickListener(BaseOnMarkerClickListener listener);

    void setOnCameraIdleListener(BaseOnCameraIdleListener listener);

    void setOnInfoWindowClickListener(BaseOnInfoWindowClickListener listener);

    void setPresenter(BaseMapPresenter presenter);

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);
}
