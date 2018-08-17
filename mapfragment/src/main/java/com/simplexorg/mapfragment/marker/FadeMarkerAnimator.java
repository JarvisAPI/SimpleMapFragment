package com.simplexorg.mapfragment.marker;

import android.animation.ValueAnimator;

import com.simplexorg.mapfragment.util.MapFactory;

public class FadeMarkerAnimator implements BaseMarkerAnimator {
    private ValueAnimator mAnimator;

    public FadeMarkerAnimator(BaseMarker marker, boolean fadeIn) {
        mAnimator =
                fadeIn ?
                        MapFactory.getInstance().createValueAnimator(0, 1) :
                        MapFactory.getInstance().createValueAnimator(1, 0);
        mAnimator.addUpdateListener((ValueAnimator valueAnimator) ->
                marker.setAlpha((float) valueAnimator.getAnimatedValue())
        );
    }

    @Override
    public void start() {
        mAnimator.start();
    }
}
