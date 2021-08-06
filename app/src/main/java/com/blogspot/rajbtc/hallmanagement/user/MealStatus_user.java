package com.blogspot.rajbtc.hallmanagement.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import com.blogspot.rajbtc.hallmanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MealStatus_user extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    Switch day,night;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_status_user);
        username=getSharedPreferences("user",MODE_PRIVATE).getString("name","null");
        if(username.equals("null")){
            Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
            return;
        }
        day=(Switch)findViewById(R.id.mealStatus_userDaySw);
        night=(Switch)findViewById(R.id.mealStatus_userNightSw);
        downFire();
        upToFire();


    }

    void downFire(){
        firebaseDatabase.getReference("Users/"+username+"/Meal1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int val=dataSnapshot.getValue(Integer.class);
                day.setChecked(val==1?true:false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
             firebaseDatabase.getReference("Users/"+username+"/Meal2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int val=dataSnapshot.getValue(Integer.class);
                night.setChecked(val==1?true:false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    void upToFire(){
        day.setOnCheckedChangeListener((buttonView, isChecked) -> {
            firebaseDatabase.getReference("Users/"+username+"/Meal1").setValue(isChecked?1:0);
        });



       night.setOnCheckedChangeListener((buttonView, isChecked) -> {
            firebaseDatabase.getReference("Users/"+username+"/Meal2").setValue(isChecked?1:0);
        });

    }
}