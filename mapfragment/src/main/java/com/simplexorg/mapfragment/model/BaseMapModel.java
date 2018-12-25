package com.simplexorg.mapfragment.model;

import android.os.Bundle;

import java.util.List;

public interface BaseMapModel<T> {
    interface Observer {
        int UPDATE_MARKERS = 0;
        int UPDATE_MARKER_DATA = 1;

        void update(int type, Object result, Object parcel);
    }

    /**
     *
     * @param geoPoint the point to load the markers from.
     * @param parcel object that is returned by the observer on update.
     */
    void loadMarkers(GeoPoint geoPoint, Object parcel);

    /**
     *
     * @param markerModel the marker model used to load the full model.
     * @param parcel object that is returned by the observer on update.
     */
    void loadMarkerData(BaseMarkerModel markerModel, Object parcel);

    List<T> getIconModels();

    void setDataRetriever(BaseModelDataRetriever<T> dataRetriever);

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void setObserver(Observer observer);

    /**
     * Check to see if there are any markers that are currently within range of a geo location.
     * @param geoPoint the location to check.
     * @param range the range in meters.
     * @return true if there is at least one marker within range, false otherwise.
     */
    boolean hasMarkerWithinDistance(GeoPoint geoPoint, double range);
}