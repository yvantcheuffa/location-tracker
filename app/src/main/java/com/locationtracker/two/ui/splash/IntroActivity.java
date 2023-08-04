package com.locationtracker.two.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.locationtracker.two.R;
import com.locationtracker.two.databinding.ActivityIntroBinding;
import com.locationtracker.two.ui.home.MainActivity;


public class IntroActivity extends AppCompatActivity {
    private AppOpenAd mAppOpenAd;
    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = () -> {
        if (mAppOpenAd != null) {
            mAppOpenAd.show(IntroActivity.this);
        } else {
            finish();
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityIntroBinding binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeOpenAd();

        mHandler.postDelayed(mRunnable, 4000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    private void initializeOpenAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AppOpenAd.load(
                this,
                getString(R.string.open_ad_unit_id),
                adRequest,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                        mAppOpenAd = appOpenAd;
                        mAppOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                mAppOpenAd = null;
                                finish();
                                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                mAppOpenAd = null;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mAppOpenAd = null;
                    }
                }
        );
    }
}
