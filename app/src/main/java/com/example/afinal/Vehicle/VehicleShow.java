package com.example.afinal.Vehicle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.afinal.R;

public class VehicleShow extends AppCompatActivity {
    TextView etName, etVehicleNumber , etAutoMaker, etKm,etNote, etType, etDate;
    ImageView ivBack;

    VehicleInfo vehicleInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_show);

        init();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        vehicleInfo = (VehicleInfo) intent.getSerializableExtra("show");

        etName.setText(vehicleInfo.getName());
        etVehicleNumber.setText(vehicleInfo.getVehicleNumber());
        etType.setText(vehicleInfo.getTypeOfVehihcle());

        etAutoMaker.setText(vehicleInfo.getAutomaker());
        etDate.setText(vehicleInfo.getDate().toString());

        etKm.setText(vehicleInfo.getKm()+"");
        etNote.setText(vehicleInfo.getNote());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void init(){
        etName = findViewById(R.id.tvName);
        etVehicleNumber =findViewById(R.id.tvVehicleNumber);
        etType = findViewById(R.id.tvType);
        etAutoMaker = findViewById(R.id.tvAutoMaker);
        etKm = findViewById(R.id.tvKm);
        etNote = findViewById(R.id.tvNote);
        etDate = findViewById(R.id.tvDate);
        ivBack = findViewById(R.id.ivBack);
    }
}