package com.simplexorg.mapfragmenttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.simplexorg.mapfragment.map.BaseCancelableCallback;
import com.simplexorg.mapfragment.map.SimpleMapFragment;
import com.simplexorg.mapfragment.model.BaseModelDataRetriever.OnModelsRetrievedListener;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SimpleMapFragment mMapFragment;

    private static final double LAT = 49.172047;
    private static final double LON = -123.182387;
    private final GeoPoint mLoc = new GeoPoint(LAT, LON);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFragment = (SimpleMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mMapFragment.setOnMapReadyCallback(() -> {
            mMapFragment.animateCamera(mLoc, 17f, new BaseCancelableCallback() {
                @Override
                public void onFinish() {
                    Log.d(TAG, "Animation finished");
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "Animation canceled");
                }
            });
            mMapFragment.setDataRetriever((OnModelsRetrievedListener<SelectableIconModel> listener, GeoPoint geoPoint) -> {
                List<SelectableIconModel> models = new ArrayList<>();
                models.add(new SelectableIconModel("0", mLoc,
                        R.drawable.basic_general_store_icon_v0,
                        R.drawable.basic_general_store_icon_selected_v0));
                listener.onModelsRetrieved(models);

            });
        });
    }
}
