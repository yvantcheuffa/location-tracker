package com.locationtracker.two.ui.streetview;

import static com.locationtracker.two.Utils.getAdSize;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.locationtracker.two.R;
import com.locationtracker.two.databinding.ActivityStreetViewBinding;

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
        loadBannerAd();
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

    private void loadBannerAd() {
        AdSize adSize = getAdSize(this, binding.adViewContainer.getWidth(), getResources().getDisplayMetrics().density);
        AdView adView = new AdView(this);
        binding.adViewContainer.addView(adView);
        adView.setAdSize(adSize);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adView.loadAd(new AdRequest.Builder().build());
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