package com.blogspot.rajbtc.hallmanagement.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blogspot.rajbtc.hallmanagement.R;
import com.blogspot.rajbtc.hallmanagement.admin.costing.CostActivity;
import com.blogspot.rajbtc.hallmanagement.user.mealdetails.MealDetailsUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class UserMain extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    double balance=0,mealRate=0;
    boolean isMeal1=false,isMeal2=false;
    String username;
    private final int REQ_CODE = 100;
    TextToSpeech t1;
    ArrayList<String> mealArray=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main2);

        t1=new TextToSpeech(getApplicationContext(), status -> {
            if(status != TextToSpeech.ERROR) {
                t1.setLanguage(Locale.UK);
            }
        });

        firebaseDatabase=FirebaseDatabase.getInstance();
        username=getSharedPreferences("user",MODE_PRIVATE).getString("name","null");
        if(username.equals("null")){
            Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
            return;
        }
        loadData();

    }

    public void mealStatusClick(View view) {
        startActivity(new Intent(this, MealStatus_user.class));
    }
    public void mealDetailsClick(View view) {
        startActivity(new Intent(this, MealDetailsUser.class));
    }


    public void costClick(View view) {
        Intent intent=new Intent(this, CostActivity.class);
        intent.putExtra("edit",false);
        startActivity(intent);
    }

    public void cashInCLick(View view) {
        Intent intent=new Intent(this, CashIn_User.class);
        intent.putExtra("msg","Recharged balance: "+balance+"\nTotal cost: "+mealArray.size()*mealRate+"\nAvailable balance: "+(balance-(mealArray.size()*mealRate)));
        startActivity(intent);
    }




    void loadData(){


        firebaseDatabase.getReference("MealRate").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               mealRate=dataSnapshot.getValue(Double.class);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });



        firebaseDatabase.getReference("Users/"+username+"/Meal1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int val=dataSnapshot.getValue(Integer.class);
                isMeal1=val==1?true:false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        firebaseDatabase.getReference("Users/"+username+"/Meal2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int val=dataSnapshot.getValue(Integer.class);
                isMeal2=val==1?true:false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mealArray=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("MealList/"+username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String s=snapshot.getKey();
                if(!mealArray.contains(s)){
                    mealArray.add(s);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String s=snapshot.getKey();
                if(!mealArray.contains(s)){
                    mealArray.add(s);
                }
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


        FirebaseDatabase.getInstance().getReference("Users/"+username+"/Balance").addValueEventListener(new ValueEventListener() {
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


    }











    public void voiceClick(View view) {
        loadData();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Need to speak");
        try {
            startActivityForResult(intent, REQ_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry your device not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String text = result.get(0).toLowerCase();

                    if(text.contains("rate")){
                        t1.speak("The meal rate is "+mealRate+" taka", TextToSpeech.QUEUE_FLUSH, null);
                        Log.e("===========",text);
                    }
                    else if(text.contains("total") && text.contains("cost")){
                        t1.speak("The total cost is "+mealArray.size()*mealRate+" taka", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else if(text.contains("total")){
                        t1.speak("The total meal is "+mealArray.size(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else if(text.contains("lauch") ){
                        t1.speak((isMeal1?"Yes!":"No!")+" Your first meal is "+(isMeal1?"On!":"Off!"), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else if(text.contains("dinner") ){
                        t1.speak((isMeal2?"Yes!":"No!")+" Your second meal is "+(isMeal2?"On!":"Off!"), TextToSpeech.QUEUE_FLUSH, null);
                    }


                    else if(text.contains("balance")){
                        t1.speak("Your current balance is "+(balance-(mealRate*mealArray.size()))+" taka", TextToSpeech.QUEUE_FLUSH, null);
                    }else{
                        t1.speak("Sorry!, I don't understand!", TextToSpeech.QUEUE_FLUSH, null);
                    }



                }
                break;
            }
        }
    }
}