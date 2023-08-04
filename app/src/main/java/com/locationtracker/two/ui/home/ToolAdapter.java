package com.locationtracker.two.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.locationtracker.two.databinding.ToolItemBinding;
import com.locationtracker.two.model.Tool;

import java.util.ArrayList;

public class ToolAdapter extends ArrayAdapter<Tool> {

    public interface OnToolClickListener {
        void onClick(Tool tool);
    }

    private final OnToolClickListener listener;
    public ToolAdapter(@NonNull Context context, ArrayList<Tool> tools, OnToolClickListener listener) {
        super(context, 0, tools);
        this.listener = listener;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToolItemBinding binding = ToolItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        Tool tool = getItem(position);
        binding.name.setText(tool.getNameResId());
        binding.icon.setImageResource(tool.getIconResId());

        binding.getRoot().setOnClickListener(view -> {
            listener.onClick(tool);
        });

        return binding.getRoot();
    }
}
