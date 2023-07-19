package com.locationtracker.one.ui.ussd;

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
import com.locationtracker.one.databinding.ActivityUssdCodeBinding;
import com.locationtracker.one.model.USSDCode;
import com.locationtracker.one.ui.contact.ContactActivity;
import com.locationtracker.one.ui.ussd.USSDCodeAdapter.OnUSSDCodeClickListener;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class USSDCodeActivity extends AppCompatActivity implements OnUSSDCodeClickListener {

    private ActivityUssdCodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUssdCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ussd_codes));

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
                        interstitialAd.show(USSDCodeActivity.this);
                    }
                }
        );
    }

    private void initializeGridView() {
        ArrayList<USSDCode> codes = getUSSDCodes();
        USSDCodeAdapter adapter = new USSDCodeAdapter(this, codes, this);
        binding.ussdGridView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return (super.onOptionsItemSelected(item));
    }

    private ArrayList<USSDCode> getUSSDCodes() {
        String jsonContent = loadFileContent("ussd_codes.json", getAssets());
        Type listType = new TypeToken<ArrayList<USSDCode>>() {
        }.getType();
        return new Gson().fromJson(jsonContent, listType);
    }

    @Override
    public void onClick(USSDCode ussdCode) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(ARG_CONTACTS, (Serializable) ussdCode.getContacts());
        intent.putExtra(ARG_TITLE, String.format("%s USSD Codes", ussdCode.getName()));
        startActivity(intent);
    }
}