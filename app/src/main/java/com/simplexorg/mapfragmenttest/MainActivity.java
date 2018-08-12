package com.simplexorg.mapfragmenttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.simplexorg.mapfragment.map.MapFragment;
import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.model.GeoPoint;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mMapFragment.setOnMapReadyCallback(() -> {
            Log.d(TAG, "Center: " + mMapFragment.getCameraLocationCenter());
            BaseMarker baseMarker = mMapFragment.addMarker(new BaseMarkerOptions()
                    .icon(R.drawable.basic_general_store_icon_v0)
                    .position(new GeoPoint(0, 0)));
            Log.d(TAG, "Marker position: " + baseMarker.getPosition());
            Log.d(TAG, "Marker alpha: " + baseMarker.getAlpha());
            mMapFragment.animateCamera(new GeoPoint(70, 70), 17);
            mMapFragment.setOnMapClickListener((GeoPoint geoPoint) -> {
                Log.d(TAG, "Clicked: " + geoPoint);
            });
        });
    }
}
