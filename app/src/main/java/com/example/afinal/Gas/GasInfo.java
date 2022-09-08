package com.example.afinal.Gas;

import com.example.afinal.Vehicle.VehicleInfo;

import java.io.Serializable;

public class GasInfo extends VehicleInfo implements Serializable {
    private VehicleInfo vehicleInfo;
    private int FuelAmount;
    private float FuelCapacity;
    private int KmAtRefuel;
    private String FuelDate;
    private String FuelTime;
    private String FuelLocation;


    public GasInfo(VehicleInfo vehicleInfo, int fuelAmount, float fuelCapacity, int kmAtRefuel, String fuelDate, String fuelTime, String fuelLocation) {
        super(vehicleInfo);
        FuelAmount = fuelAmount;
        FuelCapacity = fuelCapacity;
        KmAtRefuel = kmAtRefuel;
        FuelDate = fuelDate;
        FuelTime = fuelTime;
        FuelLocation = fuelLocation;
    }

    public GasInfo(VehicleInfo vehicleInfo) {
        super(vehicleInfo);
    }

    public GasInfo() {
    }
    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(VehicleInfo vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public int getFuelAmount() {
        return FuelAmount;
    }

    public void setFuelAmount(int fuelAmount) {
        FuelAmount = fuelAmount;
    }

    public float getFuelCapacity() {
        return FuelCapacity;
    }

    public void setFuelCapacity(float fuelCapacity) {
        FuelCapacity = fuelCapacity;
    }

    public int getKmAtRefuel() {
        return KmAtRefuel;
    }

    public void setKmAtRefuel(int kmAtRefuel) {
        KmAtRefuel = kmAtRefuel;
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

    public String getFuelLocation() {
        return FuelLocation;
    }

    public void setFuelLocation(String fuelLocation) {
        FuelLocation = fuelLocation;
    }

}
