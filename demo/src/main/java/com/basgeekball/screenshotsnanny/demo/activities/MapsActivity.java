package com.basgeekball.screenshotsnanny.demo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.basgeekball.screenshotsnanny.demo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final double mBerlinLat = 52.52;
    private final double mBerlinLng = 13.40;
    private final float mBerlinZoomLevel = 10;

    public static Intent createIntent(Activity activity) {
        return new Intent(activity, MapsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng berlin = new LatLng(mBerlinLat, mBerlinLng);
        googleMap.addMarker(new MarkerOptions().position(berlin).title(getString(R.string.map_marker_title)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(berlin));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(mBerlinZoomLevel));
    }
}
