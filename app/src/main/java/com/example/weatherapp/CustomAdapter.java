package com.example.weatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.RecyclerViewHolder> {

    Context parentContext;
    ArrayList<String> arrayList;

    public CustomAdapter(Context context, ArrayList<String> list){
        parentContext = context;
        arrayList = list;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("TAG_1", "onCreateViewHolder");
        View view = LayoutInflater.from(parentContext).inflate(R.layout.adapter_recycler, parent, false);
        RecyclerViewHolder rvHolder= new RecyclerViewHolder(view);

        return rvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Log.d("TAG_2", "onBindViewHolder");
        String s= arrayList.get(holder.getAdapterPosition());

        holder.tV.setText(s);
        if (s.contains("few clouds")){
            holder.iV.setImageResource(R.drawable.fewclouds);
        }
        else if (s.contains("scattered clouds")){
            holder.iV.setImageResource(R.drawable.clouds);
        }
        else if (s.contains("clear sky")){
            holder.iV.setImageResource(R.drawable.clearsky);
        }
        else if (s.contains("broken")){
            holder.iV.setImageResource(R.drawable.broken);
        }
        else if (s.contains("rain")){
            holder.iV.setImageResource(R.drawable.rain);
        }
        else if (s.contains("snow")){
            holder.iV.setImageResource(R.drawable.snow);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView tV;
        ImageView iV;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("TAG_3", "RecyclerViewHolder-Constructor");

            tV = itemView.findViewById(R.id.id_adapter_textview);
            iV = itemView.findViewById(R.id.adapter_image);

        }
    }
}
