package com.example.afinal.Vehicle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.afinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder>     {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();
    String[] testEmail = email.split("@");
    private List<VehicleInfo> listVehicle;
    private VehicleAdapter vehicleAdapter;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private Context context;

    public VehicleAdapter(Context context, List<VehicleInfo> listVehicle){
        this.context = context;
        this.listVehicle = listVehicle;
    }
    public void setData(List<VehicleInfo> list){
        this.listVehicle = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final VehicleInfo p = listVehicle.get(position);
        if(p==null)
            return;
        viewBinderHelper.bind(holder.layout_item, String.valueOf(p.getId()));
        if(p.getTypeOfVehihcle().equals("Xe hơi"))
            holder.ivPic.setImageResource(R.drawable.car);
        else
            holder.ivPic.setImageResource(R.drawable.moto);

        holder.tvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VehicleShow.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("show",p);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.tvNameVehicle.setText(p.getName());
        holder.tvVehicleNumber.setText(p.getVehicleNumber());
        holder.tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VehicleUpdate.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("update",p);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteData(p);
            }
        });
    }

    @Override
    public int getItemCount() {

        if(listVehicle !=null)
            return listVehicle.size();
        return 0;
    }

    public void onClickDeleteData(VehicleInfo vehicleInfo){
        new AlertDialog.Builder(context)
                .setTitle("Xóa phương tiện")
                .setMessage("Bạn có chắc chắn muốn xóa bản ghi này không?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(testEmail[0]+"/vehicle_info");
                        DatabaseReference myRef2 = database.getReference(testEmail[0]+"/oil_info");
                        DatabaseReference myRef3 = database.getReference(testEmail[0]+"/gas_info");
                        DatabaseReference myRef4 = database.getReference(testEmail[0]+"/part_info");
                        DatabaseReference myRef5 = database.getReference(testEmail[0]+"/schedule");

                        myRef2.child(vehicleInfo.getName()).removeValue();
                        myRef3.child(vehicleInfo.getName()).removeValue();
                        myRef4.child(vehicleInfo.getName()).removeValue();
                        myRef5.child(vehicleInfo.getName()).removeValue();

                        myRef.child(vehicleInfo.getName()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(context,"Xóa phương tiện thành công",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SwipeRevealLayout layout_item;
        TextView tvNameVehicle, tvVehicleNumber, tvDelete, tvChange, tvShow;
        ImageView ivPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvNameVehicle = (TextView) itemView.findViewById(R.id.tvNameVehicle);
            tvVehicleNumber = (TextView) itemView.findViewById(R.id.tvVehicleNumber);
            layout_item = (SwipeRevealLayout) itemView.findViewById(R.id.layout_item);
            tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            tvChange = (TextView) itemView.findViewById(R.id.tvChange);
            tvShow = (TextView) itemView.findViewById(R.id.tvShow);
            ivPic = (ImageView) itemView.findViewById(R.id.ivPic);
        }
    }


}
