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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText etMail,etPassword,etPassword2;
    Button btnSignUp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        initListener();
    }

    private void init(){
        etMail = findViewById(R.id.etMail);
        btnSignUp = findViewById(R.id.btnSignUp);
        etPassword = findViewById(R.id.etPassword);
        etPassword2 = findViewById(R.id.etPassword2);
        Context context;
        progressDialog = new ProgressDialog(this);
    }

    private void initListener(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMail.getText().toString().trim()+"" == ""){
                    Toast.makeText(SignUpActivity.this,"Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    String account = etMail.getText().toString().trim()+"";
                    if(account.indexOf("@") != -1){
                        if(etPassword.getText().toString().trim() == etPassword2.getText().toString().trim()){
                            Toast.makeText(SignUpActivity.this,"Mật khẩu nhập lại chưa chính xác !", Toast.LENGTH_SHORT).show();
                        }else{
                            onClickSignUp();
                            Toast.makeText(SignUpActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this,"Email không hợp lệ",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void onClickSignUp(){
        String strEmail = etMail.getText().toString().trim();
        String strPassword = etPassword.getText().toString().trim();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Đăng ký không thành công",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}