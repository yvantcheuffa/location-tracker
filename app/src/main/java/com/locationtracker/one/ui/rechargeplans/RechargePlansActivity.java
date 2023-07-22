package com.locationtracker.one.ui.rechargeplans;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.locationtracker.one.R;
import com.locationtracker.one.databinding.ActivityRechargePlansBinding;
import com.locationtracker.one.model.RechargePlan;

import java.util.ArrayList;

public class RechargePlansActivity extends AppCompatActivity {

    private ActivityRechargePlansBinding binding;
    public static String ARG_RECHARGE_PLANS = "ARG_RECHARGE_PLANS";
    private ArrayList<RechargePlan> rechargePlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.recharge_plans));
        rechargePlans = (ArrayList<RechargePlan>) getIntent().getSerializableExtra(ARG_RECHARGE_PLANS);
        binding = ActivityRechargePlansBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeBannerAd();
        loadPlans();
    }

    private void loadPlans() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_plans));
        progressDialog.show();

        new Handler().postDelayed(() -> {
            if (rechargePlans != null) {
                initViews();
            } else {
                Toast.makeText(this, getString(R.string.recharge_plans_error), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }, 2000);
    }

    private void initViews() {
        binding.appbar.setVisibility(View.VISIBLE);
        binding.viewpager.setOffscreenPageLimit(5);
        binding.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabs));
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setRechargePlanFragmentToTabLayout();
    }

    private void setRechargePlanFragmentToTabLayout() {
        for (RechargePlan rechargePlan : rechargePlans) {
            binding.tabs.addTab(binding.tabs.newTab().setText(rechargePlan.getTitle()));
        }
        RechargePlanFragmentAdapter mRechargePlanFragmentAdapter = new RechargePlanFragmentAdapter(getSupportFragmentManager(), rechargePlans);

        binding.viewpager.setAdapter(mRechargePlanFragmentAdapter);

        binding.viewpager.setCurrentItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return (super.onOptionsItemSelected(item));
    }

    private void initializeBannerAd() {
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adBannerView.loadAd(adRequest);
    }
}