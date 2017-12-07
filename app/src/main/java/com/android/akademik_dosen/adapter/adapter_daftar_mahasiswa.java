package com.android.akademik_dosen.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.akademik_dosen.R;
import com.android.akademik_dosen.activity.kirim_pesan;
import com.android.akademik_dosen.oop.Item;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class adapter_daftar_mahasiswa extends RecyclerView.Adapter<adapter_daftar_mahasiswa.ViewHolder> {
    private ImageLoader imageLoader;
    private Context context;
    List<Item> dftr;

    public adapter_daftar_mahasiswa(List<Item> dftr, Context context){
        super();
        //Getting all the superheroes
        this.dftr = dftr;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_mhs, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Item daftar =  dftr.get(position);
        holder.npm.setText(daftar.getNpm());
        holder.nama_mhs.setText(daftar.getNama_mhs());
        holder.item = daftar;
    }

    @Override
    public int getItemCount() {
        return dftr.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        final TextView npm,nama_mhs;
        Item item;

        public ViewHolder(View itemView) {
            super(itemView);
            npm = (TextView) itemView.findViewById(R.id.npm);
            nama_mhs = (TextView) itemView.findViewById(R.id.nama_mhs);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

}