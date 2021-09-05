package com.blogspot.rajbtc.hallmanagement.user.mealdetails;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.blogspot.rajbtc.hallmanagement.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import java.util.ArrayList;

public class MealDetailsUser extends AppCompatActivity {

    double debit=0,mealRate=0;
    String username;

    String name,TAG="===Main Fragment===";
    ArrayList<String> arrayList=new ArrayList<>();
    TextView totalTv;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details_user);

         username=getSharedPreferences("user",MODE_PRIVATE).getString("name","null");
        if(username.equals("null")){
            Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
            return;
        }
        name="MealList/"+username;

        totalTv=findViewById(R.id.totalTv);
        recyclerView=findViewById(R.id.recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadDebit();
        loadData();
    }




    void loadData(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MealRate");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mealRate=dataSnapshot.getValue(Double.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        arrayList.clear();
        FirebaseDatabase.getInstance().getReference(name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Log.e(TAG,snapshot.getKey());
                String date=snapshot.getKey().replace('-','/');
                if(!arrayList.contains(date)){
                    arrayList.add(date);
                }

                recyclerView.setAdapter(new RecyAdapter(MealDetailsUser.this,arrayList));

                totalTv.setText("Total Cash in: "+debit+"\nCost: "+mealRate*arrayList.size()+" TK\n"+"Available Balance: "+(debit-mealRate*arrayList.size()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e(TAG,snapshot.getKey());
                String date=snapshot.getKey().replace('-','/');
                if(!arrayList.contains(date)){
                    arrayList.add(date);
                }

                recyclerView.setAdapter(new RecyAdapter(MealDetailsUser.this,arrayList));
                totalTv.setText("Total Cash in: "+debit+"\nCost: "+mealRate*arrayList.size()+" TK\n"+"Available Balance: "+(debit-mealRate*arrayList.size()));

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


    void loadDebit(){
        FirebaseDatabase.getInstance().getReference("Users/"+username+"/Balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    debit=Double.parseDouble(dataSnapshot.getValue(String.class));
                }catch (Exception e){
                    debit=0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}