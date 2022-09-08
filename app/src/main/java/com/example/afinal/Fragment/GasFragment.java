package com.example.afinal.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.afinal.Gas.GasInfo;
import com.example.afinal.MainActivity;
import com.example.afinal.R;
import com.example.afinal.Vehicle.GasAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GasFragment extends Fragment {
    Button btnDelete;
    MainActivity mainActivity;
    GasAdapter gasAdapter;
    RecyclerView rvGas;
    Spinner spn;

    private ArrayAdapter arrayAdapter;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail()+"";
    String[] testEmail = email.split("@");

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(testEmail[0]+"/gas_info");

    private ArrayList<String> listName = new ArrayList<>();
    private List<GasInfo> listGas = new ArrayList<>();

    private String tempName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gas, container, false);
        mainActivity = (MainActivity) getActivity();
        rvGas = (RecyclerView) view.findViewById(R.id.rvGas);
        btnDelete = view.findViewById(R.id.btnDeleteAll);
        spn = (Spinner) view.findViewById(R.id.spn);
        /*------------------------------------------------------------------------------------------*/
        listName = (ArrayList<String>) getArguments().getStringArrayList("name");
        listName.add(0,"All");

        /*------------------------------------------------------------------------------------------*/
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,listName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(arrayAdapter);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempName = listName.get(position);
                listGas.clear();
                gasAdapter.notifyDataSetChanged();
                getListFromDatabase(listName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rvGas.setLayoutManager(new LinearLayoutManager(getContext()));
        gasAdapter = new GasAdapter(getContext(), listGas);
        rvGas.setAdapter(gasAdapter);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteData();
            }
        });
        return view;
    }

    private void getListFromDatabase(ArrayList<String> listName) {
        listGas.clear();
        if (tempName == "All") {
            Toast.makeText(getContext(), "all", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < listName.size(); i++) {
                String tempName2 = listName.get(i);
                DatabaseReference myRef2 = database.getReference(testEmail[0] + "/gas_info/" + tempName2);
                myRef2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        GasInfo gasInfo = snapshot.getValue(GasInfo.class);
                        if (gasInfo != null) {
                            listGas.add(gasInfo);
                            gasAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        GasInfo gasInfo = snapshot.getValue(GasInfo.class);
                        if (gasInfo == null || listGas == null || listGas.isEmpty()) {
                            return;
                        }
                        for (int i = 0; i < listGas.size(); i++) {
                            if (gasInfo.getName() == listGas.get(i).getName()) {
                                listGas.set(i, gasInfo);
                                break;
                            }
                        }
                        gasAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        GasInfo gasInfo = snapshot.getValue(GasInfo.class);
                        if (gasInfo == null || listGas == null || listGas.isEmpty()) {
                            return;
                        }

                        for (int i = 0; i < listGas.size(); i++) {
                            if (gasInfo.getName() == listGas.get(i).getName()) {
                                listGas.remove(listGas.get(i));
                                break;
                            }
                        }
                        gasAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } else {
            Toast.makeText(getContext(),tempName,Toast.LENGTH_SHORT).show();
            listGas.clear();
            DatabaseReference myRef2 = database.getReference(testEmail[0]+"/gas_info/"+tempName);
            myRef2.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    GasInfo gasInfo = snapshot.getValue(GasInfo.class);
                    if (gasInfo != null) {
                        listGas.add(gasInfo);
                        gasAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    GasInfo gasInfo = snapshot.getValue(GasInfo.class);
                    if (gasInfo == null || listGas == null || listGas.isEmpty()) {
                        return;
                    }
                    for (int i = 0; i < listGas.size(); i++) {
                        if (gasInfo.getName() == listGas.get(i).getName()) {
                            listGas.set(i, gasInfo);
                            break;
                        }
                    }
                    gasAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    GasInfo gasInfo = snapshot.getValue(GasInfo.class);
                    if (gasInfo == null || listGas == null || listGas.isEmpty()) {
                        return;
                    }

                    for (int i = 0; i < listGas.size(); i++) {
                        if (gasInfo.getName() == listGas.get(i).getName()) {
                            listGas.remove(listGas.get(i));
                            break;
                        }
                    }
                    gasAdapter.notifyDataSetChanged();
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
    public void onClickDeleteData(){
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa lịch sự")
                .setMessage("Bạn có chắc chắn muốn xóa toàn bộ lịch sử đổ xăng không?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myRef.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getContext(),"Xóa thành công",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }
}