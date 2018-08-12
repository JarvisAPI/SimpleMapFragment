package com.simplexorg.mapfragment.map;

import android.support.annotation.NonNull;
import android.util.Log;

import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.model.BaseMapModel;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel;

import java.util.ArrayList;
import java.util.List;

public class BasicMapPresenter implements BaseMapPresenter {
    private static final String TAG = BasicMapPresenter.class.getSimpleName();

    private BaseMapView mMapView;
    private BaseMapModel<SelectableIconModel> mMapModel;

    private GeoPoint mLastCameraCenter;
    private BaseMarker mLastClickedMarker;

    private List<BaseMarker> mCurrentMarkers;

    public BasicMapPresenter() {
        mCurrentMarkers = new ArrayList<>();
    }

    static void attach(BaseMapView baseMapView, BaseMapModel<SelectableIconModel> baseMapModel) {
        BasicMapPresenter presenter = new BasicMapPresenter();
        presenter.mMapView = baseMapView;
        presenter.mMapModel = baseMapModel;
        baseMapView.setPresenter(presenter);
    }

    @Override
    public void onCameraIdle() {
        float MARKER_DISPLAY_ZOOM_LEVEL = 17f;
        float MAX_DEVIATION_DISTANCE = 500;
        GeoPoint currentCameraCenter = mMapView.getCameraLocationCenter();

        if (mLastCameraCenter == null) {
            mLastCameraCenter = currentCameraCenter;
        }

        if (mMapView.getCameraZoomLevel() >= MARKER_DISPLAY_ZOOM_LEVEL) {
            if (GeoPoint.distance(currentCameraCenter, mLastCameraCenter) > MAX_DEVIATION_DISTANCE) {
                mLastCameraCenter = currentCameraCenter;
                mMapModel.loadData(currentCameraCenter, () -> {
                    Log.d(TAG, "Model Update Event Received!");
                    List<SelectableIconModel> newIconModels = mMapModel.getIconModels();
                    updateMapMarkers(newIconModels);
                });
            }
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
            for (SelectableIconModel newIconModel : newIconModels) {
                SelectableIconModel oldIconModel = getIconModel(mCurrentMarkers.get(i));
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
     * @param iconModel the icon mode.
     */
    private void addMarker(SelectableIconModel iconModel) {
        BaseMarker baseMarker = mMapView.addMarker(new BaseMarkerOptions()
                .position(iconModel.getPos())
                .icon(iconModel.getNormalResId()));
        baseMarker.setTag(iconModel);
        baseMarker.setAlpha(0);
    }

    @Override
    public void onMarkerClick(BaseMarker marker) {
        if (mLastClickedMarker == null) {
            mLastClickedMarker = marker;
            selectIcon(mLastClickedMarker, getIconModel(marker));
            return;
        }
        if (mLastClickedMarker == marker) {
            if (getIconModel(mLastClickedMarker).getState() == SelectableIconModel.NORMAL) {
                selectIcon(mLastClickedMarker, getIconModel(mLastClickedMarker));
            } else {
                deselectIcon(mLastClickedMarker, getIconModel(mLastClickedMarker));
            }
        } else {
            deselectIcon(mLastClickedMarker, getIconModel(mLastClickedMarker));
            selectIcon(marker, getIconModel(marker));
            mLastClickedMarker = marker;
        }
        marker.showInfoWindow();
    }

    private void selectIcon(BaseMarker baseMarker, SelectableIconModel iconModel) {
        baseMarker.showInfoWindow();
        iconModel.changeState(SelectableIconModel.SELECTED);
        baseMarker.setIcon(iconModel.getSelectedResId());
    }

    private void deselectIcon(BaseMarker baseMarker, SelectableIconModel iconModel) {
        baseMarker.hideInfoWindow();
        iconModel.changeState(SelectableIconModel.NORMAL);
        baseMarker.setIcon(iconModel.getNormalResId());
    }

    @Override
    public void onMapClick(GeoPoint geoPoint) {
        if (mLastClickedMarker != null) {
            deselectIcon(mLastClickedMarker, getIconModel(mLastClickedMarker));
            mLastClickedMarker = null;
        }
    }
}
