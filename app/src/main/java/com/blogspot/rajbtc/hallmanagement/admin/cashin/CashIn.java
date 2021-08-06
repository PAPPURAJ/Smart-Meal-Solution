package com.blogspot.rajbtc.hallmanagement.admin.cashin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.blogspot.rajbtc.hallmanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CashIn extends AppCompatActivity {

    FirebaseDatabase database;
    TextView julyCashTv,zainalCashTv,sojibCashTv,shanjanCashTv;
    CardView juli,zainal,sojib,shanjana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in);
        julyCashTv=findViewById(R.id.cashInJuliTv);
        zainalCashTv=findViewById(R.id.cashInZainalTv);
        sojibCashTv=findViewById(R.id.cashInSojibTv);
        shanjanCashTv=findViewById(R.id.cashInShanjanaTv);
        juli=findViewById(R.id.cashin_najrinCv);
        zainal=findViewById(R.id.cashin_zainalCv);
        sojib=findViewById(R.id.cashin_sojibCv);
        shanjana=findViewById(R.id.cashin_shanjanaCv);
        database = FirebaseDatabase.getInstance();

        loadCost("Najnin",julyCashTv);
        loadCost("Zainal",zainalCashTv);
        loadCost("Sojib",sojibCashTv);
        loadCost("Shanjana",shanjanCashTv);
        setLongClick(juli,julyCashTv,"Najnin");
        setLongClick(zainal,zainalCashTv,"Zainal");
        setLongClick(sojib,sojibCashTv,"Sojib");
        setLongClick(shanjana,shanjanCashTv,"Shanjana");



    }



    void setLongClick(CardView cardView,TextView textView,String name){
        cardView.setOnLongClickListener(v -> {
            EditText cost=new EditText(getApplicationContext());
            AlertDialog.Builder builder=new AlertDialog.Builder(CashIn.this);
            builder.setMessage("Cash in")
                    .setView(cost)
                    .setNegativeButton("Cancel",null)
                    .setPositiveButton("Ok",(dialog, which) -> {

                        int total= Integer.parseInt(cost.getText().toString())+Integer.parseInt(textView.getText().toString());

                        DatabaseReference myRef = database.getReference("Users/"+name+"/Balance");
                        myRef.setValue(total+"");
                    }).show();
            return true;
        });


    }

    void loadCost(String name,TextView textView){

        database.getReference("Users/"+name+"/Balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                textView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


    }

}