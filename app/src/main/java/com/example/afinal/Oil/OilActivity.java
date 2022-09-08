package com.example.afinal.Oil;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.Schedule;
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

public class OilActivity extends AppCompatActivity {
    Spinner spnVehicleName;
    EditText etCost, etType, etKm1, etNote,etKmNext;
    TextView tvDateNext,tvDate,tvTime;
    Button btnSave;
    ImageView ivBack;
    RadioGroup radioGroup;
    RadioButton rbKmNext,rbDateNext;
    private ArrayAdapter arrayAdapter;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail()+"";
    String[] testEmail = email.split("@");

    private ArrayList<String> listName = new ArrayList<>();
    private ArrayList<Integer> listKm = new ArrayList<>();
    private List<String> name = new ArrayList<>();
    private List<Integer> km = new ArrayList<>();

    private String tempName;
    private int tempKm;
    private LocalDate date;
    private boolean check1 = false;
    private boolean check2 = false;
    int hour,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil);

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

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(tvDate);
            }
        });
        tvDateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(tvDateNext);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPushData();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rbDateNext:
                        Toast.makeText(OilActivity.this,"Đặt lịch hẹn theo ngày",Toast.LENGTH_SHORT).show();
                        check1 = true;
                        break;
                    case R.id.rbKmNext:
                        Toast.makeText(OilActivity.this,"Đặt lịch hẹn theo Km",Toast.LENGTH_SHORT).show();
                        check2 = true;
                        break;
                }
                if(check1 == true && check2 == false){
                    tvDateNext.setEnabled(true);
                    etKmNext.setEnabled(false);
                }else if (check2 == true && check1 == false){
                    tvDateNext.setEnabled(false);
                    etKmNext.setEnabled(true);
                }
            }
        });
    }
    private void init(){
        spnVehicleName = (Spinner) findViewById(R.id.spn);
        etCost = findViewById(R.id.etCost);
        etType = findViewById(R.id.etType);
        etKm1 = findViewById(R.id.etKm);
        etNote = findViewById(R.id.etNote);
        tvDateNext = findViewById(R.id.tvDateNext);
        etKmNext = findViewById(R.id.etKmNext);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        btnSave = findViewById(R.id.btnSave);
        ivBack = findViewById(R.id.ivBack);
        radioGroup = findViewById(R.id.rg);
        rbDateNext = findViewById(R.id.rbDateNext);
        rbKmNext = findViewById(R.id.rbKmNext);
    }

    private void onClickPushData(){
        Boolean checkInfo = false;
        Schedule schedule = new Schedule();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(testEmail[0]+"/oil_info");
        DatabaseReference myRef2 = database.getReference(testEmail[0]+"/schedule");
        DatabaseReference myRef3 = database.getReference(testEmail[0]+"/vehicle_info");

        int etKm = Integer.valueOf(etKm1.getText().toString().trim());
        if(etKm <= tempKm) {
            Toast.makeText(OilActivity.this, "Giá trị km phải lớn hơn " + tempKm, Toast.LENGTH_SHORT).show();
        } else{
            OilInfo oilInfo = new OilInfo();
            oilInfo.setKmAtRefuel(Integer.valueOf(etKm1.getText().toString().trim()));
            oilInfo.setTypeOfOil(etType.getText().toString().trim());
            oilInfo.setCost((Integer.valueOf(etCost.getText().toString().trim())));
            oilInfo.setFuelDate(tvDate.getText().toString().trim());
            oilInfo.setFuelTime(tvTime.getText().toString().trim());
            oilInfo.setNote(String.valueOf(etNote.getText().toString().trim()));
            oilInfo.setName(tempName);

            schedule.setName(tempName);
            if(check1 == true && check2 == false){
                oilInfo.setDateNext(tvDateNext.getText().toString().trim());
                schedule.setDateNext(tvDateNext.getText().toString().trim());
                schedule.setType(true);
            }else if (check2 == true && check1 == false){
                oilInfo.setKmNext(Integer.valueOf(etKmNext.getText().toString().trim()));
                schedule.setKmNext(Integer.valueOf(etKmNext.getText().toString().trim()));
                schedule.setType(true);
            }

            myRef3.child(tempName).child("km").setValue(Integer.valueOf(etKm1.getText().toString().trim()));
            myRef2.child(tempName).setValue(schedule);
            myRef.child(tempName).child(oilInfo.getKmAtRefuel()+"").setValue(oilInfo, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(OilActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
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

    private void pickDate(TextView context){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                date = LocalDate.of(year, month+1 , dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                context.setText(simpleDateFormat.format(calendar.getTime()));
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
                tvTime.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style, onTimeSetListener,hour,minute,true);
        timePickerDialog.setTitle("select time");
        timePickerDialog.show();
    }
}