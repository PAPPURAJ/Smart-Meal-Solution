package com.blogspot.rajbtc.hallmanagement.user;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.blogspot.rajbtc.hallmanagement.R;
public class CashIn_User extends AppCompatActivity {
String val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in__user);
        val=getIntent().getStringExtra("msg");
        ((TextView)findViewById(R.id.cashin_userTv)).setText(val+"\n\nSend your money bKash or Nagad to 01818400400 \nand wait for the confirmation");
    }
}