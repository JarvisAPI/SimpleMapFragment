package com.simplexorg.mapfragmenttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.simplexorg.mapfragment.map.BaseCancelableCallback;
import com.simplexorg.mapfragment.map.SimpleMapFragment;
import com.simplexorg.mapfragment.model.GeoPoint;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SimpleMapFragment mMapFragment;

    private static final double LAT = 49.172047;
    private static final double LON = -123.182387;
    public static final GeoPoint mLoc = new GeoPoint(LAT, LON);

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
            mMapFragment.setDataRetriever(new SimpleModelDataRetriever());
        });
    }
}
