package com.blogspot.rajbtc.hallmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {
    private ProgressBar progressBar;
    private Handler handler;
    private Runnable runnable;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar=findViewById(R.id.loginProg);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        handler=new Handler();
        runnable= () -> {
            progressBar.setProgress(i);
            if(i++<100)
                handler.postDelayed(runnable,10);
            else{
                finish();

                    finish();
                    startActivity(new Intent(SplashScreen.this,LoginActivity.class));


            }


        };handler.post(runnable);
    }



}