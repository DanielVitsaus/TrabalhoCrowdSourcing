package com.example.daniel.videostreaming.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daniel.videostreaming.R;


public class HeatMap extends Fragment {


    public HeatMap() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heat_map, container, false);
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

}
