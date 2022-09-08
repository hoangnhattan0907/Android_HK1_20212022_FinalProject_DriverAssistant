package com.example.afinal.Part;

import com.example.afinal.Vehicle.VehicleInfo;

import java.io.Serializable;

public class PartInfo extends VehicleInfo implements Serializable {
    private VehicleInfo vehicleInfo;
    private String PartName;
    private int Number;
    private int Cost;
    private String FuelDate;
    private String FuelTime;
    private String Note;
    private String DateNext;
    private int KmNext;

    public PartInfo(VehicleInfo vehicleInfo) {
        super(vehicleInfo);
    }

    public PartInfo() {
    }

    public PartInfo(VehicleInfo vehicleInfo, String partName, int number, int cost, String fuelDate, String fuelTime, String note, String dateNext) {
        super(vehicleInfo);
        PartName = partName;
        Number = number;
        Cost = cost;
        FuelDate = fuelDate;
        FuelTime = fuelTime;
        Note = note;
        DateNext = dateNext;
    }

    public PartInfo(VehicleInfo vehicleInfo, String partName, int number, int cost, String fuelDate, String fuelTime, String note, int kmNext) {
        super(vehicleInfo);
        PartName = partName;
        Number = number;
        Cost = cost;
        FuelDate = fuelDate;
        FuelTime = fuelTime;
        Note = note;
        KmNext = kmNext;
    }

    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(VehicleInfo vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public String getPartName() {
        return PartName;
    }

    public void setPartName(String partName) {
        PartName = partName;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
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
