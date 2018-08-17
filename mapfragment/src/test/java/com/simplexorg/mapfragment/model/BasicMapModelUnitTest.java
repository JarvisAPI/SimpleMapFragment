package com.simplexorg.mapfragment.model;

import android.os.Bundle;
import android.os.Parcel;

import com.simplexorg.mapfragment.model.BaseMapModel.Observer;
import com.simplexorg.mapfragment.model.BaseModelDataRetriever.OnModelDetailsRetrievedListener;
import com.simplexorg.mapfragment.model.BaseModelDataRetriever.OnModelsRetrievedListener;
import com.simplexorg.mapfragment.util.MapFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static com.simplexorg.mapfragment.model.BasicMapModel.PARCEL_MODELS;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("unchecked")
public class BasicMapModelUnitTest {
    private BasicMapModel mModel;
    @Mock private BaseModelDataRetriever<SelectableIconModel> mDataRetriever;
    @Mock private Observer mObserver;
    @Mock private ArrayList<SelectableIconModel> mIconModels;
    @Mock private MapFactory mFactory;

    @Before
    public void setup() {
        initMocks(this);
        when(mFactory.createArrayList()).thenReturn((ArrayList) mIconModels);
        MapFactory.setFactory(mFactory);

        mModel = new BasicMapModel();
        mModel.setDataRetriever(mDataRetriever);
        mModel.setObserver(mObserver);
    }

    @After
    public void cleanup() {
        MapFactory.setFactory(null);
    }

    @Test
    public void test_onSaveInstanceState() {
        Bundle outState = mock(Bundle.class);
        mModel.onSaveInstanceState(outState);

        verify(outState).putParcelableArrayList(eq(PARCEL_MODELS), any(ArrayList.class));
    }

    @Test
    public void test_onRestoreInstanceState() {
        Bundle savedInstanceState = mock(Bundle.class);
        mModel.onRestoreInstanceState(savedInstanceState);

        verify(savedInstanceState).getParcelableArrayList(PARCEL_MODELS);
    }

    @Test
    public void test_loadMarkers() {
        Object parcel = mock(Parcel.class);
        GeoPoint geoPoint = mock(GeoPoint.class);
        ArgumentCaptor<OnModelsRetrievedListener> argumentCaptor = ArgumentCaptor
                .forClass(OnModelsRetrievedListener.class);

        mModel.loadMarkers(geoPoint, parcel);

        verify(mDataRetriever).getModels(argumentCaptor.capture(), eq(geoPoint));
        OnModelsRetrievedListener<SelectableIconModel> listener = argumentCaptor.getValue();

        List<SelectableIconModel> modelList = mock(List.class);
        listener.onModelsRetrieved(modelList);
        verify(mIconModels).clear();
        verify(mIconModels).addAll(modelList);

        verify(mObserver).update(Observer.UPDATE_MARKERS, null, parcel);
    }

    @Test
    public void test_loadMarkerData() {
        BaseMarkerModel baseMarkerModel = mock(BaseMarkerModel.class);
        Object parcel = mock(Object.class);

        ArgumentCaptor<OnModelDetailsRetrievedListener> argumentCaptor = ArgumentCaptor
                .forClass(OnModelDetailsRetrievedListener.class);

        mModel.loadMarkerData(baseMarkerModel, parcel);

        verify(mDataRetriever).getModelDetails(argumentCaptor.capture(), eq(baseMarkerModel));

        OnModelDetailsRetrievedListener<SelectableIconModel> listener = argumentCaptor.getValue();

        SelectableIconModel model = mock(SelectableIconModel.class);
        listener.onModelDetailsRetrieved(model);
        verify(mObserver).update(Observer.UPDATE_MARKER_DATA, model, parcel);
    }
}
