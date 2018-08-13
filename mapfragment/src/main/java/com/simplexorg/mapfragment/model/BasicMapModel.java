package com.simplexorg.mapfragment.model;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BasicMapModel implements BaseMapModel<SelectableIconModel> {
    private static final String TAG = BasicMapModel.class.getSimpleName();
    private static final String PARCEL_MODELS = "iconModels";

    private ArrayList<SelectableIconModel> mIconModels;
    private BaseModelDataRetriever<SelectableIconModel> mDataRetriever;
    private Observer mObserver;

    public BasicMapModel() {
        mIconModels = new ArrayList<>();
    }

    @Override
    public void setDataRetriever(BaseModelDataRetriever<SelectableIconModel> dataRetriever) {
        mDataRetriever = dataRetriever;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(PARCEL_MODELS, mIconModels);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mIconModels = savedInstanceState.getParcelableArrayList(PARCEL_MODELS);
    }

    @Override
    public void setObserver(Observer observer) {
        mObserver = observer;
    }

    @Override
    public void loadData(GeoPoint geoPoint) {
        if (mDataRetriever != null) {
            mDataRetriever.getModels((modelList) -> {
                Log.d(TAG, "retrieved modelList: " + modelList);
                mIconModels.clear();
                mIconModels.addAll(modelList);
                if (mObserver != null) {
                    mObserver.update();
                }
            }, geoPoint);
        }
    }

    @Override
    public List<SelectableIconModel> getIconModels() {
        return mIconModels;
    }
}
