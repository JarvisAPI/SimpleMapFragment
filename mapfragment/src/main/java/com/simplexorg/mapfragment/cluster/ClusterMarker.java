package com.simplexorg.mapfragment.cluster;

import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.model.GeoPoint;

import java.util.ArrayList;
import java.util.List;


public class ClusterMarker extends BaseMarker {
    private BaseMarker mBaseMarker;
    private List<BaseMarker> mClusterMarkers;

    ClusterMarker(BaseMarker baseMarker) {
        mClusterMarkers = new ArrayList<>();
        mBaseMarker = baseMarker;
    }

    @Override
    public GeoPoint getPosition() {
        return mBaseMarker.getPosition();
    }

    @Override
    public void setAlpha(float alpha) {
        mBaseMarker.setAlpha(alpha);
    }

    @Override
    public float getAlpha() {
        return mBaseMarker.getAlpha();
    }

    @Override
    public void remove() {
        mBaseMarker.remove();
    }

    @Override
    public void setPosition(GeoPoint geoPoint) {
        mBaseMarker.setPosition(geoPoint);
    }

    @Override
    public void setTitle(String title) {
        mBaseMarker.setTitle(title);
    }

    @Override
    public void setSnippet(String snippet) {
        mBaseMarker.setSnippet(snippet);
    }

    @Override
    public void showInfoWindow() {
        mBaseMarker.showInfoWindow();
    }

    @Override
    public void hideInfoWindow() {
        mBaseMarker.hideMarker();
    }

    @Override
    public void setIcon(int resId) {
        mBaseMarker.setIcon(resId);
    }

    @Override
    public void hideMarker() {
        mBaseMarker.hideMarker();
    }

    @Override
    public void displayMarker() {
        mBaseMarker.displayMarker();
    }

    int clusterSize() {
        return mClusterMarkers.size();
    }

    List<BaseMarker> getClusterMarkers() {
        return mClusterMarkers;
    }

    void addClusterMarkers(List<BaseMarker> clusterMarkers) {
        mClusterMarkers.addAll(clusterMarkers);
    }

    void addClusterMarker(BaseMarker baseMarker) {
        mClusterMarkers.add(baseMarker);
    }
}
