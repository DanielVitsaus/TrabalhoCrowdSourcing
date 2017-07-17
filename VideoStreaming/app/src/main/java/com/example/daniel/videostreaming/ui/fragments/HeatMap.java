package com.example.daniel.videostreaming.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.daniel.videostreaming.R;
import com.example.daniel.videostreaming.VideoStreaming;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class HeatMap extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap;

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private String Lista = null;


    public HeatMap() {
    }

    public static HeatMap newInstance(String listaP) {
        HeatMap fragment = new HeatMap();
        Bundle args = new Bundle();
        args.putString("lista", listaP);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Lista = getArguments().getString("lista");
        }
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

        Location lo = ((VideoStreaming)getActivity().getApplication()).getLocation();
        LatLng sydney = new LatLng(lo.getLatitude(), lo.getLongitude());
        //mMap.clear();
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Casa do Daniel"));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

        addHeatMap();
    }

    private void addHeatMap() {
        List<WeightedLatLng> list = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            if (Lista != null) {
                list = readItems(Lista);
            }
        } catch (JSONException e) {
            Toast.makeText(this.getContext(), "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }

        int[] colors = {
                Color.rgb(255, 0, 0),   // red
                Color.rgb(0, 255, 0)    // green
        };

        float[] startPoints = {
                0.1f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .weightedData(list).radius(50).gradient(gradient)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
       TileOverlay mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<WeightedLatLng> readItems(String resource) throws JSONException {
        ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
        //InputStream inputStream = getResources().openRawResource(resource);
        //String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(resource);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            double qualidade = object.getDouble("qualidade")/
            Log.d("MAPA", "Lat -> " + lat + " lon -> " + lng);
            list.add(new WeightedLatLng(new LatLng(lat, lng), qualidade));
        }
        return list;
    }

}
