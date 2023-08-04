package com.locationtracker.two.ui.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.locationtracker.two.model.Contact;
import com.locationtracker.two.databinding.ContactItemBinding;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    public interface OnContactClickListener {
        void onClick(Contact contact);
    }

    private final OnContactClickListener listener;

    public ContactAdapter(@NonNull Context context, ArrayList<Contact> contacts, OnContactClickListener listener) {
        super(context, 0, contacts);
        this.listener = listener;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContactItemBinding binding = ContactItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        Contact contact = getItem(position);
        binding.name.setText(contact.getName());
        binding.number.setText(contact.getNumber());

        binding.btnMakeCall.setOnClickListener(view -> listener.onClick(contact));

        return binding.getRoot();
    }
}
