package com.simplexorg.mapfragment.marker;


import com.simplexorg.mapfragment.model.GeoPoint;

public class BaseMarkerOptions {
    private float mAlpha;
    private float mAnchorU;
    private float mAnchorV;
    private boolean mDraggable;
    private boolean mFlat;
    private int mResId;
    private float mWindowAnchorU;
    private float mWindowAnchorV;
    private GeoPoint mGeoPoint;
    private float mRotation;
    private String mSnippet;
    private String mTitle;
    private boolean mVisible;
    private float mZIndex;

    public BaseMarkerOptions() {
        mAlpha = 1;
        mVisible = true;
    }

    public BaseMarkerOptions alpha(float alpha) {
        mAlpha = alpha;
        return this;
    }

    public float getAlpha() {
        return mAlpha;
    }

    public BaseMarkerOptions anchor(float u, float v) {
        mAnchorU = u;
        mAnchorV = v;
        return this;
    }

    public float getAnchorU() {
        return mAnchorU;
    }

    public float getAnchorV() {
        return mAnchorV;
    }

    public BaseMarkerOptions draggable(boolean draggable) {
        mDraggable = draggable;
        return this;
    }

    public boolean isDraggable() {
        return mDraggable;
    }

    public BaseMarkerOptions flat(boolean flat) {
        mFlat = flat;
        return this;
    }

    public boolean isFlat() {
        return mFlat;
    }

    public BaseMarkerOptions icon(int resId) {
        mResId = resId;
        return this;
    }

    public int getIcon() {
        return mResId;
    }

    public BaseMarkerOptions infoWindowAnchor(float u, float v) {
        mWindowAnchorU = u;
        mWindowAnchorV = v;
        return this;
    }

    public float getInfoWindowAnchorU() {
        return mWindowAnchorU;
    }

    public float getInfoWindowAnchorV() {
        return mWindowAnchorV;
    }

    public BaseMarkerOptions position(GeoPoint geoPoint) {
        mGeoPoint = geoPoint;
        return this;
    }

    public GeoPoint getPosition() {
        return mGeoPoint;
    }

    public BaseMarkerOptions rotation(float rotation) {
        mRotation = rotation;
        return this;
    }

    public float getRotation() {
        return mRotation;
    }

    public BaseMarkerOptions snippet(String snippet) {
        mSnippet = snippet;
        return this;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public BaseMarkerOptions title(String title) {
        mTitle = title;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public BaseMarkerOptions visible(boolean visible) {
        mVisible = visible;
        return this;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public BaseMarkerOptions zIndex(float zIndex) {
        mZIndex = zIndex;
        return this;
    }

    public float getZIndex() {
        return mZIndex;
    }
}
