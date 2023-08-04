package com.locationtracker.two.ui.bank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.locationtracker.two.databinding.BankItemBinding;
import com.locationtracker.two.model.Bank;

import java.util.ArrayList;

public class BankAdapter extends ArrayAdapter<Bank> {

    public interface OnBankClickListener {
        void onClick(Bank bank);
    }

    private final OnBankClickListener listener;

    public BankAdapter(@NonNull Context context, ArrayList<Bank> banks, OnBankClickListener listener) {
        super(context, 0, banks);
        this.listener = listener;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BankItemBinding binding = BankItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        Bank bank = getItem(position);
        binding.name.setText(bank.getName());
        int iconResId = parent.getResources().getIdentifier(bank.getIcon(), "drawable", parent.getContext().getPackageName());
        binding.icon.setImageResource(iconResId);
        binding.getRoot().setOnClickListener(view -> listener.onClick(bank));

        return binding.getRoot();
    }
}

