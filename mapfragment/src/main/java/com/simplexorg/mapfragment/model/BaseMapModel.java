package com.simplexorg.mapfragment.model;

import android.os.Bundle;

import java.util.List;

public interface BaseMapModel<T> {
    interface Observer {
        void update();
    }
    void loadData(GeoPoint geoPoint);

    List<T> getIconModels();

    void setDataRetriever(BaseModelDataRetriever<T> dataRetriever);

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void setObserver(Observer observer);
}