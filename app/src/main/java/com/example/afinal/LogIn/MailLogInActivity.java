package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MailLogInActivity extends AppCompatActivity {
    LinearLayout layoutSignUp;
    EditText etPassword,etMail;
    Button btnLogIn;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_log_in);

        init();
        initListener();
    }

    private void init(){
        layoutSignUp = findViewById(R.id.layout_sign_up);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        progressDialog = new ProgressDialog(this);
        btnLogIn = findViewById(R.id.btnLogIn);
    }

    private void initListener(){
        layoutSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MailLogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = etMail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if(mail.isEmpty() == false && mail != "" && password.isEmpty() == false && password != ""){
                    onClickSignIn(mail,password);
                }
            }
        });
    }
    
    private void onClickSignIn(String email,String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MailLogInActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MailLogInActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}