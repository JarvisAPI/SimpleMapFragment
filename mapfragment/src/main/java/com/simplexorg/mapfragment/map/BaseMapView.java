package com.simplexorg.mapfragment.map;


import android.graphics.Point;
import android.view.View.OnClickListener;

import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.marker.BaseOnMarkerClickListener;
import com.simplexorg.mapfragment.model.GeoPoint;

public interface BaseMapView {
    /**
     * Optional.
     * @param objects arguments.
     * @return true if style set false otherwise.
     */
    boolean setMapStyle(Object... objects);

    OnClickListener getMyLocationClickListener();

    Point projectToScreenLocation(GeoPoint geoPoint);

    GeoPoint getCameraLocationCenter();

    void animateCamera(GeoPoint geoPoint, float zoomLevel);

    float getCameraZoomLevel();

    BaseMarker addMarker(BaseMarkerOptions options);

    void setOnMapClickListener(BaseOnMapClickListener listener);

    void setOnMarkerClickListener(BaseOnMarkerClickListener listener);

    void setOnCameraIdleListener(BaseOnCameraIdleListener listener);

    void setOnInfoWindowClickListener(BaseOnInfoWindowClickListener listener);

    void setOnMapReadyCallback(BaseOnMapReadyCallback callback);

    void hideMarkers();

    void displayMarkers();

    void setPresenter(BaseMapPresenter presenter);
}
