package com.locationtracker.one.ui.rechargeplans;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.locationtracker.one.model.Plan;

import java.util.List;

public class PlanAdapter extends ArrayAdapter<Plan> {
    private final List<Plan> plans;

    public PlanAdapter(Context context, int textViewResourceId, List<Plan> plans) {
        super(context, textViewResourceId, plans);
        this.plans = plans;
    }

    @Override
    public int getCount() {
        return plans.size();
    }

    @Override
    public Plan getItem(int position) {
        return plans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(plans.get(position).getSip());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(plans.get(position).getSip());
        return label;
    }
}