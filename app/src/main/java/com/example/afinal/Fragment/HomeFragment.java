package com.example.afinal.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

//import com.example.afinal.Database.VehicleDatabase;
import com.example.afinal.MainActivity;
import com.example.afinal.R;
import com.example.afinal.Vehicle.VehicleAdapter;
import com.example.afinal.Vehicle.VehicleDetail;
import com.example.afinal.Vehicle.VehicleInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();
    String name = user.getDisplayName();
    String[] testEmail = email.split("@");
    private LinearLayout addVehicle;
    private SwipeRefreshLayout swipeRefreshLayout;

    MainActivity mainActivity;

    VehicleAdapter adapter;
    RecyclerView rvVehicle;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(testEmail[0]+"/vehicle_info");

    private List<VehicleInfo> listVehicle = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity) getActivity();
        rvVehicle = (RecyclerView) view.findViewById(R.id.rvVehicle);
        addVehicle = (LinearLayout) view.findViewById(R.id.addVehicle);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);

        getListFromDatabase();
        rvVehicle.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VehicleAdapter(getContext(), listVehicle);
        rvVehicle.setAdapter(adapter);

        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VehicleDetail.class);
                startActivity(intent);
            }
        });
        return view;
    }
/*-------------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onRefresh() {
        adapter.setData(listVehicle);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
    private void getListFromDatabase(){

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                VehicleInfo vehicleInfo = snapshot.getValue(VehicleInfo.class);
                if(vehicleInfo != null){
                    listVehicle.add(vehicleInfo);
                    adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

