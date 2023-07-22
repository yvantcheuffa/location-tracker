package com.locationtracker.one.ui.rechargedetails;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.locationtracker.one.model.RechargeDetail;

import java.util.List;

public class RechargeDetailAdapter extends ArrayAdapter<RechargeDetail> {
    private final List<RechargeDetail> rechargeDetails;

    public RechargeDetailAdapter(Context context, int textViewResourceId, List<RechargeDetail> rechargeDetails) {
        super(context, textViewResourceId, rechargeDetails);
        this.rechargeDetails = rechargeDetails;
    }

    @Override
    public int getCount() {
        return rechargeDetails.size();
    }

    @Override
    public RechargeDetail getItem(int position) {
        return rechargeDetails.get(position);
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
        label.setText(rechargeDetails.get(position).getSip());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(rechargeDetails.get(position).getSip());
        return label;
    }
}