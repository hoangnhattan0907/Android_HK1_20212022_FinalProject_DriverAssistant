package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UpdateKm extends AppCompatActivity {
    Spinner spnVehicleName;
    Button btnSave;
    ImageView ivBack;
    TextView tvUpdateKm;
    EditText etKm1;
    private String tempName;
    private int tempKm;
    private ArrayAdapter arrayAdapter;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();
    String[] testEmail = email.split("@");

    private ArrayList<String> listName = new ArrayList<>();
    private ArrayList<Integer> listKm = new ArrayList();
    private List<String> name = new ArrayList<>();
    private List<Integer> km = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_km);

        init();
        getFromMain();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,name);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVehicleName.setAdapter(arrayAdapter);

        spnVehicleName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempName = name.get(position);
                tempKm =  km.get(position);
                tvUpdateKm.setText("("+tempKm+")");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPushData();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init(){
        spnVehicleName = (Spinner) findViewById(R.id.spn);
        btnSave = findViewById(R.id.btnSave);
        ivBack = findViewById(R.id.ivBack);
        tvUpdateKm = findViewById(R.id.tvUpdateKm);
        etKm1 = findViewById(R.id.etKm);
    }

    private void onClickPushData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(testEmail[0]+"/vehicle_info");

        int etKm = Integer.valueOf(etKm1.getText().toString().trim());
        if(etKm <= tempKm) {
            Toast.makeText(UpdateKm.this, "Giá trị km phải lớn hơn " + tempKm, Toast.LENGTH_SHORT).show();
        } else{
            myRef.child(tempName).child("km").setValue(Integer.valueOf(etKm1.getText().toString().trim()), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(UpdateKm.this, "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        }
    }
    private void getFromMain(){
        Intent intent = getIntent();
        listName = intent.getStringArrayListExtra("Vehicle_Name");
        listKm = intent.getIntegerArrayListExtra("Vehicle_Km");
        if(listName != null){
            for(int i = 0; i < listName.size();i++){
                name.add(listName.get(i));
                km.add(listKm.get(i));
            }
        }
    }
}