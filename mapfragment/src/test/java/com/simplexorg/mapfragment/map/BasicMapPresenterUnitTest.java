package com.simplexorg.mapfragment.map;


import android.os.Bundle;

import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.model.BaseMapModel;
import com.simplexorg.mapfragment.model.BaseMapModel.Observer;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel;
import com.simplexorg.mapfragment.util.MapUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static com.simplexorg.mapfragment.map.BasicMapPresenter.MARKER_DISPLAY_ZOOM_LEVEL;
import static com.simplexorg.mapfragment.map.BasicMapPresenter.MAX_DEVIATION_DISTANCE;
import static com.simplexorg.mapfragment.map.BasicMapPresenter.PARCEL_CAM_LOC;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BasicMapPresenterUnitTest {
    private BasicMapPresenter mMapPresenter;
    @Mock private BaseMapView mMapView;
    @Mock private BaseMapModel<SelectableIconModel> mMapModel;
    @Mock private MapUtil mUtil;

    @Before
    public void setup() {
        initMocks(this);
        MapUtil.setUtil(mUtil);

        mMapPresenter = new BasicMapPresenter();
        mMapPresenter.attach(mMapView, mMapModel);
    }

    @After
    public void cleanup() {
        MapUtil.setUtil(null);
    }

    @Test
    public void test_onCameraIdle_loadMarkers() {
        GeoPoint currentCameraCenter = mock(GeoPoint.class);
        when(mMapView.getCameraLocationCenter()).thenReturn(currentCameraCenter);

        when(mMapView.getCameraZoomLevel()).thenReturn(MARKER_DISPLAY_ZOOM_LEVEL);
        when(mUtil.distance(any(GeoPoint.class), any(GeoPoint.class))).thenReturn((double) MAX_DEVIATION_DISTANCE * 2);

        mMapPresenter.onCameraIdle();
        verify(mMapModel).loadMarkers(currentCameraCenter, null);
    }

    @Test
    public void test_onCameraIdle_displayMarkers() {
        SelectableIconModel iconModel = mock(SelectableIconModel.class);
        when(iconModel.getId()).thenReturn("id");
        when(iconModel.getState()).thenReturn(SelectableIconModel.SELECTED);
        BaseMarker baseMarker = mock(BaseMarker.class);
        when(baseMarker.getTag()).thenReturn(iconModel);

        List<SelectableIconModel> iconModels = new ArrayList<>();
        iconModels.add(iconModel);
        when(mMapModel.getIconModels()).thenReturn(iconModels);

        when(mMapView.addMarker(any(BaseMarkerOptions.class))).thenReturn(baseMarker);

        when(mMapView.getCameraZoomLevel()).thenReturn(BasicMapPresenter.MARKER_DISPLAY_ZOOM_LEVEL);
        when(mUtil.distance(any(GeoPoint.class), any(GeoPoint.class))).thenReturn((double) MAX_DEVIATION_DISTANCE / 2);

        mMapPresenter.update(Observer.UPDATE_MARKERS, null, null);
        mMapPresenter.onCameraIdle();
        verify(baseMarker, times(2)).displayMarker();
        verify(baseMarker, times(2)).showInfoWindow();
    }

    @Test
    public void test_onCameraIdle_hideMarkers() {
        SelectableIconModel iconModel = mock(SelectableIconModel.class);
        when(iconModel.getId()).thenReturn("id");
        when(iconModel.getState()).thenReturn(SelectableIconModel.SELECTED);
        BaseMarker baseMarker = mock(BaseMarker.class);
        when(baseMarker.getTag()).thenReturn(iconModel);

        List<SelectableIconModel> iconModels = new ArrayList<>();
        iconModels.add(iconModel);
        when(mMapModel.getIconModels()).thenReturn(iconModels);

        when(mMapView.addMarker(any(BaseMarkerOptions.class))).thenReturn(baseMarker);

        when(mMapView.getCameraZoomLevel()).thenReturn(BasicMapPresenter.MARKER_DISPLAY_ZOOM_LEVEL / 2);

        mMapPresenter.update(Observer.UPDATE_MARKERS, null, null);
        mMapPresenter.onCameraIdle();
        verify(baseMarker).hideMarker();
        verify(baseMarker).hideInfoWindow();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void test_onMarkerClick_markerIsInvisible() {
        BaseMarker clickedMarker = mock(BaseMarker.class);
        when(clickedMarker.getAlpha()).thenReturn(0f);
        when(clickedMarker.getTag()).thenReturn(mock(BaseMarker.class));

        mMapPresenter.onMarkerClick(clickedMarker);

        verify(clickedMarker).getTag();
        verify(clickedMarker).getAlpha();
        verifyNoMoreInteractions(clickedMarker);
    }

    @Test
    public void test_onMarkerClick_lastClickedMarkerIsNull() {
        SelectableIconModel lastClickedMarkerModel = mock(SelectableIconModel.class);
        BaseMarker lastClickedMarker = mock(BaseMarker.class);

        when(lastClickedMarker.getTag()).thenReturn(lastClickedMarkerModel);

        when(lastClickedMarkerModel.getState()).thenReturn(SelectableIconModel.NORMAL);
        when(lastClickedMarkerModel.getNormalResId()).thenReturn(10);
        when(lastClickedMarkerModel.getSelectedResId()).thenReturn(20);

        mMapPresenter.onMarkerClick(lastClickedMarker);
        checkSelectMarker(lastClickedMarker);
    }

    @Test
    public void test_onMarkerClick_lastClickedMarkerNotNull() {
        SelectableIconModel lastClickedMarkerModel = mock(SelectableIconModel.class);
        BaseMarker lastClickedMarker = mock(BaseMarker.class);

        when(lastClickedMarker.getTag()).thenReturn(lastClickedMarkerModel);

        when(lastClickedMarkerModel.getState()).thenReturn(SelectableIconModel.NORMAL);
        when(lastClickedMarkerModel.getNormalResId()).thenReturn(10);
        when(lastClickedMarkerModel.getSelectedResId()).thenReturn(20);

        mMapPresenter.onMarkerClick(lastClickedMarker);
        when(lastClickedMarkerModel.getState()).thenReturn(SelectableIconModel.SELECTED);
        mMapPresenter.onMarkerClick(lastClickedMarker);
        checkDeselectMarker(lastClickedMarker);
    }

    @Test
    public void test_onMarkerClick_clickOnDifferentMarker() {
        SelectableIconModel lastClickedMarkerModel = mock(SelectableIconModel.class);
        SelectableIconModel clickedMarkerModel = mock(SelectableIconModel.class);
        BaseMarker lastClickedMarker = mock(BaseMarker.class);
        BaseMarker clickedMarker = mock(BaseMarker.class);

        when(lastClickedMarker.getTag()).thenReturn(lastClickedMarkerModel);
        when(clickedMarker.getTag()).thenReturn(clickedMarkerModel);

        when(lastClickedMarkerModel.getState()).thenReturn(SelectableIconModel.NORMAL);
        when(lastClickedMarkerModel.getNormalResId()).thenReturn(10);
        when(lastClickedMarkerModel.getSelectedResId()).thenReturn(20);

        when(clickedMarkerModel.getNormalResId()).thenReturn(10);
        when(clickedMarkerModel.getSelectedResId()).thenReturn(20);

        mMapPresenter.onMarkerClick(lastClickedMarker);
        mMapPresenter.onMarkerClick(clickedMarker);
        checkDeselectMarker(lastClickedMarker);
        checkSelectMarker(clickedMarker);
    }

    private void checkSelectMarker(BaseMarker baseMarker) {
        SelectableIconModel iconModel = (SelectableIconModel) baseMarker.getTag();
        verify(iconModel).changeState(SelectableIconModel.SELECTED);
        verify(baseMarker).setIcon(iconModel.getSelectedResId());
        verify(mMapModel).loadMarkerData(iconModel, baseMarker);
    }

    private void checkDeselectMarker(BaseMarker baseMarker) {
        verify(baseMarker).hideInfoWindow();
        SelectableIconModel iconModel = (SelectableIconModel) baseMarker.getTag();
        verify(iconModel).changeState(SelectableIconModel.NORMAL);
        verify(baseMarker).setIcon(iconModel.getNormalResId());
    }

    @Test
    public void test_onMapClick() {
        SelectableIconModel lastClickedMarkerModel = mock(SelectableIconModel.class);
        BaseMarker lastClickedMarker = mock(BaseMarker.class);

        when(lastClickedMarker.getTag()).thenReturn(lastClickedMarkerModel);

        when(lastClickedMarkerModel.getState()).thenReturn(SelectableIconModel.NORMAL);
        when(lastClickedMarkerModel.getNormalResId()).thenReturn(10);
        when(lastClickedMarkerModel.getSelectedResId()).thenReturn(20);

        mMapPresenter.onMarkerClick(lastClickedMarker); // Click to set last clicked marker.
        mMapPresenter.onMapClick(null);

        checkDeselectMarker(lastClickedMarker);
        reset(lastClickedMarker);
        mMapPresenter.onMapClick(null);
        verifyNoMoreInteractions(lastClickedMarker);
    }

    @Test
    public void test_onSaveInstanceState() {
        Bundle outState = mock(Bundle.class);

        mMapPresenter.onSaveInstanceState(outState);
        verify(outState).putParcelable(eq(PARCEL_CAM_LOC), any(GeoPoint.class));
    }

    @Test
    public void test_onRestoreInstanceState() {
        Bundle savedInstanceState = mock(Bundle.class);
        when(mMapModel.getIconModels()).thenReturn(new ArrayList<>());

        mMapPresenter.onRestoreInstanceState(savedInstanceState);
        verify(savedInstanceState).getParcelable(PARCEL_CAM_LOC);
    }

    @Test
    public void test_updateMarkerData() {
        SelectableIconModel iconModel = mock(SelectableIconModel.class);
        when(iconModel.getTitle()).thenReturn("title");
        when(iconModel.getDescription()).thenReturn("description");
        when(iconModel.getState()).thenReturn(SelectableIconModel.SELECTED);

        when(mMapView.getCameraZoomLevel()).thenReturn(MARKER_DISPLAY_ZOOM_LEVEL);

        BaseMarker baseMarker = mock(BaseMarker.class);

        mMapPresenter.update(Observer.UPDATE_MARKER_DATA, iconModel, baseMarker);

        verify(baseMarker).setTag(iconModel);
        verify(baseMarker).setTitle(iconModel.getTitle());
        verify(baseMarker).setSnippet(iconModel.getDescription());
        verify(baseMarker).showInfoWindow();
    }
}
