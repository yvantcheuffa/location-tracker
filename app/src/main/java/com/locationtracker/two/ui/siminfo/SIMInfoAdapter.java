package com.locationtracker.two.ui.siminfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.locationtracker.two.databinding.SimInfoItemBinding;
import com.locationtracker.two.model.SIMInfo;

import java.util.ArrayList;

public class SIMInfoAdapter extends ArrayAdapter<SIMInfo> {

    public interface OnSIMInfoClickListener {
        void onClick(SIMInfo simInfo);
    }

    private final OnSIMInfoClickListener listener;

    public SIMInfoAdapter(@NonNull Context context, ArrayList<SIMInfo> simInfoList, OnSIMInfoClickListener listener) {
        super(context, 0, simInfoList);
        this.listener = listener;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SimInfoItemBinding binding = SimInfoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        SIMInfo simInfo = getItem(position);
        binding.name.setText(simInfo.getName());
        int iconResId = parent.getResources().getIdentifier(simInfo.getIcon(), "drawable", parent.getContext().getPackageName());
        binding.icon.setImageResource(iconResId);

        binding.getRoot().setOnClickListener(view -> {
            listener.onClick(simInfo);
        });

        return binding.getRoot();
    }
}
