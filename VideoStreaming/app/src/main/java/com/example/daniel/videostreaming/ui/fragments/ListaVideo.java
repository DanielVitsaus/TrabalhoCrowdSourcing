package com.example.daniel.videostreaming.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daniel.videostreaming.R;
import com.example.daniel.videostreaming.models.Videos;
import com.example.daniel.videostreaming.ui.adapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class ListaVideo extends Fragment {

    private List<Videos> videos;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter myAdapter;

    public ListaVideo() {
        videos = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            Videos v = new Videos("URURURURURUR", "Semirario em Computação VI");
            videos.add(v);
        }
    }


    public static ListaVideo newInstance() {
        ListaVideo fragment = new ListaVideo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lista_video, container, false);

        myAdapter = new RecyclerViewAdapter(videos, this.getContext());

        recyclerView =  (RecyclerView) view.findViewById(R.id.lista_video);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
