package com.simplexorg.mapfragment.marker;

import com.simplexorg.mapfragment.model.GeoPoint;

public abstract class BaseMarker {
    private Object mTag;

    public abstract GeoPoint getPosition();

    public abstract void setAlpha(float alpha);

    public abstract float getAlpha();

    public abstract void remove();

    public abstract void setPosition(GeoPoint geoPoint);

    public abstract void setTitle(String title);

    public abstract void setSnippet(String snippet);

    public abstract void showInfoWindow();

    public abstract void hideInfoWindow();

    public abstract void setIcon(int resId);

    public void setTag(Object obj) {
        mTag = obj;
    }

    public Object getTag() {
        return mTag;
    }
}
