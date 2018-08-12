package com.simplexorg.mapfragment.map;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.marker.BaseOnMarkerClickListener;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.util.Factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GoogleMapView implements BaseMapView,
        OnMapReadyCallback, OnMapClickListener, OnCameraIdleListener, OnMarkerClickListener,
        OnInfoWindowClickListener {
    private static final String TAG = GoogleMapView.class.getSimpleName();
    private MapView mMapView;
    private GoogleMap mMap;
    private BaseMapPresenter mPresenter;
    private Map<Marker, BaseMarker> mMarkerMapping;

    private BaseOnCameraIdleListener mOnCameraIdleListener;
    private BaseOnMapClickListener mOnMapClickListener;
    private BaseOnMarkerClickListener mOnMarkerClickListener;
    private BaseOnInfoWindowClickListener mOnInfoWindowClickListener;
    private BaseOnMapReadyCallback mOnMapReadyCallback;

    public GoogleMapView(MapView mapView, Bundle savedInstanceState) {
        mMapView = mapView;
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mMarkerMapping = new HashMap<>();
    }

    @Override
    public boolean setMapStyle(Object... objects) {
        Object arg0 = objects[0];
        Object arg1 = objects[1];

        if (arg0 instanceof Context && arg1 instanceof Integer) {
            Context context = (Context) arg0;
            int resId = (int) arg1;
            mMap.setMapStyle(Factory.getInstance().createMapStyleOptions(context, resId));
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "Google Map Ready");
        mMap = googleMap;

        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnCameraIdleListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        if (mOnMapReadyCallback != null) {
            mOnMapReadyCallback.onMapReady();
        }
    }

    @Override
    public OnClickListener getMyLocationClickListener() throws SecurityException {
        mMap.setMyLocationEnabled(true);
        if (mMapView.findViewById(Integer.parseInt("1")) != null) {
            final View myLocationButton = ((View) mMapView.findViewById(Integer.parseInt("1"))
                    .getParent()).findViewById(Integer.parseInt("2"));
            myLocationButton.setVisibility(View.GONE);
            return (View view) -> myLocationButton.performClick();
        }
        return null;
    }

    @Override
    public Point projectToScreenLocation(GeoPoint geoPoint) {
        return mMap.getProjection().toScreenLocation(Factory.getInstance().createLatLng(geoPoint));
    }

    @Override
    public GeoPoint getCameraLocationCenter() {
        return Factory.getInstance().createGeoPoint(mMap.getCameraPosition().target);
    }

    @Override
    public void animateCamera(GeoPoint geoPoint, float zoomLevel) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(Factory.getInstance().createLatLng(geoPoint))
                .zoom(zoomLevel)
                .build();
        mMap.animateCamera(Factory.getInstance().createCameraUpdate(cameraPosition));
    }

    @Override
    public float getCameraZoomLevel() {
        return mMap.getCameraPosition().zoom;
    }

    @Override
    public BaseMarker addMarker(BaseMarkerOptions options) {
        Marker marker = mMap.addMarker(getMarkerOptions(options));
        BaseMarker baseMarker = Factory.getInstance().createBaseMarker(marker);
        mMarkerMapping.put(marker, baseMarker);
        return baseMarker;
    }

    private MarkerOptions getMarkerOptions(BaseMarkerOptions options) {
        return new MarkerOptions()
                .alpha(options.getAlpha())
                .anchor(options.getAnchorU(), options.getAnchorV())
                .draggable(options.isDraggable())
                .flat(options.isFlat())
                .icon(Factory.getInstance().createBitmapDescriptor(options.getIcon()))
                .infoWindowAnchor(options.getInfoWindowAnchorU(), options.getInfoWindowAnchorV())
                .position(Factory.getInstance().createLatLng(options.getPosition()))
                .rotation(options.getRotation())
                .snippet(options.getSnippet())
                .title(options.getTitle())
                .visible(options.isVisible())
                .zIndex(options.getZIndex());
    }

    @Override
    public void hideMarkers() {
        Set<Marker> markerKeys = mMarkerMapping.keySet();
        for (Marker key : markerKeys) {
            BaseMarker baseMarker = mMarkerMapping.get(key);
            if (baseMarker.getAlpha() >= 1) {
                Factory.getInstance().createExitAnimator(baseMarker).start();
            }
        }
    }

    @Override
    public void displayMarkers() {
        Set<Marker> markerKeys = mMarkerMapping.keySet();
        for (Marker key : markerKeys) {
            BaseMarker baseMarker = mMarkerMapping.get(key);
            if (baseMarker.getAlpha() <= 0) {
                Factory.getInstance().createEnterAnimator(baseMarker).start();
            }
        }
    }

    @Override
    public void setPresenter(BaseMapPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setOnCameraIdleListener(BaseOnCameraIdleListener listener) {
        mOnCameraIdleListener = listener;
    }

    @Override
    public void setOnMapClickListener(BaseOnMapClickListener listener) {
        mOnMapClickListener = listener;
    }

    @Override
    public void setOnMarkerClickListener(BaseOnMarkerClickListener listener) {
        mOnMarkerClickListener = listener;
    }

    @Override
    public void setOnInfoWindowClickListener(BaseOnInfoWindowClickListener listener) {
        mOnInfoWindowClickListener = listener;
    }

    @Override
    public void setOnMapReadyCallback(BaseOnMapReadyCallback callback) {
        mOnMapReadyCallback = callback;
    }

    @Override
    public void onCameraIdle() {
        mPresenter.onCameraIdle();
        if (mOnCameraIdleListener != null) {
            mOnCameraIdleListener.onCameraIdle();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        GeoPoint geoPoint = Factory.getInstance().createGeoPoint(latLng);
        mPresenter.onMapClick(geoPoint);
        if (mOnMapClickListener != null) {
            mOnMapClickListener.onMapClick(geoPoint);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        BaseMarker baseMarker = mMarkerMapping.get(marker);
        mPresenter.onMarkerClick(baseMarker);
        if (mOnMarkerClickListener != null) {
            mOnMarkerClickListener.onMarkerClick(baseMarker);
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        BaseMarker baseMarker = mMarkerMapping.get(marker);
        if (mOnInfoWindowClickListener != null) {
            mOnInfoWindowClickListener.onInfoWindowClick(baseMarker);
        }
    }
}
