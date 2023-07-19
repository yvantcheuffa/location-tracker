package com.locationtracker.one.ui.bank;

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
import com.locationtracker.one.databinding.ActivityBankBinding;
import com.locationtracker.one.model.Bank;
import com.locationtracker.one.ui.bank.BankAdapter.OnBankClickListener;
import com.locationtracker.one.ui.contact.ContactActivity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class BankActivity extends AppCompatActivity implements OnBankClickListener {
    private ActivityBankBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBankBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.bank_details));

        initializeListView();
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
                        interstitialAd.show(BankActivity.this);
                    }
                }
        );
    }

    private void initializeListView() {
        ArrayList<Bank> banks = getBanks();
        BankAdapter adapter = new BankAdapter(this, banks, this);
        binding.bankListView.setAdapter(adapter);
    }

    private ArrayList<Bank> getBanks() {
        String jsonContent = loadFileContent("banks.json", getAssets());
        Type listType = new TypeToken<ArrayList<Bank>>() {
        }.getType();
        return new Gson().fromJson(jsonContent, listType);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onClick(Bank bank) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(ARG_CONTACTS, (Serializable) bank.getContacts());
        intent.putExtra(ARG_TITLE, bank.getName());
        startActivity(intent);
    }
}
