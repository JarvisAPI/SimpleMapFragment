package com.simplexorg.mapfragment.model;

import java.util.List;

public interface BaseModelDataRetriever<T> {

    interface OnModelsRetrievedListener<T> {
        void onModelsRetrieved(List<T> modelList);
    }

    void getModels(OnModelsRetrievedListener<T> listener, Object... objects);
}
