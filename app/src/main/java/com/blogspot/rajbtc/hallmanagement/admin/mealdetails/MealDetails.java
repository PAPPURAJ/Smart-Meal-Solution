package com.blogspot.rajbtc.hallmanagement.admin.mealdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.rajbtc.hallmanagement.R;
import com.google.android.material.tabs.TabLayout;


public class MealDetails extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        viewPager=findViewById(R.id.myviewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout=findViewById(R.id.tabLay);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        EditText editText=new EditText(getApplicationContext());
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint("Type meal rate");

        if(item.getItemId()==R.id.mealRate){


            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Meal rate")
                    .setView(editText)
                    .setPositiveButton("Ok",(dialog, which) -> {
                        String rate=editText.getText().toString();
                        if(rate.equals("")){
                            Toast.makeText(getApplicationContext(), "Please input first", Toast.LENGTH_SHORT).show();
                        }else{
                            getSharedPreferences("MySp",MODE_PRIVATE).edit().putString("rate",rate).apply();
                            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                        }

                    }).setNegativeButton("Cancel",null)
                    .create().show();
        }

        return true;
    }
}