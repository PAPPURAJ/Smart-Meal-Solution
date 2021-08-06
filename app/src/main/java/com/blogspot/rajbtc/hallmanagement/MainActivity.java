package com.blogspot.rajbtc.hallmanagement;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final int REQ_CODE = 100;
    TextToSpeech t1;
    String lat="null",lon="null",ch4="null",co="null",co2="null",temp="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView speak = findViewById(R.id.iv_mic);


        getdata();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });



        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    ((TextView) findViewById(R.id.tv_speech_to_text)).setText(result.get(0) + "");
                    String text = result.get(0).toLowerCase();

                    if(text.contains("location")){
                        t1.speak("The latitude of the circuit location is "+lat+" and the longitude is "+lon, TextToSpeech.QUEUE_FLUSH, null);
                    }else if(text.contains("what") && text.contains("temperature")){
                        t1.speak("The temperature of the location is "+temp+"degree celsius", TextToSpeech.QUEUE_FLUSH, null);
                    }else if(text.contains("carbon") && text.contains("dioxide")){
                        t1.speak("The percentage of carbon dioxide is "+co2+" percent", TextToSpeech.QUEUE_FLUSH, null);
                    }else if(text.contains("carbon") && text.contains("monoxide")){
                        t1.speak("The percentage of carbon monoxide is "+co+" percent", TextToSpeech.QUEUE_FLUSH, null);
                    }else if(text.contains("methane") && text.contains("percentage")){
                        t1.speak("The percentage of methane is "+ch4+" percent", TextToSpeech.QUEUE_FLUSH, null);
                    }else{
                        t1.speak("Sorry sir!, I don't know this!", TextToSpeech.QUEUE_FLUSH, null);
                    }



                }
                break;
            }
        }
    }




    void getdata(){
        FirebaseDatabase.getInstance().getReference("Location").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().toLowerCase().contains("lat-1")){
                    lat=dataSnapshot.getValue().toString();

                }else if(dataSnapshot.getKey().toLowerCase().contains("lon-1")){
                    lon=dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().toLowerCase().contains("lat-1")){
                    lat=dataSnapshot.getValue().toString();

                }else if(dataSnapshot.getKey().toLowerCase().contains("lon-1")){
                    lon=dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference("Data").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().toLowerCase().contains("ch4-1")){
                    ch4=dataSnapshot.getValue().toString();

                }else if(dataSnapshot.getKey().toLowerCase().contains("co-1")){
                    co=dataSnapshot.getValue().toString();
                }
                else if(dataSnapshot.getKey().toLowerCase().contains("co2-1")){
                    co2=dataSnapshot.getValue().toString();
                }
                else if(dataSnapshot.getKey().toLowerCase().contains("temp-1")){
                    temp=dataSnapshot.getValue().toString();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().toLowerCase().contains("ch4-1")){
                    ch4=dataSnapshot.getValue().toString();

                }else if(dataSnapshot.getKey().toLowerCase().contains("co-1")){
                    co=dataSnapshot.getValue().toString();
                }
                else if(dataSnapshot.getKey().toLowerCase().contains("co2-1")){
                    co2=dataSnapshot.getValue().toString();
                }
                else if(dataSnapshot.getKey().toLowerCase().contains("temp-1")){
                    temp=dataSnapshot.getValue().toString();
                }



            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}