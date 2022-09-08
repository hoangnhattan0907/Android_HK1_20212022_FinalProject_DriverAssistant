package com.example.afinal.Vehicle;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VehicleInfo implements Serializable{

    private String Id;
    private String Name;
    private String VehicleNumber;
    private String TypeOfVehihcle;
    private String Automaker;
    private String Date;
    private int Km;
    private String Note;

    public VehicleInfo(){
    }
    public VehicleInfo(VehicleInfo vehicleInfo){
        Name = vehicleInfo.Name;
        VehicleNumber = vehicleInfo.VehicleNumber;
        TypeOfVehihcle = vehicleInfo.TypeOfVehihcle;
        Automaker = vehicleInfo.Automaker;
        Date = vehicleInfo.Date;
        Km = vehicleInfo.Km;
        Note = vehicleInfo.Note;
    }
    public VehicleInfo(String name, String vehicleNumber, String typeOfVehihcle, String automaker,String date, int km, String note) {
        Name = name;
        VehicleNumber = vehicleNumber;
        TypeOfVehihcle = typeOfVehihcle;
        Automaker = automaker;
        Date = date;
        Km = km;
        Note = note;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getTypeOfVehihcle() {
        return TypeOfVehihcle;
    }

    public void setTypeOfVehihcle(String typeOfVehihcle) {
        TypeOfVehihcle = typeOfVehihcle;
    }

    public String getAutomaker() {
        return Automaker;
    }

    public void setAutomaker(String automaker) {
        Automaker = automaker;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getKm() {
        return Km;
    }

    public void setKm(int km) {
        Km = km;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",Name);
        result.put("automaker",Automaker);
        result.put("date",Date);
        result.put("km",Km);
        result.put("type",TypeOfVehihcle);
        result.put("vehicleNumber",VehicleNumber);
        result.put("note",Note);
        return result;
    }
}
