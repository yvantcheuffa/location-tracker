package com.locationtracker.one.ui.streetview;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.locationtracker.one.R;
import com.locationtracker.one.databinding.ActivityStreetViewBinding;

public class StreetViewActivity extends AppCompatActivity {

    private ActivityStreetViewBinding binding;
    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private final FullScreenContentCallback interstitialFullscreenCallback = new FullScreenContentCallback() {
        @Override
        public void onAdDismissedFullScreenContent() {
            mInterstitialAd = null;
            showErrorMessage();
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            showErrorMessage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStreetViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.street_view));

        adRequest = new AdRequest.Builder().build();

        setupListeners();
        initializeBannerAd();
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

    private void initializeBannerAd() {
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adBannerView.loadAd(adRequest);
    }

    private void setupListeners() {
        binding.btnFamousPlaces.setOnClickListener(view -> handleClick());
        binding.btnSevenWonders.setOnClickListener(view -> handleClick());
    }

    private void handleClick() {
        if (mInterstitialAd == null) {
            showErrorMessage();
        } else {
            mInterstitialAd.show(this);
        }
    }

    private void showErrorMessage() {
        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return (super.onOptionsItemSelected(item));
    }
}