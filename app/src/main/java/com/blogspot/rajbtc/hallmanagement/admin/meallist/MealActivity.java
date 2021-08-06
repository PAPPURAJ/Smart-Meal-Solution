package com.blogspot.rajbtc.hallmanagement.admin.meallist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.blogspot.rajbtc.hallmanagement.R;

public class MealActivity extends AppCompatActivity {

    private RecyclerView mealRecy;
    private MealAdapter mealAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        mealRecy=findViewById(R.id.mealRecy);
        mealRecy.setHasFixedSize(true);;
        mealRecy.setLayoutManager(new LinearLayoutManager(this));


    }



    void loadData(){


    }
}