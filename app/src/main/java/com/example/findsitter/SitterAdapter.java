package com.example.findsitter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SitterAdapter extends RecyclerView.Adapter<SitterAdapter.MyViewHolder> {

    Context context;
    ArrayList<Sitter> sitter;

    public SitterAdapter(Context c, ArrayList<Sitter> p){
        context = c;
        sitter = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.sitter_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.xcleaner_id.setText(sitter.get(i).getCleaner_id());
        myViewHolder.xcleaner_name.setText(sitter.get(i).getCleaner_name());
    }

    @Override
    public int getItemCount() {
        return sitter.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView xcleaner_id, xcleaner_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            xcleaner_id = itemView.findViewById(R.id.xcleaner_id);
            xcleaner_name = itemView.findViewById(R.id.xcleaner_name);

        }
    }

}
