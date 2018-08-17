package com.simplexorg.mapfragment.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simplexorg.mapfragment.R;
import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.marker.BaseOnMarkerClickListener;
import com.simplexorg.mapfragment.marker.GoogleMarker;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.util.MapFactory;
import com.simplexorg.mapfragment.util.MapUtil;

public class GoogleMapView implements BaseMapView,
        OnMapClickListener, OnCameraIdleListener, OnMarkerClickListener,
        OnInfoWindowClickListener, OnMarkerDragListener {
    private static final String TAG = GoogleMapView.class.getSimpleName();
    private GoogleMap mMap;
    private View mView;
    private BaseMapPresenter mPresenter;

    private BaseOnCameraIdleListener mOnCameraIdleListener;
    private BaseOnMapClickListener mOnMapClickListener;
    private BaseOnMarkerClickListener mOnMarkerClickListener;
    private BaseOnInfoWindowClickListener mOnInfoWindowClickListener;
    private BaseOnMarkerDragListener mOnMarkerDragListener;

    public GoogleMapView(GoogleMap googleMap, View view) {
        Log.d(TAG, "Google Map Ready");
        mMap = googleMap;
        mView = view;

        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnCameraIdleListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }

    @Override
    public boolean setMapStyle(Object... objects) {
        Object arg0 = objects[0];
        Object arg1 = objects[1];

        if (arg0 instanceof Context && arg1 instanceof Integer) {
            Context context = (Context) arg0;
            int resId = (int) arg1;
            mMap.setMapStyle(MapFactory.getInstance().createMapStyleOptions(context, resId));
            return true;
        }
        return false;
    }

    @Override
    public void setMaxZoomPreference(float zoomPreference) {
        mMap.setMaxZoomPreference(zoomPreference);
    }

    @Override
    public void setMyLocationEnabled(boolean enabled) throws SecurityException {
        mMap.setMyLocationEnabled(enabled);
    }

    @Override
    public OnClickListener getMyLocationClickListener() throws SecurityException {
        mMap.setMyLocationEnabled(true);
        if (mView.findViewById(Integer.parseInt("1")) != null) {
            final View myLocationButton = ((View) mView.findViewById(Integer.parseInt("1"))
                    .getParent()).findViewById(Integer.parseInt("2"));
            myLocationButton.setVisibility(View.GONE);
            return (View view) -> myLocationButton.performClick();
        }
        return null;
    }

    @Override
    public GeoPoint projectFromScreenLocation(Point point) {
        return MapFactory.getInstance().createGeoPoint(mMap.getProjection().fromScreenLocation(point));
    }

    @Override
    public Point projectToScreenLocation(GeoPoint geoPoint) {
        return mMap.getProjection().toScreenLocation(MapFactory.getInstance().createLatLng(geoPoint));
    }

    @Override
    public GeoPoint getCameraLocationCenter() {
        return MapFactory.getInstance().createGeoPoint(mMap.getCameraPosition().target);
    }

    @Override
    public void animateCamera(GeoPoint geoPoint, float zoomLevel) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(MapFactory.getInstance().createLatLng(geoPoint))
                .zoom(zoomLevel)
                .build();
        mMap.animateCamera(MapFactory.getInstance().createCameraUpdate(cameraPosition));
    }

    @Override
    public void animateCamera(GeoPoint geoPoint, float zoomLevel, BaseCancelableCallback callback) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(MapFactory.getInstance().createLatLng(geoPoint))
                .zoom(zoomLevel)
                .build();
        mMap.animateCamera(MapFactory.getInstance().createCameraUpdate(cameraPosition),
                new CancelableCallback() {
                    @Override
                    public void onFinish() {
                        callback.onFinish();
                    }

                    @Override
                    public void onCancel() {
                        callback.onCancel();
                    }
                });
    }

    @Override
    public float getCameraZoomLevel() {
        return mMap.getCameraPosition().zoom;
    }

    @Override
    public BaseMarker addMarker(BaseMarkerOptions options) {
        Marker marker = mMap.addMarker(getMarkerOptions(options));
        GoogleMarker googleMarker = MapFactory.getInstance().createGoogleMarker(marker);
        marker.setTag(googleMarker);
        return googleMarker;
    }

    private MarkerOptions getMarkerOptions(BaseMarkerOptions options) {
        MarkerOptions markerOptions = new MarkerOptions()
                .alpha(options.getAlpha())
                .anchor(options.getAnchorU(), options.getAnchorV())
                .draggable(options.isDraggable())
                .flat(options.isFlat())
                .infoWindowAnchor(options.getInfoWindowAnchorU(), options.getInfoWindowAnchorV())
                .position(MapFactory.getInstance().createLatLng(options.getPosition()))
                .rotation(options.getRotation())
                .snippet(options.getSnippet())
                .title(options.getTitle())
                .visible(options.isVisible())
                .zIndex(options.getZIndex());
        if (options.getIcon() == R.drawable.map_baseline_place_black_48) {
            Bitmap original = BitmapFactory.decodeResource(mView.getResources(), options.getIcon());
            Bitmap tinted = MapUtil.getInstance().tintImage(original, ContextCompat.getColor(mView.getContext(),
                    android.R.color.holo_red_light));
            markerOptions.icon(MapFactory.getInstance().createBitmapDescriptor(tinted));
        } else {
            markerOptions.icon(MapFactory.getInstance().createBitmapDescriptor(options.getIcon()));
        }
        return markerOptions;
    }

    @Override
    public void setPresenter(BaseMapPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

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
    public void setOnMarkerDragListener(BaseOnMarkerDragListener listener) {
        mOnMarkerDragListener = listener;
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
        GeoPoint geoPoint = MapFactory.getInstance().createGeoPoint(latLng);
        mPresenter.onMapClick(geoPoint);
        if (mOnMapClickListener != null) {
            mOnMapClickListener.onMapClick(geoPoint);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        BaseMarker baseMarker = (GoogleMarker) marker.getTag();
        mPresenter.onMarkerClick(baseMarker);
        if (mOnMarkerClickListener != null) {
            mOnMarkerClickListener.onMarkerClick(baseMarker);
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        BaseMarker baseMarker = (GoogleMarker) marker.getTag();
        if (mOnInfoWindowClickListener != null) {
            mOnInfoWindowClickListener.onInfoWindowClick(baseMarker);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        if (mOnMarkerDragListener != null) {
            mOnMarkerDragListener.onMarkerDragStart((GoogleMarker) marker.getTag());
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        if (mOnMarkerDragListener != null) {
            mOnMarkerDragListener.onMarkerDrag((GoogleMarker) marker.getTag());
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (mOnMarkerDragListener != null) {
            mOnMarkerDragListener.onMarkerDragEnd((GoogleMarker) marker.getTag());
        }
    }
}
