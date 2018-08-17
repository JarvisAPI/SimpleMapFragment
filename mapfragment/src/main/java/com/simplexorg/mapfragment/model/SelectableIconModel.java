package com.simplexorg.mapfragment.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.simplexorg.mapfragment.util.MapFactory;

public class SelectableIconModel implements Parcelable, BaseMarkerModel {
    private String mId;
    private GeoPoint mPos;
    private int mNormalResId;
    private int mSelectedResId;
    private int mState;
    private String mMarkerTitle;
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
        mMarkerTitle = parcel.readString();
        mMarkerDescription = parcel.readString();
    }

    public static SelectableIconModel getDefault() {
        if (mDefaultSelectableIconModel == null) {
            mDefaultSelectableIconModel = new SelectableIconModel();
            mDefaultSelectableIconModel.mId = "";
            mDefaultSelectableIconModel.mPos = MapFactory.getInstance().createGeoPoint(Double.NaN, Double.NaN);
            mDefaultSelectableIconModel.mNormalResId = 0;
            mDefaultSelectableIconModel.mSelectedResId = 0;
            mDefaultSelectableIconModel.mState = NORMAL;
        }
        return mDefaultSelectableIconModel;
    }

    @NonNull
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

    public String getTitle() {
        return mMarkerTitle;
    }

    public String getDescription() {
        return mMarkerDescription;
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
        parcel.writeString(mMarkerTitle);
        parcel.writeString(mMarkerDescription);
    }

    public static class Builder {
        private final SelectableIconModel mModel;

        public Builder() {
            mModel = new SelectableIconModel();
            mModel.mState = NORMAL;
        }

        /**
         * Constructs a builder to modify the given model. Does not
         * construct a new model but uses the existing one.
         * @param model the model to modify.
         */
        public Builder(SelectableIconModel model) {
            mModel = model;
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

        public Builder title(String title) {
            mModel.mMarkerTitle = title;
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
