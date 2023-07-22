package com.locationtracker.one.ui.rechargeplans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.locationtracker.one.databinding.PlanItemBinding;
import com.locationtracker.one.model.Plan;

import java.util.ArrayList;

public class PlanAdapter extends ArrayAdapter<Plan> {

    public PlanAdapter(@NonNull Context context, ArrayList<Plan> plans) {
        super(context, 0, plans);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PlanItemBinding binding = PlanItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        Plan plan = getItem(position);
        binding.price.setText(plan.getPrice());
        binding.description.setText(plan.getDescription());

        return binding.getRoot();
    }


    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
