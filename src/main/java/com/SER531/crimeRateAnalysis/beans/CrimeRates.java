package com.SER531.crimeRateAnalysis.beans;

public class CrimeRates {
    private String state;
    private String city;
    private int population;
    private PropertyCrimeRates propertyCrimeRates;
    private ViolentCrimeRates violentCrimeRates;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public PropertyCrimeRates getPropertyCrimeRates() {
        return propertyCrimeRates;
    }

    public void setPropertyCrimeRates(PropertyCrimeRates propertyCrimeRates) {
        this.propertyCrimeRates = propertyCrimeRates;
    }

    public ViolentCrimeRates getViolentCrimeRates() {
        return violentCrimeRates;
    }

    public void setViolentCrimeRates(ViolentCrimeRates violentCrimeRates) {
        this.violentCrimeRates = violentCrimeRates;
    }
}
