package com.locationtracker.two.ui.rechargeplans;

import static com.locationtracker.two.ui.rechargeplans.PlanFragment.ARG_PLAN;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.locationtracker.two.model.RechargePlan;

import java.io.Serializable;
import java.util.List;

public class RechargePlanFragmentAdapter extends FragmentStatePagerAdapter {
    private final List<RechargePlan> rechargePlans;

    RechargePlanFragmentAdapter(FragmentManager fm, List<RechargePlan> rechargePlans) {
        super(fm);
        this.rechargePlans = rechargePlans;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PLAN, (Serializable) rechargePlans.get(position).getPlans());
        Fragment fragment = PlanFragment.newInstance();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return rechargePlans.size();
    }
}