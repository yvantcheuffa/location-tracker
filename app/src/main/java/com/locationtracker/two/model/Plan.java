package com.locationtracker.two.model;

import java.io.Serializable;

public class Plan implements Serializable {
    private String price;
    private String description;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
