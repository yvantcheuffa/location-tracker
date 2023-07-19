package com.locationtracker.one.ui.ussd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.locationtracker.one.databinding.UssdCodeItemBinding;
import com.locationtracker.one.model.USSDCode;

import java.util.ArrayList;

public class USSDCodeAdapter extends ArrayAdapter<USSDCode> {

    public interface OnUSSDCodeClickListener {
        void onClick(USSDCode ussdCode);
    }

    private final OnUSSDCodeClickListener listener;

    public USSDCodeAdapter(@NonNull Context context, ArrayList<USSDCode> ussdCodes, OnUSSDCodeClickListener listener) {
        super(context, 0, ussdCodes);
        this.listener = listener;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UssdCodeItemBinding binding = UssdCodeItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        USSDCode ussdCode = getItem(position);
        binding.name.setText(ussdCode.getName());
        int iconResId = parent.getResources().getIdentifier(ussdCode.getIcon(), "drawable", parent.getContext().getPackageName());
        binding.icon.setImageResource(iconResId);

        binding.getRoot().setOnClickListener(view -> listener.onClick(ussdCode));

        return binding.getRoot();
    }
}
