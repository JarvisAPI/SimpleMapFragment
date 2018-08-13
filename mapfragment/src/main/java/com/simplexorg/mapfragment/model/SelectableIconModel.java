package com.simplexorg.mapfragment.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.simplexorg.mapfragment.util.Factory;

public class SelectableIconModel implements Parcelable {
    private String mId;
    private GeoPoint mPos;
    private int mNormalResId;
    private int mSelectedResId;
    private int mState;
    private String mMarkerName;
    private String mMarkerDescription;

    public static final int NORMAL = 0;
    public static final int SELECTED = 1;

    public static final Creator<SelectableIconModel> CREATOR = new Creator<SelectableIconModel>() {
        @Override
        public SelectableIconModel createFromParcel(Parcel parcel) {
            return new SelectableIconModel(parcel);
        }

        @Override
        public SelectableIconModel[] newArray(int i) {
            return new SelectableIconModel[i];
        }
    };

    private static SelectableIconModel mDefaultSelectableIconModel;

    private SelectableIconModel() {

    }

    private SelectableIconModel(Parcel parcel) {
        mId = parcel.readString();
        mPos = parcel.readParcelable(GeoPoint.class.getClassLoader());
        mNormalResId = parcel.readInt();
        mSelectedResId = parcel.readInt();
        mState = parcel.readInt();
        mMarkerName = parcel.readString();
        mMarkerDescription = parcel.readString();
    }

    public static SelectableIconModel getDefault() {
        if (mDefaultSelectableIconModel == null) {
            mDefaultSelectableIconModel = new SelectableIconModel();
            mDefaultSelectableIconModel.mId = "";
            mDefaultSelectableIconModel.mPos = Factory.getInstance().createGeoPoint(Double.NaN, Double.NaN);
            mDefaultSelectableIconModel.mNormalResId = 0;
            mDefaultSelectableIconModel.mSelectedResId = 0;
            mDefaultSelectableIconModel.mState = NORMAL;
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

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeParcelable(mPos, 0);
        parcel.writeInt(mNormalResId);
        parcel.writeInt(mSelectedResId);
        parcel.writeInt(mState);
        parcel.writeString(mMarkerName);
        parcel.writeString(mMarkerDescription);
    }

    public static class Builder {
        private final SelectableIconModel mModel;

        public Builder() {
            mModel = new SelectableIconModel();
            mModel.mState = NORMAL;
        }

        public Builder id(String id) {
            mModel.mId = id;
            return this;
        }

        public Builder position(GeoPoint pos) {
            mModel.mPos = pos;
            return this;
        }

        public Builder normalResId(int normalResId) {
            mModel.mNormalResId = normalResId;
            return this;
        }

        public Builder selectedResId(int selectedResId) {
            mModel.mSelectedResId = selectedResId;
            return this;
        }

        public Builder name(String name) {
            mModel.mMarkerName = name;
            return this;
        }

        public Builder description(String description) {
            mModel.mMarkerDescription = description;
            return this;
        }

        public SelectableIconModel build() {
            return mModel;
        }
    }
}
