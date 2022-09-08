package com.example.afinal;

public class Schedule {
    private String Name;
    private String DateNext;
    private int KmNext;
    private boolean Type;


    public Schedule() {

    }

    public Schedule(String name, int kmNext) {
        Name = name;
        KmNext = kmNext;
    }

    public boolean isType() {
        return Type;
    }

    public void setType(boolean type) {
        Type = type;
    }

    public Schedule(String name, String dateNext) {
        Name = name;
        DateNext = dateNext;
    }

    public int getKmNext() {
        return KmNext;
    }

    public void setKmNext(int kmNext) {
        KmNext = kmNext;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDateNext() {
        return DateNext;
    }

    public void setDateNext(String dateNext) {
        DateNext = dateNext;
    }
}
