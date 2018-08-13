package com.simplexorg.mapfragment.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BasicMapModel implements BaseMapModel<SelectableIconModel> {
    private static final String TAG = BasicMapModel.class.getSimpleName();

    private List<SelectableIconModel> mIconModels;
    private BaseModelDataRetriever<SelectableIconModel> mDataRetriever;

    public BasicMapModel() {
        mIconModels = new ArrayList<>();
    }

    @Override
    public void setDataRetriever(BaseModelDataRetriever<SelectableIconModel> dataRetriever) {
        mDataRetriever = dataRetriever;
    }

    @Override
    public void loadData(GeoPoint geoPoint, BaseOnModelUpdateListener onModelUpdateListener) {
        if (mDataRetriever != null) {
            mDataRetriever.getModels((modelList) -> {
                Log.d(TAG, "retrieved modelList: " + modelList);
                mIconModels.clear();
                mIconModels.addAll(modelList);
                onModelUpdateListener.onModelUpdate();
            }, geoPoint);
        }
    }

    @Override
    public List<SelectableIconModel> getIconModels() {
        return mIconModels;
    }
}
