package com.simplexorg.mapfragment.cluster;

import android.content.Context;
import android.graphics.Point;

import com.simplexorg.mapfragment.map.BaseMapView;
import com.simplexorg.mapfragment.marker.BaseMarker;
import com.simplexorg.mapfragment.marker.BaseMarkerOptions;
import com.simplexorg.mapfragment.model.GeoPoint;
import com.simplexorg.mapfragment.model.SelectableIconModel.Builder;
import com.simplexorg.mapfragment.util.Factory;

import java.util.ArrayList;
import java.util.List;

class ClusterAlgorithm {
    private IconMaker mIconMaker;
    private BaseMapView mMapView;
    private static final int CLUSTER_SQUARE_DIST_THRESH = 2000; // in pixels squared

    ClusterAlgorithm(Context context) {
        mIconMaker = Factory.getInstance().createIconMaker(context);
    }

    void cluster(List<BaseMarker> currentMarkers) {
        List<BaseMarker> onScreenMarkers = getOnScreenMarkers(currentMarkers);
        List<BaseMarker> markersToHide = new ArrayList<>();
        List<BaseMarker> markersToDisplay = new ArrayList<>();

        while (onScreenMarkers.size() > 1) {
            ClusterMarker clusterMarker = formClusterMarker(onScreenMarkers);
            if (clusterMarker != null) {
                currentMarkers.add(clusterMarker);
                currentMarkers.removeAll(clusterMarker.getClusterMarkers());
                markersToDisplay.add(clusterMarker);
                markersToHide.addAll(clusterMarker.getClusterMarkers());
            }
        }

        for (BaseMarker baseMarker : markersToHide) {
            baseMarker.hideMarker();
        }

        for (BaseMarker baseMarker : markersToDisplay) {
            baseMarker.displayMarker();
        }
    }

    private ClusterMarker formClusterMarker(List<BaseMarker> onScreenMarkers) {
        List<BaseMarker> clusterMarkers = new ArrayList<>();
        BaseMarker baseMarker = onScreenMarkers.get(0);
        for (int i = 1; i < onScreenMarkers.size(); i++) {
            BaseMarker currentBaseMarker = onScreenMarkers.get(i);
            if (shouldCluster(baseMarker, currentBaseMarker)) {
                clusterMarkers.add(currentBaseMarker);
            }
        }
        if (clusterMarkers.isEmpty()) {
            onScreenMarkers.remove(baseMarker);
            return null;
        } else {
            clusterMarkers.add(baseMarker);
            onScreenMarkers.removeAll(clusterMarkers);
            return makeClusterMarker(clusterMarkers);
        }
    }

    private ClusterMarker makeClusterMarker(List<BaseMarker> clusterMarkers) {
        GeoPoint midPoint = getClusterMidPoint(clusterMarkers);
        ClusterMarker clusterMarker = new ClusterMarker(
                mMapView.addMarker(new BaseMarkerOptions()
                        .iconBitmap(mIconMaker.makeIcon(String.valueOf(clusterMarkers.size())))
                        .position(midPoint))
        );
        clusterMarker.addClusterMarkers(clusterMarkers);
        clusterMarker.setTag(new Builder()
                .id(String.valueOf(clusterMarker.hashCode()))
                .build());
        return clusterMarker;
    }

    private GeoPoint getClusterMidPoint(List<BaseMarker> clusterMarkers) {
        int mid_x = 0;
        int mid_y = 0;
        for (BaseMarker baseMarker : clusterMarkers) {
            Point point = mMapView.projectToScreenLocation(baseMarker.getPosition());
            mid_x += point.x;
            mid_y += point.y;
        }
        mid_x /= clusterMarkers.size();
        mid_y /= clusterMarkers.size();
        return mMapView.projectFromScreenLocation(new Point(mid_x, mid_y));
    }

    private boolean shouldCluster(BaseMarker baseMarker, BaseMarker otherBaseMarker) {
        Point point0 = mMapView.projectToScreenLocation(baseMarker.getPosition());
        Point point1 = mMapView.projectToScreenLocation(otherBaseMarker.getPosition());

        int x_diff = point0.x - point1.x;
        int y_diff = point0.y - point1.y;

        return x_diff * x_diff + y_diff * y_diff < CLUSTER_SQUARE_DIST_THRESH;
    }

    private List<BaseMarker> getOnScreenMarkers(List<BaseMarker> currentMarkers) {
        List<BaseMarker> onScreenMarkers = new ArrayList<>();
        for (BaseMarker baseMarker : currentMarkers) {
            Point screenPoint = mMapView.projectToScreenLocation(baseMarker.getPosition());
            if (screenPoint.x > 0 && screenPoint.y > 0) {
                onScreenMarkers.add(baseMarker);
            }
        }
        return onScreenMarkers;
    }

    void setMapView(BaseMapView baseMapView) {
        mMapView = baseMapView;
    }
}
