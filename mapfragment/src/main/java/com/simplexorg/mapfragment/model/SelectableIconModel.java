package com.simplexorg.mapfragment.model;


import com.simplexorg.mapfragment.util.Factory;

public class SelectableIconModel {
    private String mId;
    private GeoPoint mPos;
    private int mNormalResId;
    private int mSelectedResId;

    public static final int NORMAL = 0;
    public static final int SELECTED = 1;

    private static SelectableIconModel mDefaultSelectableIconModel;

    private int mState;

    public SelectableIconModel(String id, GeoPoint geoPoint,
                               int normalResId, int selectedResId) {
        mId = id;
        mPos = geoPoint;
        mNormalResId = normalResId;
        mSelectedResId = selectedResId;
        mState = NORMAL;
    }

    public static SelectableIconModel getDefault() {
        if (mDefaultSelectableIconModel == null) {
            mDefaultSelectableIconModel = new SelectableIconModel("",
                    Factory.getInstance().createGeoPoint(Double.NaN, Double.NaN),
                    0, 0);
        }
        return mDefaultSelectableIconModel;
    }

    public String getId() {
        return mId;
    }

    public GeoPoint getPos() {
        return mPos;
    }

    public int getNormalResId() {
        return mNormalResId;
    }

    public int getSelectedResId() {
        return mSelectedResId;
    }

    public int getState() {
        return mState;
    }

    public void changeState(int state) {
        mState = state;
    }
}
