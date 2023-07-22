package com.locationtracker.one.ui.location;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.locationtracker.one.Utils.getAdSize;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.locationtracker.one.R;
import com.locationtracker.one.databinding.ActivityUserLocationBinding;

public class UserLocationActivity extends AppCompatActivity {

    private ActivityUserLocationBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.user_location));

        binding = ActivityUserLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestLocationPermission();
        loadBannerAd();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return (super.onOptionsItemSelected(item));
    }

    private void loadBannerAd() {
        AdSize adSize = getAdSize(this, binding.adViewContainer.getWidth(), getResources().getDisplayMetrics().density);
        AdView adView = new AdView(this);
        binding.adViewContainer.addView(adView);
        adView.setAdSize(adSize);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adView.loadAd(new AdRequest.Builder().build());
    }


    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        if (!hasLocationPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        }
    }
}