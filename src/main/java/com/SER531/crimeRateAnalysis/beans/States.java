package com.SER531.crimeRateAnalysis.beans;

import java.util.ArrayList;
import java.util.List;

public class States {

    private List<String> states;

    public States(){
        states = new ArrayList<String>();
    }

    public List<String> getStates(){
        return states;
    }

    public void addState(String state){
        states.add(state);
    }

    public void setStates(List<String> states){
        this.states = states;
    }
}
