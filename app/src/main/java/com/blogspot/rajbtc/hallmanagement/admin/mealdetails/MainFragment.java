package com.blogspot.rajbtc.hallmanagement.admin.mealdetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blogspot.rajbtc.hallmanagement.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    String name,TAG="===Main Fragment===";
    View view;
    ArrayList<String> arrayList=new ArrayList<>();
    TextView totalTv;
    double mealRate=0,balance=0;
    RecyclerView recyclerView;
    public MainFragment(String name) {
        this.name="MealList/"+name;
        FirebaseDatabase.getInstance().getReference("Users/"+name+"/Balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    balance=Double.parseDouble(dataSnapshot.getValue(String.class));
                }catch (Exception e){
                    balance=0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        FirebaseDatabase.getInstance().getReference("MealRate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mealRate=dataSnapshot.getValue(Double.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_main, container, false);
        totalTv=view.findViewById(R.id.totalTv);
        recyclerView=view.findViewById(R.id.recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadData();
        return view;
    }



    void loadData(){









        arrayList.clear();
        FirebaseDatabase.getInstance().getReference(name).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e(TAG,snapshot.getKey());
                String date=snapshot.getKey().replace('-','/');
                if(!arrayList.contains(date))
                    arrayList.add(date);
                recyclerView.setAdapter(new RecyAdapter(getContext(),arrayList));
                totalTv.setText("Balance: "+balance+" Tk\nCost: "+mealRate*arrayList.size()+" Tk\nAvailable balance: "+(balance-(mealRate*arrayList.size()))+" Tk");


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e(TAG,snapshot.getValue().toString());
                String date=snapshot.getKey().replace('-','/');
                if(!arrayList.contains(date))
                    arrayList.add(date);
                recyclerView.setAdapter(new RecyAdapter(getContext(),arrayList));
                totalTv.setText("Balance: "+balance+" Tk\nCost: "+mealRate*arrayList.size()+" Tk\nAvailable balance: "+(balance-(mealRate*arrayList.size()))+" Tk");
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