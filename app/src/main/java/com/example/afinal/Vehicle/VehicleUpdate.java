package com.example.afinal.Vehicle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

//import com.example.afinal.Database.VehicleDatabase;
import com.example.afinal.MainActivity;
import com.example.afinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class VehicleUpdate extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();
    String[] testEmail = email.split("@");

    EditText etVehicleNumber , etAutoMaker, etKm,etNote, etType, etDate;
    TextView etName;
    Button btnSave;
    ImageView ivBack;

    LocalDate date;
    String dateString;
    int pic = R.drawable.plus ;

    public static final int CHANGE_DATA =102;
    private String id;

    VehicleInfo vehicleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_update);

        init();
        registerForContextMenu(etType);
        Intent intent = getIntent();

        vehicleInfo = (VehicleInfo) intent.getSerializableExtra("update");

        if(vehicleInfo != null){
            id = vehicleInfo.getId();
            etName.setText(vehicleInfo.getName());
            etVehicleNumber.setText(vehicleInfo.getVehicleNumber());
            etAutoMaker.setText(vehicleInfo.getAutomaker());
            etType.setText(vehicleInfo.getTypeOfVehihcle());
            etAutoMaker.setText(vehicleInfo.getAutomaker());
            etDate.setText(vehicleInfo.getDate());
            dateString = vehicleInfo.getDate();
            etKm.setText(vehicleInfo.getKm()+"");
            etNote.setText(vehicleInfo.getNote());
        }

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
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
        etName = findViewById(R.id.etName);
        etVehicleNumber =findViewById(R.id.etVehicleNumber);
        etType = findViewById(R.id.etType);
        etAutoMaker = findViewById(R.id.etAutoMaker);
        etKm = findViewById(R.id.etKm);
        etNote = findViewById(R.id.etNote);
        btnSave = findViewById(R.id.btnSave);
        etDate = findViewById(R.id.etDate);
        ivBack = findViewById(R.id.ivBack);
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
                dateString = date.toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                etDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },year,month,day);
        datePickerDialog.show();
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if(item.getItemId() == R.id.isCar) {
            pic = R.drawable.car;
            etType.setText("Xe hơi");
        }
        else{
            pic = R.drawable.moto;
            etType.setText("Xe máy");
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vehicle_type_option_menu, menu);
    }

    private void updateInfo(){
        VehicleInfo put = new VehicleInfo(
                etName.getText().toString().trim(),
                etVehicleNumber.getText().toString().trim(),
                etType.getText().toString().trim(),
                etAutoMaker.getText().toString().trim(),
                dateString,
                Integer.valueOf(etKm.getText().toString().trim()),
                etNote.getText().toString().trim());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(testEmail[0]+"/vehicle_info");

        myRef.child(put.getName()).updateChildren(put.toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(VehicleUpdate.this, "update thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }
}