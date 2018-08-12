package com.simplexorg.mapfragment.map;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.simplexorg.mapfragment.R;
import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.marker.BaseOnMarkerClickListener;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.util.Factory;


public class MapFragment extends Fragment {
    private static final String TAG = MapFragment.class.getSimpleName();
    private BaseMapView mMapView;
    private boolean mMapReady;
    private BaseOnMapReadyCallback mOnMapReadyCallback;

    public MapFragment() {
        mMapView = Factory.getInstance().createDumbBaseMapView();
        mMapReady = false;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment_map, container, false);
        mMapView = Factory.getInstance().createBaseMapView(view.findViewById(R.id.map_map_view), savedInstanceState);
        BasicMapPresenter.attach(mMapView, Factory.getInstance().createBaseMapModel());
        mMapView.setOnMapReadyCallback(() -> {
            mMapReady = true;
            if (mOnMapReadyCallback != null) {
                mOnMapReadyCallback.onMapReady();
            }
        });
        return view;
    }

    public void setOnMapReadyCallback(BaseOnMapReadyCallback callback) {
        mOnMapReadyCallback = callback;
    }

    public boolean setMapStyle(Context context, int resId) {
        return mMapReady && mMapView.setMapStyle(context, resId);
    }

    public OnClickListener getMyLocationClickListener() throws SecurityException {
        if (!mMapReady) {
            Log.e(TAG, "Map Not Ready!");
            return null;
        }
        return mMapView.getMyLocationClickListener();
    }

    public Point projectToScreenLocation(GeoPoint geoPoint) {
        if (!mMapReady) {
            Log.e(TAG, "Map Not Ready!");
            return null;
        }
        return mMapView.projectToScreenLocation(geoPoint);
    }

    public GeoPoint getCameraLocationCenter() {
        if (!mMapReady) {
            Log.e(TAG, "Map Not Ready!");
            return null;
        }
        return mMapView.getCameraLocationCenter();
    }

    public void animateCamera(GeoPoint geoPoint, float zoomLevel) {
        if (!mMapReady) {
            Log.e(TAG, "Map Not Ready!");
            return;
        }
        mMapView.animateCamera(geoPoint, zoomLevel);
    }

    public float getCameraZoomLevel() {
        if (!mMapReady) {
            Log.e(TAG, "Map Not Ready!");
            return Float.NaN;
        }
        return mMapView.getCameraZoomLevel();
    }

    public BaseMarker addMarker(BaseMarkerOptions options) {
        if (!mMapReady) {
            Log.e(TAG, "Map Not Ready!");
            return null;
        }
        return mMapView.addMarker(options);
    }

    public void setOnMapClickListener(BaseOnMapClickListener listener) {
        mMapView.setOnMapClickListener(listener);
    }

    public void setOnMarkerClickListener(BaseOnMarkerClickListener listener) {
        mMapView.setOnMarkerClickListener(listener);
    }

    public void setOnCameraIdleListener(BaseOnCameraIdleListener listener) {
        mMapView.setOnCameraIdleListener(listener);
    }

    public void setOnInfoWindowClickListener(BaseOnInfoWindowClickListener listener) {
        mMapView.setOnInfoWindowClickListener(listener);
    }
}
