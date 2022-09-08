package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.afinal.Fragment.GasFragment;
import com.example.afinal.Fragment.HomeFragment;
import com.example.afinal.Fragment.OilFragment;
import com.example.afinal.Fragment.PartFragment;
import com.example.afinal.Gas.GasActivity;
import com.example.afinal.MailLogInActivity;
import com.example.afinal.Oil.OilActivity;
import com.example.afinal.Oil.OilInfo;
import com.example.afinal.Part.PartActivity;
import com.example.afinal.Vehicle.VehicleInfo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public  class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView menu,tvMail;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Fragment fragment;
    ImageView imgGas, imgOil, imgUpdateKm,imgPart;

    private  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String name = user.getDisplayName()+"";
    String email = user.getEmail()+"";
    String[] testEmail = email.split("@");

    private HomeFragment homeFragment;
    private GasFragment gasFragment;
    private OilFragment oilFragment;
    private PartFragment partFragment;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private List<VehicleInfo> listVehicle = new ArrayList<>();
    private ArrayList<String> listName = new ArrayList<>();
    private ArrayList<Integer> listKm = new ArrayList<>();

    private List<OilInfo> listOil = new ArrayList<>();
    private ArrayList<Schedule> listSchedule = new ArrayList<>();

    private static final int NOTIFICATION_ID = 10;
    private static final int FRAGMENT_HOME = 100;
    private static final int FRAGMENT_GAS = 101;
    private static final int FRAGMENT_OIL = 102;
    private static final int FRAGMENT_PART = 103;

    private int currentFragment = FRAGMENT_HOME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inIt();

        showUserInfomation();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*-----------------------------------------Menu----------------------------------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_view);
        /*-------------------------------------------------------------------------------------------*/

        getListFromDatabase(name);
        /*-----------------------------------------Button--------------------------------------------*/
        imgGas.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent i = new Intent(MainActivity.this, GasActivity.class);
                                          test();
                                          i.putStringArrayListExtra("Vehicle_Name", listName);
                                          i.putIntegerArrayListExtra("Vehicle_Km",listKm);
                                          getSchedule();
//                                          Toast.makeText(MainActivity.this,listKm.size()+"",Toast.LENGTH_SHORT).show();
                                          startActivity(i);
                                      }
                                  }
        );

        imgOil.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent i = new Intent(MainActivity.this, OilActivity.class);
                                          test();
                                          i.putStringArrayListExtra("Vehicle_Name", listName);
                                          i.putIntegerArrayListExtra("Vehicle_Km",listKm);
                                          getSchedule();
//                                          Toast.makeText(MainActivity.this,listKm.size()+"",Toast.LENGTH_SHORT).show();
                                          startActivity(i);
                                      }
                                  }
        );

        imgUpdateKm.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent i = new Intent(MainActivity.this, UpdateKm.class);
                                               test();
                                               i.putStringArrayListExtra("Vehicle_Name", listName);
                                               i.putIntegerArrayListExtra("Vehicle_Km",listKm);
                                               getSchedule();
//                                          Toast.makeText(MainActivity.this,listKm.size()+"",Toast.LENGTH_SHORT).show();
                                               startActivity(i);
                                           }
                                       }
        );
        imgPart.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent i = new Intent(MainActivity.this, PartActivity.class);
                                               test();
                                               i.putStringArrayListExtra("Vehicle_Name", listName);
                                               i.putIntegerArrayListExtra("Vehicle_Km",listKm);
                                               getSchedule();
