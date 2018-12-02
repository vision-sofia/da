package com.viziazasofia.da.data_extract;

public class Station {
    private String code;
    private String name;
    private int gotUp;
    private int descended;
    private int exchange;
    private int loaded;

    Station(){}

    Station(String code, String name, int gotUp, int descended, int exchange, int loaded){
        this.code = code;
        this.name = name;
        this.gotUp = gotUp;
        this.descended = descended;
        this.exchange = exchange;
        this.loaded = loaded;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGotUp() {
        return gotUp;
    }

    public void setGotUp(int gotUp) {
        this.gotUp = gotUp;
    }

    public int getDescended() {
        return descended;
    }

    public void setDescended(int descended) {
        this.descended = descended;
    }

    public int getExchange() {
        return exchange;
    }

    public void setExchange(int exchange) {
        this.exchange = exchange;
    }

    public int getLoaded() {
        return loaded;
    }

    public void setLoaded(int loaded) {
        this.loaded = loaded;
    }

    @Override
    public String toString() {
        return "{" +
                "code:'" + code + '\'' +
                ", input:" + gotUp +
                ", output:" + descended +
                ", exchange:" + exchange +
                ", natovarenost:" + loaded +
                '}';
    }
}
