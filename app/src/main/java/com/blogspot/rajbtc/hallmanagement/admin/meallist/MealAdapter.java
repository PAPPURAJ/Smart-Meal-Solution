package com.blogspot.rajbtc.hallmanagement.admin.meallist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.blogspot.rajbtc.hallmanagement.R;

import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHolder> {
    Context context;
    ArrayList<MealData> mealData;

    public MealAdapter(Context context, ArrayList<MealData> mealData) {
        this.context = context;
        this.mealData = mealData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_meallist,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(mealData.get(position).getName());
        holder.cost.setText(mealData.get(position).getCost()+"");

    }

    @Override
    public int getItemCount() {
        return mealData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,cost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.single_meallist_NameTv);
            cost=itemView.findViewById(R.id.single_meallist_CostTv);
        }
    }
}
