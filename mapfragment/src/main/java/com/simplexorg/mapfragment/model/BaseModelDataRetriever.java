package com.simplexorg.mapfragment.model;

import java.util.List;

public interface BaseModelDataRetriever<T> {

    interface OnModelsRetrievedListener<T> {
        void onModelsRetrieved(List<T> modelList);
    }

    interface OnModelDetailsRetrievedListener<T> {
        void onModelDetailsRetrieved(T model);
    }

    void getModels(OnModelsRetrievedListener<T> listener, GeoPoint geoPoint);

    void getModelDetails(OnModelDetailsRetrievedListener<T> listener, BaseMarkerModel markerModel);
}
