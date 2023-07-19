package com.locationtracker.one.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.locationtracker.one.R;
import com.locationtracker.one.databinding.FragmentToolsBinding;
import com.locationtracker.one.model.Tool;
import com.locationtracker.one.ui.bank.BankActivity;
import com.locationtracker.one.ui.directionmap.DirectionMapActivity;
import com.locationtracker.one.ui.location.UserLocationActivity;
import com.locationtracker.one.ui.nearbyplaces.NearbyPlacesActivity;
import com.locationtracker.one.ui.numberlocation.NumberLocationActivity;
import com.locationtracker.one.ui.rechargeplans.RechargePlansActivity;
import com.locationtracker.one.ui.siminfo.SIMInfoActivity;
import com.locationtracker.one.ui.streetview.StreetViewActivity;
import com.locationtracker.one.ui.ussd.USSDCodeActivity;

import java.util.ArrayList;
import java.util.List;

public class ToolsFragment extends Fragment implements ToolAdapter.OnToolClickListener {

    private FragmentToolsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentToolsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeMenu();
        initializeBannerAd();
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeInterstitialAd();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initializeBannerAd() {
        MobileAds.initialize(getContext(), initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adBannerView.loadAd(adRequest);
    }

    private void initializeInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                getContext(),
                getString(R.string.interstitial_ad_unit_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        interstitialAd.show(getActivity());
                    }
                }
        );
    }

    private void initializeMenu() {
        ArrayList<Tool> tools = getTools();
        Context context = getContext();
        if (context != null) {
            ToolAdapter adapter = new ToolAdapter(getContext(), tools, this);
            binding.toolGridView.setAdapter(adapter);
        }
    }

    private ArrayList<Tool> getTools() {
        return new ArrayList<>(List.of(
                new Tool(R.string.number_location, R.drawable.ic_number_location),
                new Tool(R.string.direction_map, R.drawable.ic_direction_map),
                new Tool(R.string.street_view, R.drawable.ic_street_view),
                new Tool(R.string.near_by_places, R.drawable.ic_nearby_places),
                new Tool(R.string.user_location, R.drawable.ic_user_location),
                new Tool(R.string.recharge_plans, R.drawable.ic_recharge_plans),
                new Tool(R.string.sim_info, R.drawable.ic_sim_info),
                new Tool(R.string.bank_info, R.drawable.ic_bank_info),
                new Tool(R.string.ussd_codes, R.drawable.ic_ussd_codes)
        ));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(Tool tool) {
        if (tool.getIconResId() == R.drawable.ic_number_location) {
            startActivity(new Intent(getContext(), NumberLocationActivity.class));
        }
        if (tool.getIconResId() == R.drawable.ic_direction_map) {
            startActivity(new Intent(getContext(), DirectionMapActivity.class));
        }
        if (tool.getIconResId() == R.drawable.ic_street_view) {
            startActivity(new Intent(getContext(), StreetViewActivity.class));
        }
        if (tool.getIconResId() == R.drawable.ic_nearby_places) {
            startActivity(new Intent(getContext(), NearbyPlacesActivity.class));
        }
        if (tool.getIconResId() == R.drawable.ic_user_location) {
            startActivity(new Intent(getContext(), UserLocationActivity.class));
        }
        if (tool.getIconResId() == R.drawable.ic_recharge_plans) {
            startActivity(new Intent(getContext(), RechargePlansActivity.class));
        }
        if (tool.getIconResId() == R.drawable.ic_sim_info) {
            startActivity(new Intent(getContext(), SIMInfoActivity.class));
        }
        if (tool.getIconResId() == R.drawable.ic_bank_info) {
            startActivity(new Intent(getContext(), BankActivity.class));
        }
        if (tool.getIconResId() == R.drawable.ic_ussd_codes) {
            startActivity(new Intent(getContext(), USSDCodeActivity.class));
        }
    }
}