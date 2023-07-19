package com.locationtracker.one.ui.siminfo;

import static com.locationtracker.one.Utils.loadFileContent;
import static com.locationtracker.one.ui.contact.ContactActivity.ARG_CONTACTS;
import static com.locationtracker.one.ui.contact.ContactActivity.ARG_TITLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.locationtracker.one.R;
import com.locationtracker.one.databinding.ActivitySimInfoBinding;
import com.locationtracker.one.model.SIMInfo;
import com.locationtracker.one.ui.contact.ContactActivity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SIMInfoActivity extends AppCompatActivity implements SIMInfoAdapter.OnSIMInfoClickListener {

    private ActivitySimInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySimInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.sim_details));

        initializeGridView();
        initializeBannerAd();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeInterstitialAd();
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
                        interstitialAd.show(SIMInfoActivity.this);
                    }
                }
        );
    }

    private void initializeGridView() {
        ArrayList<SIMInfo> codes = getSimInfoList();
        SIMInfoAdapter adapter = new SIMInfoAdapter(this, codes, this);
        binding.simInfoGridView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return (super.onOptionsItemSelected(item));
    }

    private ArrayList<SIMInfo> getSimInfoList() {
        String jsonContent = loadFileContent("sim_info.json", getAssets());
        Type listType = new TypeToken<ArrayList<SIMInfo>>() {
        }.getType();
        return new Gson().fromJson(jsonContent, listType);
    }

    @Override
    public void onClick(SIMInfo simInfo) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(ARG_CONTACTS, (Serializable) simInfo.getContacts());
        intent.putExtra(ARG_TITLE, String.format("%s Codes", simInfo.getName()));
        startActivity(intent);
    }
}