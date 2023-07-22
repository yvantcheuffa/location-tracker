package com.locationtracker.one.ui.rechargedetails;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.locationtracker.one.Utils.loadFileContent;
import static com.locationtracker.one.ui.rechargeplans.RechargePlansActivity.ARG_RECHARGE_PLANS;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
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

    private static final int PERMISSION_REQUEST_CODE = 101;
    private ActivityRechargeDetailsBinding binding;
    private ArrayAdapter<String> citiesAdapter;
    private ArrayList<RechargeDetail> rechargeDetails;
    private final AdRequest adRequest = new AdRequest.Builder().build();
    private ArrayList<RechargePlan> airtelRechargePlans;
    private ArrayList<RechargePlan> jioRechargePlans;
    private int activeSipIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.recharge_details));

        binding = ActivityRechargeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeBannerAd();
        initializeSpinners();
        setupClickListeners();
        airtelRechargePlans = getAirtelRechargePlans();
        jioRechargePlans = getJioRechargePlans();
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

    private void setupClickListeners() {
        binding.btnGetPlans.setOnClickListener(view -> InterstitialAd.load(
                this,
                getString(R.string.interstitial_ad_unit_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                openPlansActivity();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                openPlansActivity();
                            }
                        });
                        interstitialAd.show(RechargeDetailsActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        openPlansActivity();
                    }
                }
        ));
    }

    private void openPlansActivity() {
        Intent intent = new Intent(this, RechargePlansActivity.class);
        if (activeSipIndex == 0) {
            intent.putExtra(ARG_RECHARGE_PLANS, (Serializable) airtelRechargePlans);
        } else if(activeSipIndex == 3) {
            intent.putExtra(ARG_RECHARGE_PLANS, (Serializable) jioRechargePlans);
        }
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return (super.onOptionsItemSelected(item));
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(this::initializeInterstitialAd, 3000);
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void initializeBannerAd() {
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adBannerView.loadAd(adRequest);
    }

    private void initializeInterstitialAd() {
        InterstitialAd.load(
                this,
                getString(R.string.interstitial_ad_unit_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                checkPermissions();
                            }
                        });
                        interstitialAd.show(RechargeDetailsActivity.this);
                    }
                }
        );
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
                activeSipIndex = position;
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