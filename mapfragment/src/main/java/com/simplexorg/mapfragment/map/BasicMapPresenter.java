package com.simplexorg.mapfragment.map;

import android.support.annotation.NonNull;
import android.util.Log;

import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.model.BaseMapModel;
import com.simplexorg.mapfragment.model.BaseOnModelUpdateListener;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel;

import java.util.ArrayList;
import java.util.List;

public class BasicMapPresenter implements BaseMapPresenter, BaseOnModelUpdateListener {
    private static final String TAG = BasicMapPresenter.class.getSimpleName();

    private BaseMapView mMapView;
    private BaseMapModel<SelectableIconModel> mMapModel;

    private GeoPoint mLastCameraCenter;
    private BaseMarker mLastClickedMarker;

    private List<BaseMarker> mCurrentMarkers;

    private BasicMapPresenter() {
        mCurrentMarkers = new ArrayList<>();
    }

    static void attach(BaseMapView baseMapView, BaseMapModel<SelectableIconModel> baseMapModel) {
        BasicMapPresenter presenter = new BasicMapPresenter();
        presenter.mMapView = baseMapView;
        presenter.mMapModel = baseMapModel;
        baseMapView.setPresenter(presenter);
        presenter.onCameraIdle();
    }

    @Override
    public void onCameraIdle() {
        Log.d(TAG, "onCameraIdle");
        float MARKER_DISPLAY_ZOOM_LEVEL = 17f;
        float MAX_DEVIATION_DISTANCE = 500;
        GeoPoint currentCameraCenter = mMapView.getCameraLocationCenter();

        if (mLastCameraCenter == null) {
            mLastCameraCenter = currentCameraCenter;
        }

        if (mMapView.getCameraZoomLevel() >= MARKER_DISPLAY_ZOOM_LEVEL) {
            if (GeoPoint.distance(currentCameraCenter, mLastCameraCenter) > MAX_DEVIATION_DISTANCE) {
                mLastCameraCenter = currentCameraCenter;
                mMapModel.loadData(currentCameraCenter, this);
            }
        }
    }

    private void updateMapMarkers(List<SelectableIconModel> newIconModels) {
        // For each new icon if it's not in the old icon list then we add it to the map.
        for (SelectableIconModel newIconModel : newIconModels) {
            boolean match = false;
            Log.d(TAG, "newIconModelId: " + newIconModel.getId());
            for (BaseMarker oldMarker : mCurrentMarkers) {
                SelectableIconModel oldIconModel = getIconModel(oldMarker);
                Log.d(TAG, "oldIconId: " + oldIconModel.getId());
                if (oldIconModel.getId().equals(newIconModel.getId())) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                addMarker(newIconModel);
            }
        }
        // For each old icon if it's not in the new icon list then we remove it from the map.
        for (int i = 0; i < mCurrentMarkers.size(); i++) {
            boolean match = false;
            SelectableIconModel oldIconModel = getIconModel(mCurrentMarkers.get(i));
            for (SelectableIconModel newIconModel : newIconModels) {
                if (oldIconModel.getId().equals(newIconModel.getId())) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                BaseMarker baseMarker = mCurrentMarkers.remove(i);
                baseMarker.remove();
            }
        }
    }

    @NonNull
    private SelectableIconModel getIconModel(BaseMarker baseMarker) {
        Object tag = baseMarker.getTag();
        if (tag != null &&
                tag instanceof SelectableIconModel) {
            return (SelectableIconModel) tag;
        }
        return SelectableIconModel.getDefault();
    }

    /**
     * Add marker to the map.
     *
     * @param iconModel the icon mode.
     */
    private void addMarker(SelectableIconModel iconModel) {
        Log.d(TAG, "addMarker in presenter!");
        BaseMarker baseMarker = mMapView.addMarker(new BaseMarkerOptions()
                .position(iconModel.getPos())
                .icon(iconModel.getNormalResId()));
        baseMarker.setTag(iconModel);
        mCurrentMarkers.add(baseMarker);
        baseMarker.setAlpha(0);
    }

    @Override
    public void onMarkerClick(BaseMarker marker) {
        if (marker.getTag() == null) {
            return;
        }
        if (mLastClickedMarker == null) {
            mLastClickedMarker = marker;
            selectMarker(mLastClickedMarker);
            return;
        }
        if (mLastClickedMarker == marker) {
            if (getIconModel(mLastClickedMarker).getState() == SelectableIconModel.NORMAL) {
                selectMarker(mLastClickedMarker);
            } else {
                deselectMarker(mLastClickedMarker);
            }
        } else {
            deselectMarker(mLastClickedMarker);
            selectMarker(marker);
            mLastClickedMarker = marker;
        }
        marker.showInfoWindow();
    }

    private void selectMarker(BaseMarker baseMarker) {
        baseMarker.showInfoWindow();
        SelectableIconModel iconModel = getIconModel(baseMarker);
        iconModel.changeState(SelectableIconModel.SELECTED);
        baseMarker.setIcon(iconModel.getSelectedResId());
    }

    private void deselectMarker(BaseMarker baseMarker) {
        baseMarker.hideInfoWindow();
        SelectableIconModel iconModel = getIconModel(baseMarker);
        iconModel.changeState(SelectableIconModel.NORMAL);
        baseMarker.setIcon(iconModel.getNormalResId());
    }

    @Override
    public void onMapClick(GeoPoint geoPoint) {
        if (mLastClickedMarker != null) {
            deselectMarker(mLastClickedMarker);
            mLastClickedMarker = null;
        }
    }

    @Override
    public void onModelUpdate() {
        Log.d(TAG, "Model Update Event Received!");
        List<SelectableIconModel> newIconModels = mMapModel.getIconModels();
        updateMapMarkers(newIconModels);
        mMapView.displayMarkers();
    }
}
