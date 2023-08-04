package com.locationtracker.two.ui.rechargeplans;

import static com.locationtracker.two.Utils.getAdSize;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.locationtracker.two.R;
import com.locationtracker.two.databinding.ActivityRechargePlansBinding;
import com.locationtracker.two.model.RechargePlan;

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

        loadBannerAd();
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

    private void loadBannerAd() {
        AdSize adSize = getAdSize(this, binding.adViewContainer.getWidth(), getResources().getDisplayMetrics().density);
        AdView adView = new AdView(this);
        binding.adViewContainer.addView(adView);
        adView.setAdSize(adSize);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adView.loadAd(new AdRequest.Builder().build());
    }
}