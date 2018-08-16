package com.simplexorg.mapfragment.model;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GeoPointUnitTest {
    private GeoPoint mGeoPoint;
    private double mLat = 10;
    private double mLon = 11;

    @Before
    public void setup() {
        mGeoPoint = new GeoPoint(mLat, mLon);
    }

    @Test
    public void test_writeToParcel() {
        Parcel parcel = mock(Parcel.class);
        mGeoPoint.writeToParcel(parcel, 0);

        InOrder inOrder = Mockito.inOrder(parcel);
        inOrder.verify(parcel).writeDouble(mLat);
        inOrder.verify(parcel).writeDouble(mLon);
    }

    @Test
    public void test_parcel_creation() {
        Parcel parcel = mock(Parcel.class);
        mGeoPoint = GeoPoint.CREATOR.createFromParcel(parcel);

        verify(parcel, times(2)).readDouble();
    }
}
