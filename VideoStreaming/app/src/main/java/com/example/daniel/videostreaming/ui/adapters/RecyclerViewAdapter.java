package com.example.daniel.videostreaming.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.videostreaming.R;
import com.example.daniel.videostreaming.models.Videos;

import java.util.List;

/**
 * Created by daniel on 28/06/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Videos> myVideos;

    public  RecyclerViewAdapter(List<Videos> items){
        myVideos = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Toast.makeText(parent.getContext(), viewType, Toast.LENGTH_SHORT);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_video, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nome_Video.setText(myVideos.get(position).getNome_Video());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public  final View view;
        public String url_video;
        public TextView nome_Video;

        public ViewHolder(View itemView) {
            super(itemView);
            view =  itemView;
            nome_Video =  (TextView) view.findViewById(R.id.nomeVideo_textview);
        }
    }
}
