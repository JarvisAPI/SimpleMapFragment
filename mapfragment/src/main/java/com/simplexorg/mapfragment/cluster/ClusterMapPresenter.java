package com.simplexorg.mapfragment.cluster;

import android.content.Context;
import android.os.Bundle;

import com.simplexorg.mapfragment.map.BaseMapPresenter;
import com.simplexorg.mapfragment.map.BaseMapView;
import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.model.BaseMapModel;
import com.simplexorg.mapfragment.model.BaseMapModel.Observer;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel;
import com.simplexorg.mapfragment.util.Factory;

import java.util.List;

public class ClusterMapPresenter implements BaseMapPresenter<SelectableIconModel> {
    private static final String TAG = ClusterMapPresenter.class.getSimpleName();

    private List<BaseMarker> mCurrentMarkers;
    private BaseMapView mMapView;
    private BaseMapModel<SelectableIconModel> mMapModel;
    private GeoPoint mLastCameraLocation;
    private ClusterAlgorithm mAlgorithm;

    public ClusterMapPresenter(Context context) {
        mCurrentMarkers = Factory.getInstance().createArrayList();
        mAlgorithm = new ClusterAlgorithm(context);
    }

    @Override
    public void attach(BaseMapView baseMapView, BaseMapModel<SelectableIconModel> baseMapModel) {
        mMapView = baseMapView;
        mMapModel = baseMapModel;
        baseMapView.setPresenter(this);
        baseMapModel.setObserver(this);

        GeoPoint currentCameraCenter = mMapView.getCameraLocationCenter();
        if (mLastCameraLocation == null) {
            mLastCameraLocation = currentCameraCenter;
        }
        mAlgorithm.setMapView(mMapView);
    }

    @Override
    public void onCameraIdle() {
        GeoPoint currentCameraCenter = mMapView.getCameraLocationCenter();
        if (mLastCameraLocation == null) {
            mLastCameraLocation = currentCameraCenter;
        }

        mMapModel.loadMarkers(currentCameraCenter, null);
        mAlgorithm.cluster(mCurrentMarkers);
    }

    @Override
    public void onMarkerClick(BaseMarker marker) {

    }

    @Override
    public void onMapClick(GeoPoint geoPoint) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    private void updateMapMarkers(List<SelectableIconModel> newIconModels) {
        for (SelectableIconModel model : newIconModels) {
            if (!modelIsIncluded(model)) {
                addMarker(model);
            }
        }
    }

    /**
     * Add marker to the map.
     *
     * @param iconModel the icon mode.
     */
    private void addMarker(SelectableIconModel iconModel) {
        int iconRes = iconModel.getState() == SelectableIconModel.NORMAL ?
                iconModel.getNormalResId() :
                iconModel.getSelectedResId();
        BaseMarker baseMarker = mMapView.addMarker(new BaseMarkerOptions()
                .position(iconModel.getPos())
                .icon(iconRes));
        baseMarker.setTag(iconModel);
        mCurrentMarkers.add(baseMarker);
    }

    private boolean modelIsIncluded(SelectableIconModel model) {
        for (BaseMarker baseMarker : mCurrentMarkers) {
            if (baseMarker instanceof ClusterMarker) {
                if (modelIsIncludedInCluster((ClusterMarker) baseMarker, model)) {
                    return true;
                }
            } else {
                SelectableIconModel otherModel = (SelectableIconModel) baseMarker.getTag();
                if (otherModel.getId().equals(model.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean modelIsIncludedInCluster(ClusterMarker clusterMarker, SelectableIconModel model) {
        for (BaseMarker baseMarker : clusterMarker.getClusterMarkers()) {
            SelectableIconModel otherModel = (SelectableIconModel) baseMarker.getTag();
            if (otherModel.getId().equals(model.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(int type, SelectableIconModel result, Object parcel) {
        switch (type) {
            case Observer.UPDATE_MARKERS:
                List<SelectableIconModel> newIconModels = mMapModel.getIconModels();
                updateMapMarkers(newIconModels);
                break;
        }
    }
}
