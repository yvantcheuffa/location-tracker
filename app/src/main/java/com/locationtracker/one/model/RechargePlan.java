package com.locationtracker.one.model;

import java.io.Serializable;
import java.util.List;

public class RechargePlan implements Serializable {
    private String title;
    private List<Plan> plans;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Plan> getPlans() {
        return plans;
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
    }
}
