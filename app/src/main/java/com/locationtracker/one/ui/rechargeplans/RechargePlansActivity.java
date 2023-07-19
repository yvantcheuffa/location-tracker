package com.locationtracker.one.ui.rechargeplans;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.locationtracker.one.Utils.loadFileContent;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.locationtracker.one.R;
import com.locationtracker.one.databinding.ActivityRechargePlansBinding;
import com.locationtracker.one.model.Plan;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RechargePlansActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 101;
    private ActivityRechargePlansBinding binding;
    private ArrayAdapter<String> citiesAdapter;
    private ArrayList<Plan> plans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.recharge_plans));

        binding = ActivityRechargePlansBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeBannerAd();
        initializeSpinners();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return (super.onOptionsItemSelected(item));
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeInterstitialAd();
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
        AdRequest adRequest = new AdRequest.Builder().build();
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
                        interstitialAd.show(RechargePlansActivity.this);
                    }
                }
        );
    }

    private ArrayList<Plan> getPlans() {
        String jsonContent = loadFileContent("plans.json", getAssets());
        Type listType = new TypeToken<ArrayList<Plan>>() {
        }.getType();
        return new Gson().fromJson(jsonContent, listType);
    }

    private void initializeSpinners() {
        plans = getPlans();
        List<String> cities = plans.get(0).getCities();
        PlanAdapter planAdapter = new PlanAdapter(this, android.R.layout.simple_spinner_dropdown_item, plans);
        citiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);

        binding.spinnerPlans.setAdapter(planAdapter);
        binding.spinnerPlans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                List<String> cities = new ArrayList<>(plans.get(position).getCities());
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