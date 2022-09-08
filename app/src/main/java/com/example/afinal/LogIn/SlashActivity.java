package com.example.afinal.LogIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.afinal.MailLogInActivity;
import com.example.afinal.MainActivity;
import com.example.afinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SlashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextAcitvity();
            }
        },2000);
    }
    private void nextAcitvity(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(SlashActivity.this, MailLogInActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(SlashActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

}