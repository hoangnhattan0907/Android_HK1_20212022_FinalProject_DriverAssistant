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

public class VehicleDetail extends AppCompatActivity {
    EditText etName, etVehicleNumber , etAutomaker, etKm,etNote, etType, etDate;
    Button btnSave;
    ImageView ivBack;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();
    String name = user.getDisplayName();
    String[] testEmail = email.split("@");
    LocalDate date;
    int pic = R.drawable.plus ;

    MainActivity mainActivity;
    public static final int PUT_DATA = 13;

    private VehicleInfo vehicleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        init();

        registerForContextMenu(etType);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPushData();
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                etDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },year,month,day);
        datePickerDialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vehicle_type_option_menu, menu);
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

    private void init(){
        etName = findViewById(R.id.etName);
        etVehicleNumber =findViewById(R.id.etVehicleNumber);
        etType = findViewById(R.id.etType);
        etAutomaker = findViewById(R.id.etAutoMaker);
        etKm = findViewById(R.id.etKm);
        etNote = findViewById(R.id.etNote);
        btnSave = findViewById(R.id.btnSave);
        etDate = findViewById(R.id.etDate);
        ivBack = findViewById(R.id.ivBack);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PUT_DATA && resultCode == RESULT_OK){}
    }

    private void onClickPushData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(testEmail[0]+"/vehicle_info");

        VehicleInfo ve = new VehicleInfo();
        ve.setName(etName.getText().toString().trim());
        ve.setVehicleNumber(etVehicleNumber.getText().toString().trim());
        ve.setTypeOfVehihcle(etType.getText().toString().trim());
        ve.setAutomaker(etAutomaker.getText().toString().trim());
        ve.setKm(Integer.valueOf(etKm.getText().toString().trim()));
        ve.setDate(date.toString().trim());
        ve.setNote(etNote.getText().toString().trim());

        myRef.child(ve.getName()).setValue(ve, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(VehicleDetail.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

}