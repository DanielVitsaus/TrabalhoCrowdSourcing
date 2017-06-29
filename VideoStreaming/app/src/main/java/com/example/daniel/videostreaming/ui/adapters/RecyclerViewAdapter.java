package com.example.daniel.videostreaming.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daniel.videostreaming.R;
import com.example.daniel.videostreaming.models.Videos;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Videos> myVideos;

    public  RecyclerViewAdapter(List<Videos> items){
        myVideos = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_video, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nome_Video.setText(myVideos.get(position).getNome_Video());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return myVideos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public  final View view;
        public TextView nome_Video;

        public ViewHolder(View itemView) {
            super(itemView);
            view =  itemView;
            nome_Video =  (TextView) view.findViewById(R.id.nomeVideo_textview);
        }
    }
}
