package com.locationtracker.one.ui.contact;

import static android.Manifest.permission.CALL_PHONE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
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
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<Contact> contacts = (ArrayList<Contact>) getIntent().getSerializableExtra(ARG_CONTACTS);
        String title = getIntent().getStringExtra(ARG_TITLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        initializeListView(contacts);
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
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                checkPermissions();
                            }
                        });
                        interstitialAd.show(ContactActivity.this);
                    }
                }
        );
    }

    private void checkPermissions() {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, CALL_PHONE) != PERMISSION_GRANTED) {
                requestPermissions(new String[]{CALL_PHONE}, PERMISSION_REQUEST_CODE);
            }
        }
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
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, CALL_PHONE) != PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.enable_call_permission), Toast.LENGTH_SHORT).show();
            } else {
                makeCall(contact.getNumber());
            }
        } else {
            makeCall(contact.getNumber());
        }
    }

    private void makeCall(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Uri.encode(number)));
        startActivity(intent);
    }
}