package com.blogspot.rajbtc.hallmanagement.user.mealdetails;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.rajbtc.hallmanagement.R;

import java.util.ArrayList;

public class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyHolder> {
    private Context context;
    private ArrayList<String> arrayList;
    private double total=0;

    public RecyAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;


    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.single_meal_details,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.serialTv.setText((position+1)+".");
        holder.dateTv.setText(arrayList.get(position));
        holder.presentTv.setText("Present");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView serialTv,dateTv,presentTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            serialTv=itemView.findViewById(R.id.single_SerialTv);
            dateTv=itemView.findViewById(R.id.single_DateTv);
            presentTv=itemView.findViewById(R.id.single_PresentTv);
        }
    }
}