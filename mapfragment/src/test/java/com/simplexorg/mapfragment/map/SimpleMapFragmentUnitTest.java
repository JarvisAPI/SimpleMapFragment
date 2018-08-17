package com.simplexorg.mapfragment.map;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.simplexorg.mapfragment.model.BaseMapModel;
import com.simplexorg.mapfragment.model.SelectableIconModel;
import com.simplexorg.mapfragment.util.MapFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SimpleMapFragmentUnitTest {
    private SimpleMapFragment mFragment;
    @Mock private MapFactory mFactory;
    @Mock private GoogleMapView mMapView;
    @Mock private BaseMapModel<SelectableIconModel> mMapModel;
    @Mock private BaseMapPresenter<SelectableIconModel> mMapPresenter;

    @Before
    public void setup() {
        initMocks(this);
        when(mFactory.createBaseMapView(any(GoogleMap.class), any(View.class))).thenReturn(mMapView);
        when(mFactory.createBaseMapModel()).thenReturn(mMapModel);
        when(mFactory.createBaseMapPresenter()).thenReturn(mMapPresenter);
        MapFactory.setFactory(mFactory);

        mFragment = new SimpleMapFragment();
    }

    @After
    public void cleanup() {
        MapFactory.setFactory(null);
    }

    @Test
    public void test_onMapReady_noCallback_andNoSavedInstanceState() {
        mFragment.onMapReady(null);

        verify(mMapPresenter).attach(mMapView, mMapModel);
    }

    @Test
    public void test_onMapReady_callbackSet_andHasSavedInstanceState() {
        Bundle savedInstanceState = mock(Bundle.class);
        BaseOnMapReadyCallback onMapReadyCallback = mock(BaseOnMapReadyCallback.class);

        mFragment.onActivityCreated(savedInstanceState);
        mFragment.setOnMapReadyCallback(onMapReadyCallback);

        mFragment.onMapReady(null);

        verify(mMapPresenter).attach(mMapView, mMapModel);
        InOrder inOrder = Mockito.inOrder(mMapModel, mMapPresenter);
        verify(mMapView).onRestoreInstanceState(savedInstanceState);
        inOrder.verify(mMapModel).onRestoreInstanceState(savedInstanceState);
        inOrder.verify(mMapPresenter).onRestoreInstanceState(savedInstanceState);
    }

    @Test
    public void test_onSaveInstanceState() {
        Bundle outState = mock(Bundle.class);

        mFragment.onMapReady(null);
        mFragment.onSaveInstanceState(outState);

        verify(mMapView).onSaveInstanceState(outState);
        verify(mMapModel).onSaveInstanceState(outState);
        verify(mMapPresenter).onSaveInstanceState(outState);
    }
}