//                                          Toast.makeText(MainActivity.this,listKm.size()+"",Toast.LENGTH_SHORT).show();
                                               startActivity(i);

                                           }
                                       }
        );

        /*-----------------------------------------Control--------------------------------------------*/
        sendDataToFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.danhSachXe).setChecked(true);

    }

    public void inIt() {
        navigationView = findViewById(R.id.nav_view);
        tvMail = navigationView.getHeaderView(0).findViewById(R.id.tvMail);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgOil = (ImageView) findViewById(R.id.imgNhot);
        imgGas = (ImageView) findViewById(R.id.imgXang);
        imgUpdateKm = (ImageView) findViewById(R.id.imgUpdateKm);
        imgPart = (ImageView) findViewById(R.id.imgPart);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.danhSachXe:
                if(currentFragment != FRAGMENT_HOME){
                    fragment = new HomeFragment();
                    sendDataToFragment(fragment);
                    currentFragment = FRAGMENT_HOME;
                    navigationView.getMenu().findItem(R.id.danhSachXe).setChecked(true);
                    navigationView.getMenu().findItem(R.id.lichSuDoXang).setChecked(false);
                    navigationView.getMenu().findItem(R.id.lichSuThayLinhKien).setChecked(false);
                    navigationView.getMenu().findItem(R.id.lichSuThayNhot).setChecked(false);
                }
                break;
            case R.id.lichSuDoXang:
                if(currentFragment != FRAGMENT_GAS){
                    gasFragment = new GasFragment();
                    sendDataToFragment(gasFragment);
                    currentFragment = FRAGMENT_GAS;

                    test();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("name", listName);
                    gasFragment.setArguments(bundle);

                    navigationView.getMenu().findItem(R.id.danhSachXe).setChecked(false);
                    navigationView.getMenu().findItem(R.id.lichSuDoXang).setChecked(true);
                    navigationView.getMenu().findItem(R.id.lichSuThayLinhKien).setChecked(false);
                    navigationView.getMenu().findItem(R.id.lichSuThayNhot).setChecked(false);
                }
                break;
            case R.id.lichSuThayNhot:
                if(currentFragment != FRAGMENT_OIL){
                    oilFragment = new OilFragment();
                    sendDataToFragment(oilFragment);
                    currentFragment = FRAGMENT_OIL;

                    test();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("name", listName);
                    oilFragment.setArguments(bundle);

                    navigationView.getMenu().findItem(R.id.danhSachXe).setChecked(false);
                    navigationView.getMenu().findItem(R.id.lichSuDoXang).setChecked(false);
                    navigationView.getMenu().findItem(R.id.lichSuThayLinhKien).setChecked(false);
                    navigationView.getMenu().findItem(R.id.lichSuThayNhot).setChecked(true);
                }
                break;
            case R.id.lichSuThayLinhKien:
                if(currentFragment != FRAGMENT_PART){
                    partFragment = new PartFragment();
                    sendDataToFragment(partFragment);
                    currentFragment = FRAGMENT_PART;

                    test();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("name", listName);
                    partFragment.setArguments(bundle);

                    navigationView.getMenu().findItem(R.id.danhSachXe).setChecked(false);
                    navigationView.getMenu().findItem(R.id.lichSuDoXang).setChecked(false);
                    navigationView.getMenu().findItem(R.id.lichSuThayLinhKien).setChecked(true);
                    navigationView.getMenu().findItem(R.id.lichSuThayNhot).setChecked(false);
                }
                break;
            case R.id.banDo:
                Intent intent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(intent);
                break;
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(this, MailLogInActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendDataToFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    private void test(){
        listName.removeAll(listName);
        listKm.removeAll(listKm);
        for(int i = 0; i<listVehicle.size();i++){
            listName.add(listVehicle.get(i).getName());
            listKm.add(listVehicle.get(i).getKm());
        }
    }
    private  void getSchedule(){
        String name;
        String date;
        int km;
        for(int i = 0; i<listSchedule.size();i++){
            date = listSchedule.get(i).getDateNext();
            name = listSchedule.get(i).getName();
            km = listSchedule.get(i).getKmNext();
            if(km == 0 && listSchedule.get(i).isType() == true){
                LocalDate localDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String format = localDate.format(formatter);

                if(date.equals(format)){
                    String notifi = "Xe "+ name +" cần được thay nhớt ";
                    sendNotification(notifi);
                }
            }else if(km!= 0 && listSchedule.get(i).isType() == true){
                for(int j=0; j<listKm.size();j++){
                    if(listKm.get(i) >= listSchedule.get(i).getKmNext()){
                        String notifi = "Xe "+ name +" cần được thay nhớt ";
                        sendNotification(notifi);
                    }
                }
            }
        }
    }
    private void getListFromDatabase(String email){
        DatabaseReference myRef = database.getReference(testEmail[0]+"/vehicle_info");
        DatabaseReference myRef2 = database.getReference(testEmail[0]+"/schedule");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                VehicleInfo vehicleInfo = snapshot.getValue(VehicleInfo.class);
                if(vehicleInfo != null){
                    listVehicle.add(vehicleInfo);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                VehicleInfo vehicleInfo = snapshot.getValue(VehicleInfo.class);
                if(vehicleInfo == null || listVehicle == null || listVehicle.isEmpty()){
                    return;
                }
                for (int i = 0; i< listVehicle.size();i++){
                    if (vehicleInfo.getName()== listVehicle.get(i).getName()){
                        listVehicle.set(i, vehicleInfo);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                VehicleInfo vehicleInfo = snapshot.getValue(VehicleInfo.class);
                if(vehicleInfo == null || listVehicle == null || listVehicle.isEmpty()){
                    return;
                }

                for (int i = 0; i< listVehicle.size();i++){
                    if (vehicleInfo.getName() == listVehicle.get(i).getName()){
                        listVehicle.remove(listVehicle.get(i));
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef2.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               Schedule schedule = snapshot.getValue(Schedule.class);
               if(schedule != null){
                   listSchedule.add(schedule);
               }

           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               Schedule schedule = snapshot.getValue(Schedule.class);
               if(schedule == null || listSchedule == null || listSchedule.isEmpty()){
                   return;
               }
               for (int i = 0; i< listSchedule.size();i++){
                   if (schedule.getName()== listSchedule.get(i).getName()){
                       listSchedule.set(i, schedule);
                       break;
                   }
               }

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {
               Schedule schedule = snapshot.getValue(Schedule.class);
               if(schedule == null || listSchedule == null || listSchedule.isEmpty()){
                   return;
               }

               for (int i = 0; i< listSchedule.size();i++){
                   if (schedule.getName() == listSchedule.get(i).getName()){
                       listSchedule.remove(listVehicle.get(i));
                       break;
                   }
               }
           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

    }

    private void sendNotification(String inputValue){
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String format = localDate.format(formatter);
        Notification notification = new NotificationCompat.Builder(this, MyApp.CHANNEL_ID)
                .setContentTitle("Vehicle manager")
                .setContentText(inputValue)
                .setSmallIcon(R.drawable.motorcycle)
                .setColor(getResources().getColor(R.color.background))
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(getNotificationId(), notification);
        }
    }

    private int getNotificationId(){
        return (int) new Date().getTime();
    }

    private void showUserInfomation(){
        if(user == null){
            return;
        }

        tvMail.setText(email);
    }
}

//                    fragment = new HomeFragment();
//                    menuItem.setChecked(true);