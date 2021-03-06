package com.simplexorg.mapfragment.util;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.simplexorg.mapfragment.map.BaseMapPresenter;
import com.simplexorg.mapfragment.map.BaseMapView;
import com.simplexorg.mapfragment.map.BasicMapPresenter;
import com.simplexorg.mapfragment.map.GoogleMapView;
import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerAnimator;
import com.simplexorg.mapfragment.marker.DropMarkerAnimator;
import com.simplexorg.mapfragment.marker.FadeMarkerAnimator;
import com.simplexorg.mapfragment.marker.GoogleMarker;
import com.simplexorg.mapfragment.model.BaseMapModel;
import com.simplexorg.mapfragment.model.BasicMapModel;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel;

import java.util.ArrayList;

public class MapFactory {
    private static MapFactory mFactory;

    private MapFactory() {

    }

    public <T> ArrayList<T> createArrayList() {
        return new ArrayList<>();
    }

    public LatLng createLatLng(@NonNull GeoPoint geoPoint) {
        return new LatLng(geoPoint.latitude, geoPoint.longitude);
    }

    public GeoPoint createGeoPoint(@NonNull LatLng latLng) {
        return new GeoPoint(latLng.latitude, latLng.longitude);
    }

    public GeoPoint createGeoPoint(double latitude, double longitude) {
        return new GeoPoint(latitude, longitude);
    }

    public BaseMapView createBaseMapView(GoogleMap googleMap, View mapView) {
        return new GoogleMapView(googleMap, mapView);
    }

    public BaseMapView createDumbBaseMapView() {
        return new DumbBaseMapView();
    }

    public BaseMapPresenter<SelectableIconModel> createBaseMapPresenter() {
        return new BasicMapPresenter();
    }

    public BitmapDescriptor createBitmapDescriptor(int resId) {
        return BitmapDescriptorFactory.fromResource(resId);
    }

    public BitmapDescriptor createBitmapDescriptor(Bitmap bitmap) {
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public MapStyleOptions createMapStyleOptions(Context context, int resId) {
        return MapStyleOptions.loadRawResourceStyle(context, resId);
    }

    public CameraUpdate createCameraUpdate(CameraPosition cameraPosition) {
        return CameraUpdateFactory.newCameraPosition(cameraPosition);
    }

    public GoogleMarker createGoogleMarker(Marker marker) {
        return new GoogleMarker(marker);
    }

    public ValueAnimator createValueAnimator(float... floats) {
        return ValueAnimator.ofFloat(floats);
    }

    public BaseMarkerAnimator createEnterAnimator(BaseMarker marker) {
        return new FadeMarkerAnimator(marker, true);
    }

    public BaseMarkerAnimator createExitAnimator(BaseMarker marker) {
        return new FadeMarkerAnimator(marker, false);
    }

    public BaseMarkerAnimator createDropAnimator(long duration, BaseMarker marker,
                                                 Handler handler, GeoPoint target, GeoPoint startLatLng) {
        return new DropMarkerAnimator(duration, marker, handler, target, startLatLng);
    }

    public BaseMapModel<SelectableIconModel> createBaseMapModel() {
        return new BasicMapModel();
    }

    public static MapFactory getInstance() {
        if (mFactory == null) {
            mFactory = new MapFactory();
        }
        return mFactory;
    }

    @VisibleForTesting
    public static void setFactory(MapFactory factory) {
        mFactory = factory;
    }
}
