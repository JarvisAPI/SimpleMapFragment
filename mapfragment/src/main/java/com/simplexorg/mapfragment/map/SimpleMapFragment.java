package com.simplexorg.mapfragment.map;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;
import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.marker.BaseOnMarkerClickListener;
import com.simplexorg.mapfragment.model.BaseMapModel;
import com.simplexorg.mapfragment.model.BaseModelDataRetriever;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel;
import com.simplexorg.mapfragment.util.Factory;


public class SimpleMapFragment extends SupportMapFragment {
    private static final String TAG = SimpleMapFragment.class.getSimpleName();
    private BaseMapView mMapView;
    private BaseMapModel<SelectableIconModel> mMapModel;
    private BaseOnMapReadyCallback mOnMapReadyCallback;

    public SimpleMapFragment() {
        mMapView = Factory.getInstance().createDumbBaseMapView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getMapAsync((map) -> {
            mMapView = Factory.getInstance().createBaseMapView(map, view);
            mMapModel = Factory.getInstance().createBaseMapModel();
            BasicMapPresenter.attach(mMapView, mMapModel);
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
        return mMapView.setMapStyle(context, resId);
    }

    public OnClickListener getMyLocationClickListener() throws SecurityException {
        return mMapView.getMyLocationClickListener();
    }

    public Point projectToScreenLocation(GeoPoint geoPoint) {
        return mMapView.projectToScreenLocation(geoPoint);
    }

    public GeoPoint getCameraLocationCenter() {
        return mMapView.getCameraLocationCenter();
    }

    public void animateCamera(GeoPoint geoPoint, float zoomLevel) {
        mMapView.animateCamera(geoPoint, zoomLevel);
    }

    public void animateCamera(GeoPoint geoPoint, float zoomLevel, BaseCancelableCallback callback) {
        mMapView.animateCamera(geoPoint, zoomLevel, callback);
    }

    public float getCameraZoomLevel() {
        return mMapView.getCameraZoomLevel();
    }

    public BaseMarker addMarker(BaseMarkerOptions options) {
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

    public void setDataRetriever(BaseModelDataRetriever<SelectableIconModel> dataRetriever) {
        if (mMapModel != null) {
            mMapModel.setDataRetriever(dataRetriever);
        }
    }
}