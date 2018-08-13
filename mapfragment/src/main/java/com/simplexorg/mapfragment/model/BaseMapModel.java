package com.simplexorg.mapfragment.model;

import java.util.List;

public interface BaseMapModel<T> {
    void loadData(GeoPoint geoPoint, BaseOnModelUpdateListener onModelUpdateListener);

    List<T> getIconModels();

    void setDataRetriever(BaseModelDataRetriever<T> dataRetriever);
}