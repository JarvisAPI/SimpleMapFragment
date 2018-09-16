package com.simplexorg.mapfragment.map;

import android.os.Bundle;

import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.model.BaseMapModel;
import com.simplexorg.mapfragment.model.BaseMapModel.Observer;
import com.simplexorg.mapfragment.model.GeoPoint;

public interface BaseMapPresenter<T> extends Observer<T> {
    void onCameraIdle();

    void onMarkerClick(BaseMarker marker);

    void onMapClick(GeoPoint geoPoint);

    void onSaveInstanceState(Bundle outState);

    /**
     * Triggers the presenter to update its view after restoring
     * saved state, therefore the view and model that is attached to this
     * presenter should have their state restored before this is called.
     * @param savedInstanceState the saved state.
     */
    void onRestoreInstanceState(Bundle savedInstanceState);

    void attach(BaseMapView baseMapView, BaseMapModel<T> baseMapModel);
}
