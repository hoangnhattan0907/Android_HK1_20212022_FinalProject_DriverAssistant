package com.example.afinal.Oil;

import com.example.afinal.Vehicle.VehicleInfo;

import java.io.Serializable;

public class OilInfo extends VehicleInfo implements Serializable {
    private VehicleInfo vehicleInfo;
    private int KmAtRefuel;
    private String TypeOfOil;
    private int Cost;
    private String FuelDate;
    private String FuelTime;
    private String Note;
    private String DateNext;
    private int KmNext;

    public OilInfo(VehicleInfo vehicleInfo) {
        super(vehicleInfo);
    }

    public OilInfo(VehicleInfo vehicleInfo, int kmAtRefuel, String type, int cost, String fuelDate, String fuelTime, String note, String dateNext) {
        super(vehicleInfo);
        KmAtRefuel = kmAtRefuel;
        TypeOfOil = type;
        Cost = cost;
        FuelDate = fuelDate;
        FuelTime = fuelTime;
        Note = note;
        DateNext = dateNext;
    }

    public OilInfo(VehicleInfo vehicleInfo, int kmAtRefuel, String type, int cost, String fuelDate, String fuelTime, String note, int kmNext) {
        super(vehicleInfo);
        KmAtRefuel = kmAtRefuel;
        TypeOfOil = type;
        Cost = cost;
        FuelDate = fuelDate;
        FuelTime = fuelTime;
        Note = note;
        KmNext = kmNext;
    }

    public OilInfo() {
    }

    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(VehicleInfo vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public int getKmAtRefuel() {
        return KmAtRefuel;
    }

    public void setKmAtRefuel(int kmAtRefuel) {
        KmAtRefuel = kmAtRefuel;
    }

    public String getTypeOfOil() {
        return TypeOfOil;
    }

    public void setTypeOfOil(String type) {
        TypeOfOil = type;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int cost) {
        Cost = cost;
    }

    public String getFuelDate() {
        return FuelDate;
    }

    public void setFuelDate(String fuelDate) {
        FuelDate = fuelDate;
    }

    public String getFuelTime() {
        return FuelTime;
    }

    public void setFuelTime(String fuelTime) {
        FuelTime = fuelTime;
    }

    @Override
    public String getNote() {
        return Note;
    }

    @Override
    public void setNote(String note) {
        Note = note;
    }

    public String getDateNext() {
        return DateNext;
    }

    public void setDateNext(String dateNext) {
        DateNext = dateNext;
    }

    public int getKmNext() {
        return KmNext;
    }

    public void setKmNext(int kmNext) {
        KmNext = kmNext;
    }
}
