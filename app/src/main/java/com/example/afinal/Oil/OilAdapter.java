package com.example.afinal.Oil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.afinal.Gas.GasInfo;
import com.example.afinal.Oil.OilInfo;
import com.example.afinal.Part.PartInfo;
import com.example.afinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OilAdapter extends RecyclerView.Adapter<OilAdapter.ViewHolder>     {

    private List<OilInfo> listOil;
    private OilAdapter oilAdapter;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private Context context;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail()+"";
    String[] testEmail = email.split("@");

    public OilAdapter(Context context, List<OilInfo> listOil){
        this.context = context;
        this.listOil = listOil;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oil,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OilInfo p = listOil.get(position);
        if(p==null)
            return;
        holder.tvDate.setText(p.getFuelDate()+"");
        holder.tvCost.setText(p.getCost()+"");
        holder.tvKm.setText(p.getKmAtRefuel()+"");
        holder.tvName.setText(p.getName());
        holder.tvTypeOfOil.setText(p.getTypeOfOil()+"");
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteData(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listOil !=null)
            return listOil.size();
        return 0;
    }

    public void onClickDeleteData(OilInfo oilInfo){
        new AlertDialog.Builder(context)
                .setTitle("Xóa bản ghi lịch sử")
                .setMessage("Bạn có chắc chắn muốn xóa bản ghi này không?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(testEmail[0]+"/oil_info");
                        myRef.child(oilInfo.getName()).child(oilInfo.getKmAtRefuel()+"").removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(context,"Xóa bản ghi thành công",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTypeOfOil, tvCost, tvKm,tvName;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTypeOfOil = (TextView) itemView.findViewById(R.id.tvTypeOfOil);
            tvKm = (TextView) itemView.findViewById(R.id.tvKm);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            linearLayout = itemView.findViewById(R.id.linearlayout);
        }
    }


}