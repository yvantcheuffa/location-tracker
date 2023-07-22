package com.locationtracker.one.ui.contact;

import static android.Manifest.permission.CALL_PHONE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
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
import com.locationtracker.one.R;
import com.locationtracker.one.databinding.ActivityContactBinding;
import com.locationtracker.one.model.Contact;
import com.locationtracker.one.ui.contact.ContactAdapter.OnContactClickListener;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity implements OnContactClickListener {
    public static final String ARG_CONTACTS = "ARG_CONTACTS";
    public static final String ARG_TITLE = "ARG_TITLE";
    private ActivityContactBinding binding;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private Contact mCurrentContact;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new RequestPermission(), isGranted -> {
                if (isGranted) {
                    makeCall();
                } else {
                    Toast.makeText(this, getString(R.string.enable_call_permission), Toast.LENGTH_SHORT).show();
                }
            }
    );
    private final FullScreenContentCallback interstitialFullscreenCallback = new FullScreenContentCallback() {
        @Override
        public void onAdDismissedFullScreenContent() {
            mInterstitialAd = null;
            makeCall();
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            makeCall();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<Contact> contacts = (ArrayList<Contact>) getIntent().getSerializableExtra(ARG_CONTACTS);
        String title = getIntent().getStringExtra(ARG_TITLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        adRequest = new AdRequest.Builder().build();
        initializeListView(contacts);
        initializeBannerAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeInterstitialAd();
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
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(interstitialFullscreenCallback);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                }
        );
    }

    private void initializeListView(ArrayList<Contact> contacts) {
        ContactAdapter adapter = new ContactAdapter(this, contacts, this);
        binding.contactListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onClick(Contact contact) {
        mCurrentContact = contact;
        if (mInterstitialAd == null) {
            if (hasPhoneCallPermission()) {
                makeCall();
            } else {
                requestPermissionLauncher.launch(CALL_PHONE);
            }
        } else {
            mInterstitialAd.show(this);
        }
    }

    private boolean hasPhoneCallPermission() {
        return ActivityCompat.checkSelfPermission(this, CALL_PHONE) == PERMISSION_GRANTED;
    }

    private void makeCall() {
        if (hasPhoneCallPermission()) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Uri.encode(mCurrentContact.getNumber())));
            startActivity(intent);
        } else {
            requestPermissionLauncher.launch(CALL_PHONE);
        }
    }
}