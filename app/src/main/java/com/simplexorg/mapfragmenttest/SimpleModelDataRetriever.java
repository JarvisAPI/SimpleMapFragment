package com.simplexorg.mapfragmenttest;

import android.util.Log;

import com.simplexorg.mapfragment.model.BaseMarkerModel;
import com.simplexorg.mapfragment.model.BaseModelDataRetriever;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel;
import com.simplexorg.mapfragment.model.SelectableIconModel.Builder;

import java.util.ArrayList;
import java.util.List;

public class SimpleModelDataRetriever implements BaseModelDataRetriever<SelectableIconModel> {
    private final String TAG = SimpleModelDataRetriever.class.getSimpleName();

    public void getModels(OnModelsRetrievedListener<SelectableIconModel> listener, GeoPoint geoPoint) {
        List<SelectableIconModel> models = new ArrayList<>();
        GeoPoint ref = MainActivity.mLoc;
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            GeoPoint point = new GeoPoint(ref.latitude + offset, ref.longitude + offset);
            models.add(getIconModel(String.valueOf(i), point));
        }

        listener.onModelsRetrieved(models);
    }

    private SelectableIconModel getIconModel(String id, GeoPoint loc) {
        return new SelectableIconModel.Builder()
                .id(id)
                .position(loc)
                .normalResId(R.drawable.basic_general_store_icon_v0)
                .selectedResId(R.drawable.basic_general_store_icon_selected_v0)
                .title("Marker")
                .description("Snippet")
                .build();
    }

    @Override
    public void getModelDetails(OnModelDetailsRetrievedListener<SelectableIconModel> listener, BaseMarkerModel
            markerModel) {
        Log.d(TAG, "Get model details");
        if (markerModel.getId().equals("0")) {
            SelectableIconModel.Builder builder = new Builder((SelectableIconModel) markerModel);
            listener.onModelDetailsRetrieved(builder
                    .title("Other")
                    .description("Snip?")
                    .build());
        }
    }
}
