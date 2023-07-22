package com.locationtracker.one.ui.bank;

import static com.locationtracker.one.Utils.getAdSize;
import static com.locationtracker.one.Utils.loadFileContent;
import static com.locationtracker.one.ui.contact.ContactActivity.ARG_CONTACTS;
import static com.locationtracker.one.ui.contact.ContactActivity.ARG_TITLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
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
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private Bank mBank;

    private final FullScreenContentCallback interstitialFullscreenCallback = new FullScreenContentCallback() {
        @Override
        public void onAdDismissedFullScreenContent() {
            mInterstitialAd = null;
            startActivity(getActivityIntent());
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            startActivity(getActivityIntent());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBankBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.bank_details));

        adRequest = new AdRequest.Builder().build();

        initializeListView();
        loadBannerAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeInterstitialAd();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onClick(Bank bank) {
        mBank = bank;
        if (mInterstitialAd == null) {
            startActivity(getActivityIntent());
        } else {
            mInterstitialAd.show(this);
        }
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

    private void loadBannerAd() {
        AdSize adSize = getAdSize(this, binding.adViewContainer.getWidth(), getResources().getDisplayMetrics().density);
        AdView adView = new AdView(this);
        binding.adViewContainer.addView(adView);
        adView.setAdSize(adSize);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adView.loadAd(adRequest);
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

    private Intent getActivityIntent() {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(ARG_CONTACTS, (Serializable) mBank.getContacts());
        intent.putExtra(ARG_TITLE, mBank.getName());
        return intent;
    }
}
