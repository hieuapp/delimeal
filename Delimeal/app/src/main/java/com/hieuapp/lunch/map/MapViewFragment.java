package com.hieuapp.lunch.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.util.ImageUtils;

import java.util.List;

/**
 * Created by hieuapp on 29/04/2017.
 */

public class MapViewFragment extends MapFragment implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener, OnMapReadyCallback {

    private Rect mMapInsets = new Rect();
    private GoogleMap mMap;

    private Marker mActiveMarker = null;
    private BitmapDescriptor ICON_ACTIVE;
    private BitmapDescriptor ICON_NORMAL;

    interface MarkerCallbacks{
        void switchNextCandidate(String id);
    }

    private static MarkerCallbacks sDummyCallbacks = new MarkerCallbacks() {
        @Override
        public void switchNextCandidate(String id) {

        }
    };

    private MarkerCallbacks markerCallbacks = sDummyCallbacks;

    public static MapViewFragment newInstance(){
        return new MapViewFragment();
    }

    public static MapViewFragment newInstance(Bundle savedState){
        MapViewFragment fragment = new MapViewFragment();
        fragment.setArguments(savedState);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(!(activity instanceof MarkerCallbacks)){
            throw new ClassCastException("Activity must implement fragment's callbacks.");
        }

        markerCallbacks = (MarkerCallbacks)activity;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap iconSelected = ImageUtils.getBitmap(getContext(), R.drawable.map_marker_selected);
        Bitmap iconUnselected = ImageUtils.getBitmap(getContext(), R.drawable.map_marker_unselected);
        ICON_ACTIVE = BitmapDescriptorFactory.fromBitmap(iconSelected);
        ICON_NORMAL = BitmapDescriptorFactory.fromBitmap(iconUnselected);

        getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mapView = super.onCreateView(inflater, container, savedInstanceState);
        setMapInsets(mMapInsets);
        return mapView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        markerCallbacks = sDummyCallbacks;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.hideInfoWindow();
        String id = marker.getTitle();
        markerCallbacks.switchNextCandidate(id);
        deselectActiveMarker();
        selectActiveMarker(marker);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMyLocationEnabled(false);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(false);
        mapUiSettings.setMapToolbarEnabled(false);

        // add all markers
        generateMarker();

    }

    private void deselectActiveMarker() {
        if (mActiveMarker != null) {
            mActiveMarker.setIcon(ICON_NORMAL);
            mActiveMarker = null;
        }
    }

    private void selectActiveMarker(Marker marker) {
        if (mActiveMarker == marker) {
            return;
        }
        if (marker != null) {
            mActiveMarker = marker;
            mActiveMarker.setIcon(ICON_ACTIVE);
        }
    }

    public void setMapInsets(Rect insets) {
        mMapInsets.set(insets.left, insets.top, insets.right, insets.bottom);
        if (mMap != null) {
            mMap.setPadding(mMapInsets.left, mMapInsets.top, mMapInsets.right, mMapInsets.bottom);
        }
    }

    private void generateMarker(){
        List<MarkerOptions> markers = MarkerHelper.getInstance().getAllMarker(getContext());
        LatLng firstPos = markers.get(0).getPosition();

        MarkerOptions st1Options = markers.get(0);
        Marker st1Marker = mMap.addMarker(st1Options);
        selectActiveMarker(st1Marker);

        for(int i = 1; i < markers.size(); i++){
            mMap.addMarker(markers.get(i));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPos, 14.0f));
    }
}
