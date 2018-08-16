package com.simplexorg.mapfragment.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.model.BaseMapModel;
import com.simplexorg.mapfragment.model.BaseMapModel.Observer;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel;
import com.simplexorg.mapfragment.util.Factory;
import com.simplexorg.mapfragment.util.Util;

import java.util.List;

public class BasicMapPresenter implements BaseMapPresenter<SelectableIconModel> {
    private static final String TAG = BasicMapPresenter.class.getSimpleName();
    @VisibleForTesting
    static final String PARCEL_CAM_LOC = "lastCamLoc";
    @VisibleForTesting
    static float MARKER_DISPLAY_ZOOM_LEVEL = 17f;
    @VisibleForTesting
    static float MAX_DEVIATION_DISTANCE = 500;

    private BaseMapView mMapView;
    private BaseMapModel<SelectableIconModel> mMapModel;

    private GeoPoint mLastCameraCenter;
    private BaseMarker mLastClickedMarker;
    private boolean mCurrentMarkersRefreshed;

    private List<BaseMarker> mCurrentMarkers;

    public BasicMapPresenter() {
        mCurrentMarkers = Factory.getInstance().createArrayList();
    }

    @Override
    public void attach(BaseMapView baseMapView, BaseMapModel<SelectableIconModel> baseMapModel) {
        mMapView = baseMapView;
        mMapModel = baseMapModel;
        baseMapView.setPresenter(this);
        baseMapModel.setObserver(this);
        onCameraIdle();
    }

    @Override
    public void onCameraIdle() {
        GeoPoint currentCameraCenter = mMapView.getCameraLocationCenter();

        if (mLastCameraCenter == null) {
            mLastCameraCenter = currentCameraCenter;
        }

        if (mMapView.getCameraZoomLevel() >= MARKER_DISPLAY_ZOOM_LEVEL) {
            if (Util.getInstance().distance(currentCameraCenter, mLastCameraCenter) > MAX_DEVIATION_DISTANCE) {
                mLastCameraCenter = currentCameraCenter;
                mMapModel.loadMarkers(currentCameraCenter, null);
            } else if (!mCurrentMarkersRefreshed) {
                displayMarkers();
                mCurrentMarkersRefreshed = true;
            }
        } else {
            hideMarkers();
            mCurrentMarkersRefreshed = false;
        }
    }

    private void displayMarkers() {
        for (BaseMarker baseMarker : mCurrentMarkers) {
            baseMarker.displayMarker();
        }
    }

    private void hideMarkers() {
        for (BaseMarker baseMarker : mCurrentMarkers) {
            baseMarker.hideMarker();
        }
    }

    private void updateMapMarkers(List<SelectableIconModel> newIconModels) {
        // For each new icon if it's not in the old icon list then we add it to the map.
        for (SelectableIconModel newIconModel : newIconModels) {
            boolean match = false;
            for (BaseMarker oldMarker : mCurrentMarkers) {
                SelectableIconModel oldIconModel = getIconModel(oldMarker);
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
        int iconRes = iconModel.getState() == SelectableIconModel.NORMAL ?
                iconModel.getNormalResId() :
                iconModel.getSelectedResId();
        BaseMarker baseMarker = mMapView.addMarker(new BaseMarkerOptions()
                .position(iconModel.getPos())
                .icon(iconRes));
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
    }

    private void selectMarker(BaseMarker baseMarker) {
        SelectableIconModel iconModel = getIconModel(baseMarker);
        iconModel.changeState(SelectableIconModel.SELECTED);
        baseMarker.setIcon(iconModel.getSelectedResId());
        mMapModel.loadMarkerData(iconModel, baseMarker);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(PARCEL_CAM_LOC, mLastCameraCenter);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mLastCameraCenter = savedInstanceState.getParcelable(PARCEL_CAM_LOC);
        update(Observer.UPDATE_MARKERS, null, null);
        for (BaseMarker baseMarker : mCurrentMarkers) {
            // Looping to restore any info windows.
            update(UPDATE_MARKER_DATA, baseMarker.getTag(), baseMarker);
        }
    }

    @Override
    public void update(int type, Object result, Object parcel) {
        switch (type) {
            case Observer.UPDATE_MARKERS:
                List<SelectableIconModel> newIconModels = mMapModel.getIconModels();
                updateMapMarkers(newIconModels);
                displayMarkers();
                break;
            case Observer.UPDATE_MARKER_DATA:
                SelectableIconModel model = (SelectableIconModel) result;
                BaseMarker baseMarker = (BaseMarker) parcel;
                baseMarker.setTag(model);
                baseMarker.setTitle(model.getTitle());
                baseMarker.setSnippet(model.getDescription());
                if (model.getState() == SelectableIconModel.SELECTED) {
                    baseMarker.showInfoWindow();
                }
                break;
        }

    }
}
