package com.locationtracker.two.ui.siminfo;

import static com.locationtracker.two.Utils.getAdSize;
import static com.locationtracker.two.Utils.loadFileContent;
import static com.locationtracker.two.ui.contact.ContactActivity.ARG_CONTACTS;
import static com.locationtracker.two.ui.contact.ContactActivity.ARG_TITLE;

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
import com.locationtracker.two.R;
import com.locationtracker.two.databinding.ActivitySimInfoBinding;
import com.locationtracker.two.model.SIMInfo;
import com.locationtracker.two.ui.contact.ContactActivity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SIMInfoActivity extends AppCompatActivity implements SIMInfoAdapter.OnSIMInfoClickListener {

    private ActivitySimInfoBinding binding;
    private SIMInfo mSimInfo;

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

        binding = ActivitySimInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.sim_details));

        adRequest = new AdRequest.Builder().build();

        initializeGridView();
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
    public void onClick(SIMInfo simInfo) {
        mSimInfo = simInfo;
        if (mInterstitialAd == null) {
            startActivity(getActivityIntent());
        } else {
            mInterstitialAd.show(this);
        }
    }

    private ArrayList<SIMInfo> getSimInfoList() {
        String jsonContent = loadFileContent("sim_info.json", getAssets());
        Type listType = new TypeToken<ArrayList<SIMInfo>>() {
        }.getType();
        return new Gson().fromJson(jsonContent, listType);
    }

    private void loadBannerAd() {
        AdSize adSize = getAdSize(this, binding.adViewContainer.getWidth(), getResources().getDisplayMetrics().density);
        AdView adView = new AdView(this);
        binding.adViewContainer.addView(adView);
        adView.setAdSize(adSize);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adView.loadAd(new AdRequest.Builder().build());
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
        ArrayList<SIMInfo> codes = getSimInfoList();
        SIMInfoAdapter adapter = new SIMInfoAdapter(this, codes, this);
        binding.simInfoGridView.setAdapter(adapter);
    }

    private Intent getActivityIntent() {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(ARG_CONTACTS, (Serializable) mSimInfo.getContacts());
        intent.putExtra(ARG_TITLE, String.format("%s Codes", mSimInfo.getName()));
        return intent;
    }
}