package com.viziazasofia.da.data_extract;

import java.util.ArrayList;

public class Bus {
    private String number;
    private ArrayList<Station> stations;

    Bus(){}

    Bus(String number){
        this.number = number;
    }

    Bus(String number, ArrayList<Station> stations){
        this.number = number;
        this.stations = stations;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
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
        return "{" +
                "name:'" + number + '\'' +
                ", stops:" + stations +
                '}';
    }
}
