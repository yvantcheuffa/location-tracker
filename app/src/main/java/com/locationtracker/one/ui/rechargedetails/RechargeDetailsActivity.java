package com.locationtracker.one.ui.rechargedetails;

import static com.locationtracker.one.Utils.loadFileContent;
import static com.locationtracker.one.ui.rechargeplans.RechargePlansActivity.ARG_RECHARGE_PLANS;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.locationtracker.one.R;
import com.locationtracker.one.databinding.ActivityRechargeDetailsBinding;
import com.locationtracker.one.model.RechargeDetail;
import com.locationtracker.one.model.RechargePlan;
import com.locationtracker.one.ui.rechargeplans.RechargePlansActivity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RechargeDetailsActivity extends AppCompatActivity {

    private ActivityRechargeDetailsBinding binding;
    private ArrayAdapter<String> citiesAdapter;
    private ArrayList<RechargeDetail> rechargeDetails;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private List<RechargePlan> mRechargePlans;
    private final FullScreenContentCallback interstitialFullscreenCallback = new FullScreenContentCallback() {
        @Override
        public void onAdDismissedFullScreenContent() {
            mInterstitialAd = null;
            openPlansActivity();
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            openPlansActivity();
        }
    };
    private ArrayList<RechargePlan> airtelRechargePlans;
    private ArrayList<RechargePlan> jioRechargePlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.recharge_details));

        binding = ActivityRechargeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adRequest = new AdRequest.Builder().build();

        initializeBannerAd();
        initializeSpinners();
        setupClickListeners();
        airtelRechargePlans = getAirtelRechargePlans();
        jioRechargePlans = getJioRechargePlans();
    }

    private void setupClickListeners() {
        binding.btnGetPlans.setOnClickListener(view -> {
            if (mInterstitialAd == null) {
                openPlansActivity();
            } else {
                mInterstitialAd.show(this);
            }
        });
    }

    private ArrayList<RechargePlan> getAirtelRechargePlans() {
        String jsonContent = loadFileContent("airtel_recharge_plans.json", getAssets());
        Type listType = new TypeToken<ArrayList<RechargePlan>>() {
        }.getType();
        return new Gson().fromJson(jsonContent, listType);
    }

    private ArrayList<RechargePlan> getJioRechargePlans() {
        String jsonContent = loadFileContent("jio_recharge_plans.json", getAssets());
        Type listType = new TypeToken<ArrayList<RechargePlan>>() {
        }.getType();
        return new Gson().fromJson(jsonContent, listType);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeInterstitialAd();
    }

    private void initializeInterstitialAd() {
        InterstitialAd.load(
                this,
                getString(R.string.interstitial_ad_unit_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(interstitialFullscreenCallback);
                    }
                }
        );
    }

    private void openPlansActivity() {
        Intent intent = new Intent(this, RechargePlansActivity.class);
        intent.putExtra(ARG_RECHARGE_PLANS, (Serializable) mRechargePlans);
        startActivity(intent);
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

    private ArrayList<RechargeDetail> getRechargeDetails() {
        String jsonContent = loadFileContent("recharge_details.json", getAssets());
        Type listType = new TypeToken<ArrayList<RechargeDetail>>() {
        }.getType();
        return new Gson().fromJson(jsonContent, listType);
    }

    private void initializeSpinners() {
        rechargeDetails = getRechargeDetails();
        List<String> cities = rechargeDetails.get(0).getCities();
        RechargeDetailAdapter rechargeDetailAdapter = new RechargeDetailAdapter(this, android.R.layout.simple_spinner_dropdown_item, rechargeDetails);
        citiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);

        binding.spinnerSip.setAdapter(rechargeDetailAdapter);
        binding.spinnerSip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    mRechargePlans = airtelRechargePlans;
                } else if (position == 3) {
                    mRechargePlans = jioRechargePlans;
                } else {
                    mRechargePlans = null;
                }
                List<String> cities = new ArrayList<>(rechargeDetails.get(position).getCities());
                citiesAdapter.clear();
                citiesAdapter.addAll(cities);
                citiesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerCities.setAdapter(citiesAdapter);
        binding.spinnerCities.setSelection(0);
    }
}