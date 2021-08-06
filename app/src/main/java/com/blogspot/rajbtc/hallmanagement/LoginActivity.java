package com.blogspot.rajbtc.hallmanagement;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.blogspot.rajbtc.hallmanagement.admin.AdminMain;
import com.blogspot.rajbtc.hallmanagement.user.UserMain;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends Activity {

    private EditText emailEt,passEt;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEt=findViewById(R.id.mailEt);
        passEt=findViewById(R.id.passEt);
       // startActivity(new Intent(getApplicationContext(), AdminMain.class));
        startActivity(new Intent(getApplicationContext(), UserMain.class));
        sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
    }








    public void loginClick(View view){

        String email=emailEt.getText().toString().toLowerCase();
        String pass=passEt.getText().toString();

        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please input",Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(authResult -> {
            Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_SHORT).show();
            if(email.contains("admin"))
                startActivity(new Intent(getApplicationContext(), AdminMain.class));
            else{
                startActivity(new Intent(getApplicationContext(), UserMain.class));
                String name;
                if(email.contains("najnin"))
                    name="Najnin";
                else if(email.contains("zainal"))
                    name="Zainal";
                else if(email.contains("sojib"))
                    name="Sojib";
                else name="Shanjana";

                sharedPreferences.edit().putString("name",name).apply();
            }



        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_SHORT).show());
    }
}