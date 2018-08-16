package com.simplexorg.mapfragment.model;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static com.simplexorg.mapfragment.model.SelectableIconModel.NORMAL;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SelectableIconModelUnitTest {
    private SelectableIconModel mModel;
    private final String mId = "id";
    private final GeoPoint mPos = new GeoPoint(0, 1);
    private final int mNormalResId = 100;
    private final int mSelectedResId = 200;
    private final int mState = NORMAL;
    private final String mMarkerTitle = "title";
    private final String mMarkerDescription = "description";

    @Before
    public void setup() {
        mModel = new SelectableIconModel.Builder()
                .id(mId)
                .position(mPos)
                .normalResId(mNormalResId)
                .selectedResId(mSelectedResId)
                .title(mMarkerTitle)
                .description(mMarkerDescription)
                .build();
    }

    @Test
    public void test_writeToParcel() {
        Parcel parcel = mock(Parcel.class);
        mModel.writeToParcel(parcel, 0);

        verify(parcel).writeString(mId);
        verify(parcel).writeParcelable(mPos, 0);
        verify(parcel).writeInt(mNormalResId);
        verify(parcel).writeInt(mSelectedResId);
        verify(parcel).writeInt(mState);
        verify(parcel).writeString(mMarkerTitle);
        verify(parcel).writeString(mMarkerDescription);
    }

    @Test
    public void test_parcel_creation() {
        Parcel parcel = mock(Parcel.class);
        mModel = SelectableIconModel.CREATOR.createFromParcel(parcel);

        InOrder inOrder = Mockito.inOrder(parcel);

        inOrder.verify(parcel).readString();
        inOrder.verify(parcel).readParcelable(GeoPoint.class.getClassLoader());
        inOrder.verify(parcel, times(3)).readInt();
        inOrder.verify(parcel, times(2)).readString();
    }
}
