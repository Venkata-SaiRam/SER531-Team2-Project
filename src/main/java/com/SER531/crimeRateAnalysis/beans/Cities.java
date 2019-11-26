package com.SER531.crimeRateAnalysis.beans;

import java.util.ArrayList;
import java.util.List;

public class Cities {
    private List<String> cities;

    public Cities(){
        cities = new ArrayList<String>();
    }

    public List<String> getCities(){
        return cities;
    }

    public void addState(String city){
        cities.add(city);
    }

    public void setStates(List<String> cities){
        this.cities = cities;
    }
}
