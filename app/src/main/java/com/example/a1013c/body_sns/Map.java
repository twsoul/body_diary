package com.example.a1013c.body_sns;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class Map extends AppCompatActivity
//        implements OnMapReadyCallback {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.map_main);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        MapFragment mapFragment = (MapFragment)fragmentManager
//                .findFragmentById(R.id.map_view);
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(final GoogleMap map) {
//
//        LatLng inter = new LatLng(37.482534, 126.970078);
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(inter);
//        markerOptions.title("오르막 길");
//        markerOptions.snippet("-인터벌 하기 좋음");
//        map.addMarker(markerOptions);
//
//        LatLng pipe = new LatLng(37.482323, 126.969627);
//
//        MarkerOptions markerOptions1 = new MarkerOptions();
//        markerOptions1.position(pipe);
//        markerOptions1.title("철봉");
//        markerOptions1.snippet("-3단 철봉");
//        map.addMarker(markerOptions1);
//
//        LatLng pipe1 = new LatLng(37.484985, 126.975120);
//
//        MarkerOptions markerOptions2 = new MarkerOptions();
//        markerOptions2.position(pipe1);
//        markerOptions2.title("철봉");
//        markerOptions2.snippet("-2단 철봉");
//        map.addMarker(markerOptions2);
//
//
//        map.moveCamera(CameraUpdateFactory.newLatLng(inter));
//        map.animateCamera(CameraUpdateFactory.zoomTo(12.7f));
//
//
//    }
//
//}
