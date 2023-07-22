package com.locationtracker.one.ui.ussd;

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
import com.google.android.gms.ads.FullScreenContentCallback;
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
    private USSDCode mUssdCode;

    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;

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

        binding = ActivityUssdCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ussd_codes));

        adRequest = new AdRequest.Builder().build();

        initializeGridView();
        initializeBannerAd();
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
    public void onClick(USSDCode ussdCode) {
        mUssdCode = ussdCode;
        if (mInterstitialAd == null) {
            startActivity(getActivityIntent());
        } else {
            mInterstitialAd.show(this);
        }
    }

    private void initializeBannerAd() {
        MobileAds.initialize(this, initializationStatus -> {
        });
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
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(interstitialFullscreenCallback);
                    }
                }
        );
    }

    private void initializeGridView() {
        ArrayList<USSDCode> codes = getUSSDCodes();
        USSDCodeAdapter adapter = new USSDCodeAdapter(this, codes, this);
        binding.ussdGridView.setAdapter(adapter);
    }

    private ArrayList<USSDCode> getUSSDCodes() {
        String jsonContent = loadFileContent("ussd_codes.json", getAssets());
        Type listType = new TypeToken<ArrayList<USSDCode>>() {
        }.getType();
        return new Gson().fromJson(jsonContent, listType);
    }

    private Intent getActivityIntent() {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(ARG_CONTACTS, (Serializable) mUssdCode.getContacts());
        intent.putExtra(ARG_TITLE, String.format("%s USSD Codes", mUssdCode.getName()));
        return intent;
    }
}