package com.viziazasofia.da.data_extract;

import java.util.ArrayList;

public class Trolley {
    private int number;
    private ArrayList<Station> stations;

    Trolley(){}

    Trolley(int number){
        this.number = number;
    }

    Trolley(int number, ArrayList<Station> stations){
        this.number = number;
        this.stations = stations;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    @Override
    public String toString() {
        return "Trolley{" +
                "number=" + number +
                ", stations=" + stations +
                '}';
    }
}
