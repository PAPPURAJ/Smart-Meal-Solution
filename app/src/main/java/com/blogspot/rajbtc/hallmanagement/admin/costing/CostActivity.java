package com.blogspot.rajbtc.hallmanagement.admin.costing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.rajbtc.hallmanagement.R;
import com.blogspot.rajbtc.hallmanagement.admin.meallist.MealAdapter;
import com.blogspot.rajbtc.hallmanagement.admin.meallist.MealData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class CostActivity extends AppCompatActivity {
    private DatabaseReference fireRef;
    private ArrayList<MealData> arrayList=new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView totalTv;
    long total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);
        totalTv=findViewById(R.id.costTotalTv);
        recyclerView=findViewById(R.id.costRecy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fireRef= FirebaseDatabase.getInstance().getReference("CostList");
        loadFireData();
        findViewById(R.id.costAddFab).setOnClickListener(v -> {

            if(!getIntent().getBooleanExtra("edit",true)){
                Toast.makeText(getApplicationContext(),"You don't have add permission!",Toast.LENGTH_LONG).show();
                return;
            }

            EditText costNameEt=new EditText(this);
            costNameEt.setHint("Enter Cost name");
            costNameEt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

            EditText costEt=new EditText(this);
            costEt.setHint("Enter Cost");
            costEt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

            LinearLayout linearLayout=new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(costNameEt);
            linearLayout.addView(costEt);


            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Add cost")
                    .setView(linearLayout)
                    .setPositiveButton("Save",(dialog, which) -> {

                        String name=costNameEt.getText().toString();
                        String cost=costEt.getText().toString();



                        if(name.isEmpty() || cost.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Input first",Toast.LENGTH_LONG).show();
                            return;
                        }
                        MealData mealData=new MealData(name,cost);
                        fireRef.push().setValue(mealData);

                    })
                    .setNegativeButton("Cancel",null).show();
        });
    }




    void loadFireData(){
        total=0;
        arrayList=new ArrayList<>();
        fireRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                MealData val=snapshot.getValue(MealData.class);
                arrayList.add(val);
                Log.e("=====CostList====",val.getName()+" "+val.getCost());
                recyclerView.setAdapter(new MealAdapter(CostActivity.this,arrayList));
                total+=val.getCost();
                totalTv.setText("Total "+total+"tk");

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}