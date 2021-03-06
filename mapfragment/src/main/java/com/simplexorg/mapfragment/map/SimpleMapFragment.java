package com.simplexorg.mapfragment.map;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.marker.BaseOnMarkerClickListener;
import com.simplexorg.mapfragment.model.BaseMapModel;
import com.simplexorg.mapfragment.model.BaseModelDataRetriever;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel;
import com.simplexorg.mapfragment.util.MapFactory;


public class SimpleMapFragment extends SupportMapFragment
        implements OnMapReadyCallback {
    private static final String TAG = SimpleMapFragment.class.getSimpleName();
    private BaseMapView mMapView;
    private BaseMapPresenter<SelectableIconModel> mMapPresenter;
    private BaseMapModel<SelectableIconModel> mMapModel;
    private BaseOnMapReadyCallback mOnMapReadyCallback;
    private Bundle mSavedInstanceState;

    public SimpleMapFragment() {
        mMapView = MapFactory.getInstance().createDumbBaseMapView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getMapAsync(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
        if (mMapModel != null) {
            mMapModel.onSaveInstanceState(outState);
        }
        if (mMapPresenter != null) {
            mMapPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
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

    public void setMyLocationEnabled(boolean enabled) {
        mMapView.setMyLocationEnabled(enabled);
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

    public void setOnMarkerDragListener(BaseOnMarkerDragListener listener) {
        mMapView.setOnMarkerDragListener(listener);
    }

    public void setDataRetriever(BaseModelDataRetriever<SelectableIconModel> dataRetriever) {
        if (mMapModel != null) {
            mMapModel.setDataRetriever(dataRetriever);
        }
    }

    public GeoPoint projectFromScreenLocation(Point point) {
        return mMapView.projectFromScreenLocation(point);
    }

    /**
     * Triggers the map to update its current list of markers.
     */
    public void refreshMarkers() {
        if (mMapModel != null) {
            mMapModel.loadMarkers(mMapView.getCameraLocationCenter(), null);
        }
    }

    public void setMaxZoomPreference(float zoomPreference) {
        mMapView.setMaxZoomPreference(zoomPreference);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapView = MapFactory.getInstance().createBaseMapView(googleMap, getView());
        mMapModel = MapFactory.getInstance().createBaseMapModel();
        mMapPresenter = MapFactory.getInstance().createBaseMapPresenter();
        mMapPresenter.attach(mMapView, mMapModel);
        if (mOnMapReadyCallback != null) {
            mOnMapReadyCallback.onMapReady();
        }
        if (mSavedInstanceState != null) {
            mMapView.onRestoreInstanceState(mSavedInstanceState);
            mMapModel.onRestoreInstanceState(mSavedInstanceState);
            mMapPresenter.onRestoreInstanceState(mSavedInstanceState);
            mSavedInstanceState = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView = null;
    }


    public boolean hasMakerWithinDistance(GeoPoint geoPoint, double range) {
        return mMapModel.hasMarkerWithinDistance(geoPoint, range);
    }
}
