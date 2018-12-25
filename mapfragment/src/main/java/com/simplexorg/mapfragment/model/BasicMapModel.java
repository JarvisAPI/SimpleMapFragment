package com.simplexorg.mapfragment.model;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;

import com.simplexorg.mapfragment.util.MapFactory;
import com.simplexorg.mapfragment.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

import static com.simplexorg.mapfragment.model.BaseMapModel.Observer.UPDATE_MARKERS;
import static com.simplexorg.mapfragment.model.BaseMapModel.Observer.UPDATE_MARKER_DATA;

public class BasicMapModel implements BaseMapModel<SelectableIconModel> {
    private static final String TAG = BasicMapModel.class.getSimpleName();

    @VisibleForTesting
    static final String PARCEL_MODELS = "iconModels";

    private ArrayList<SelectableIconModel> mIconModels;
    private BaseModelDataRetriever<SelectableIconModel> mDataRetriever;
    private Observer mObserver;

    public BasicMapModel() {
        mIconModels = MapFactory.getInstance().createArrayList();
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
    public void loadMarkers(GeoPoint geoPoint, Object parcel) {
        if (mDataRetriever != null) {
            mDataRetriever.getModels((List<SelectableIconModel> modelList) -> {
                mIconModels.clear();
                mIconModels.addAll(modelList);
                if (mObserver != null) {
                    mObserver.update(UPDATE_MARKERS, null, parcel);
                }
            }, geoPoint);
        }
    }

    @Override
    public void loadMarkerData(BaseMarkerModel markerModel, Object parcel) {
        if (mDataRetriever != null) {
            mDataRetriever.getModelDetails((SelectableIconModel model) -> {
                if (mObserver != null) {
                    mObserver.update(UPDATE_MARKER_DATA, model, parcel);
                }
            }, markerModel);
        }
    }

    @Override
    public List<SelectableIconModel> getIconModels() {
        return mIconModels;
    }

    @Override
    public boolean hasMarkerWithinDistance(GeoPoint geoPoint, double range) {
        for (SelectableIconModel selectableIconModel : mIconModels) {
            if (MapUtil.getInstance().distance(selectableIconModel.getPos(), geoPoint) <= range) {
                return true;
            }
        }
        return false;
    }
}
