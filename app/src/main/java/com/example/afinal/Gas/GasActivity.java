package com.example.afinal.Gas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.afinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class GasActivity extends AppCompatActivity {
    Spinner spnVehicleName;
    EditText fuelAmount, fuelCapacity, kmAtRefuel, fuelLocation;
    TextView fuelDate, fuelTime;
    Button btnSave;
    ImageView ivBack;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail()+"";
    String[] testEmail = email.split("@");

    private ArrayAdapter arrayAdapter;

    private ArrayList<String> listName = new ArrayList<>();
    private ArrayList<Integer> listKm = new ArrayList();
    private List<String> name = new ArrayList<>();
    private List<Integer> km = new ArrayList<>();

    private String tempName;
    private int tempKm;
    private LocalDate date;
    int hour,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fuelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        fuelTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
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
        btnSave = findViewById(R.id.btnSave);
        spnVehicleName = (Spinner) findViewById(R.id.spn);
        fuelAmount = findViewById(R.id.etCost);
        fuelCapacity = findViewById(R.id.etValue);
        kmAtRefuel = findViewById(R.id.etKm);
        fuelDate = findViewById(R.id.tvDate);
        fuelTime = findViewById(R.id.tvTime);
        fuelLocation = findViewById(R.id.etAddress);
        ivBack = findViewById(R.id.ivBack);
    }

    private void onClickPushData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(testEmail[0]+"/gas_info");
        DatabaseReference myRef2 = database.getReference(testEmail[0]+"/vehicle_info");
        int etKm = Integer.valueOf(kmAtRefuel.getText().toString().trim());
        if(etKm <= tempKm){
            Toast.makeText(GasActivity.this,"Giá trị km phải lớn hơn "+ tempKm,Toast.LENGTH_SHORT).show();
        }else{
            GasInfo gasInfo = new GasInfo();
            gasInfo.setFuelAmount(Integer.valueOf(fuelAmount.getText().toString().trim()));
            gasInfo.setFuelCapacity(Float.valueOf(fuelCapacity.getText().toString().trim()));
            gasInfo.setKmAtRefuel(Integer.valueOf(kmAtRefuel.getText().toString().trim()));
            gasInfo.setFuelDate(fuelDate.getText().toString().trim());
            gasInfo.setFuelTime(fuelTime.getText().toString().trim());
            gasInfo.setFuelLocation(String.valueOf(fuelLocation.getText().toString().trim()));
            gasInfo.setName(tempName);

            myRef2.child(tempName).child("km").setValue(Integer.valueOf(kmAtRefuel.getText().toString().trim()));
            myRef.child(tempName).child(gasInfo.getKmAtRefuel()+"").setValue(gasInfo, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(GasActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        }


    }

    private void getFromMain(){
        Intent intent = getIntent();
        listName = intent.getStringArrayListExtra("Vehicle_Name");
        listKm = intent.getIntegerArrayListExtra("Vehicle_Km");
//        listKm = (ArrayList<Float>) intent.getSerializableExtra("Vehicle_Km");
//        Toast.makeText(GasActivity.this,listKm.get(0).toString(),Toast.LENGTH_SHORT).show();
        if(listName != null){
            for(int i = 0; i < listName.size();i++){
                name.add(listName.get(i));
                km.add(listKm.get(i));
            }
        }
    }

    private void pickDate(){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                date = LocalDate.of(year, month+1, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                fuelDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },year,month,day);
        datePickerDialog.show();
    }

    private void pickTime(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minute = minute;
                fuelTime.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style, onTimeSetListener,hour,minute,true);
        timePickerDialog.setTitle("select time");
        timePickerDialog.show();
    }
}
