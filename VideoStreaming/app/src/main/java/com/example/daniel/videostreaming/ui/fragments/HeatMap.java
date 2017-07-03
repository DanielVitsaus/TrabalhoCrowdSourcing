package com.example.daniel.videostreaming.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daniel.videostreaming.R;
import com.example.daniel.videostreaming.VideoStreaming;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class HeatMap extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;

    private GoogleMap map;
    private SupportMapFragment mapFragment;


    public HeatMap() {
    }


    public static HeatMap newInstance() {
        HeatMap fragment = new HeatMap();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_maps, container, false);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map, mapFragment).commit();

        return v;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ((VideoStreaming)getActivity().getApplication()).setMap(mMap);

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
    }

}
