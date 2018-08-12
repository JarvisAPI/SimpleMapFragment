package com.simplexorg.mapfragment.map;

import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.model.GeoPoint;

public interface BaseMapPresenter {
    void onCameraIdle();

    void onMarkerClick(BaseMarker marker);

    void onMapClick(GeoPoint geoPoint);
}
