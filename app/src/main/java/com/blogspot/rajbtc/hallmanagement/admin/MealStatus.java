package com.blogspot.rajbtc.hallmanagement.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import com.blogspot.rajbtc.hallmanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MealStatus extends AppCompatActivity {
    FirebaseDatabase database;
    int[] value1;
    int[] value2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_status);
        value1=new int[4];
        value2=new int[4];

        database = FirebaseDatabase.getInstance();
        loadCost("Najnin",findViewById(R.id.mealStatusNajrinTv),0);
        loadCost("Zainal",findViewById(R.id.mealStatusZainalTv),1);
        loadCost("Sojib",findViewById(R.id.mealStatusSojibTv),2);
        loadCost("Shanjana",findViewById(R.id.mealStatusShanjanaTv),3);
    }



    String numToOffOn(int value){
        return value==0?"Off":"On";
    }

    void loadCost(String name,TextView textView,int index){

        database.getReference("Users/"+name+"/Meal1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value1[index]=dataSnapshot.getValue(Integer.class);

                textView.setText("D: "+ numToOffOn(value1[index])+"\nN: "+numToOffOn(value2[index]));

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        database.getReference("Users/"+name+"/Meal2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
                value2[index]=dataSnapshot2.getValue(Integer.class);
                textView.setText("D: "+ numToOffOn(value1[index])+"\nN: "+numToOffOn(value2[index]));
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });



    }
}