package com.simplexorg.mapfragment.marker;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.util.MapFactory;

public class DropMarkerAnimator implements BaseMarkerAnimator,
        Runnable {
    private long mStart, mDuration;
    private Interpolator mInterpolator;
    private BaseMarker mMarker;
    private Handler mHandler;
    private GeoPoint mTarget, mStartLatLng;

    public DropMarkerAnimator(long duration, BaseMarker marker,
                       Handler handler, GeoPoint target, GeoPoint startLatLng) {
        mStart = SystemClock.uptimeMillis();
        mDuration = duration;
        mMarker = marker;
        mHandler = handler;
        mInterpolator = new BounceInterpolator();
        mTarget = target;
        mStartLatLng = startLatLng;
    }

    @Override
    public void start() {
        mHandler.post(this);
    }

    @Override
    public void run() {
        long elapsed = SystemClock.uptimeMillis() - mStart;
        float t = mInterpolator.getInterpolation((float) elapsed / mDuration);
        double lng = t * mTarget.longitude + (1 - t) * mStartLatLng.longitude;
        double lat = t * mTarget.latitude + (1 - t) * mStartLatLng.latitude;
        mMarker.setPosition(MapFactory.getInstance().createGeoPoint(lat, lng));
        if (t < 1.0) {
            // Post again 10ms later.
            mHandler.postDelayed(this, 10L);
        } else {
            mMarker.setPosition(mTarget);
        }
    }
}
