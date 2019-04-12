package com.example.findsitter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SitterAdapter extends RecyclerView.Adapter<SitterAdapter.MyViewHolder> {

    Context context;
    Object[] sitters;
    HashMap<String, Sitter> sitter;

    public SitterAdapter(Context c, HashMap<String, Sitter> p, Object[] s){
        context = c;
        sitter = p;
        sitters = s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.sitter_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        Sitter data = (Sitter) sitters[i];

        myViewHolder.xcleaner_name.setText(data.getCleaner_name());
        myViewHolder.xcleaner_id.setText(data.getCleaner_id());

        final String getCleanerId = data.getCleaner_id();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "item " + getCleanerId + " pos: " + i, Toast.LENGTH_SHORT).show();
//                Intent axd = new Intent(context, ProfileAct.class );
//                axd.putExtra("cleaner_id", getCleanerId);
//                context.startActivity(axd);
//                ((MainActivity) context).finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return sitter.size();
    }

    public void update(HashMap<String, Sitter> list, Object[] values) {
        this.sitters = values;
        this.sitter = list;
        this.notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView xcleaner_name, xcleaner_id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            xcleaner_name = itemView.findViewById(R.id.xcleaner_name);
            xcleaner_id = itemView.findViewById(R.id.xcleaner_id);


        }
    }

}
