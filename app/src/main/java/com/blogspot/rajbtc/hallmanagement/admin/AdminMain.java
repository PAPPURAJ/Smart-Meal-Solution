package com.blogspot.rajbtc.hallmanagement.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.blogspot.rajbtc.hallmanagement.R;
import com.blogspot.rajbtc.hallmanagement.admin.cashin.CashIn;
import com.blogspot.rajbtc.hallmanagement.admin.costing.CostActivity;
import com.blogspot.rajbtc.hallmanagement.admin.mealdetails.MealDetails;
import com.google.firebase.database.FirebaseDatabase;

public class AdminMain extends Activity {

    Double mealRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


    }

    public void costClick(View view){
        startActivity(new Intent(this, CostActivity.class));
    }

    public void cashInCLick(View view) {startActivity(new Intent(this, CashIn.class));
    }

    public void mealStatusClick(View view) {startActivity(new Intent(this, MealStatus.class));
    }

    public void mealDetailsClick(View view) {startActivity(new Intent(this, MealDetails.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        EditText mealrateEt=new EditText(this);
        mealrateEt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(mealrateEt);


        if(item.getItemId()==R.id.mealRate){

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Set meal rate")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        try{
                             mealRate=Double.parseDouble(mealrateEt.getText().toString());
                            FirebaseDatabase.getInstance().getReference("MealRate").setValue(mealRate);
                            Toast.makeText(getApplicationContext(),"Meal set!",Toast.LENGTH_LONG).show();

                        }
                        catch (Exception e){
                            mealRate=0.0;
                            Toast.makeText(getApplicationContext(),"Please set again!",Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton("No",null).create().show();;

        }

        return true;
    }
}