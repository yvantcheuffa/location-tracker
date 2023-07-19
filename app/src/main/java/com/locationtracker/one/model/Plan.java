package com.locationtracker.one.model;

import java.util.List;

public class Plan {
    private String sip;
    private List<String> cities;

    public String getSip() {
        return sip;
    }

    public void setSip(String sip) {
        this.sip = sip;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
