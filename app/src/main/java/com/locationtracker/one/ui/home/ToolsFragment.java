package com.locationtracker.one.ui.home;

import static com.locationtracker.one.Utils.getAdSize;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.locationtracker.one.R;
import com.locationtracker.one.databinding.FragmentToolsBinding;
import com.locationtracker.one.model.Tool;
import com.locationtracker.one.ui.bank.BankActivity;
import com.locationtracker.one.ui.directionmap.DirectionMapActivity;
import com.locationtracker.one.ui.location.UserLocationActivity;
import com.locationtracker.one.ui.nearbyplaces.NearbyPlacesActivity;
import com.locationtracker.one.ui.numberlocation.NumberLocationActivity;
import com.locationtracker.one.ui.rechargedetails.RechargeDetailsActivity;
import com.locationtracker.one.ui.siminfo.SIMInfoActivity;
import com.locationtracker.one.ui.streetview.StreetViewActivity;
import com.locationtracker.one.ui.ussd.USSDCodeActivity;

import java.util.ArrayList;
import java.util.List;

public class ToolsFragment extends Fragment implements ToolAdapter.OnToolClickListener {

    private FragmentToolsBinding binding;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;
    private Class<? extends AppCompatActivity> mNextActivityClass;
    private Tool mCurrentTool;

    private final FullScreenContentCallback interstitialFullscreenCallback = new FullScreenContentCallback() {
        @Override
        public void onAdDismissedFullScreenContent() {
            mInterstitialAd = null;
            startActivity(new Intent(requireContext(), mNextActivityClass));
            mNextActivityClass = null;
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            startActivityByTool(mCurrentTool);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentToolsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adRequest = new AdRequest.Builder().build();
        initializeMenu();
        loadBannerAd();
    }

    private void loadBannerAd() {
        AdSize adSize = getAdSize(requireActivity(), binding.adViewContainer.getWidth(), getResources().getDisplayMetrics().density);
        AdView adView = new AdView(requireContext());
        binding.adViewContainer.addView(adView);
        adView.setAdSize(adSize);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeInterstitialAd();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(Tool tool) {
        mCurrentTool = tool;
        setNextActivity();
        if (mInterstitialAd == null) {
            startActivityByTool(tool);
        } else {
            mInterstitialAd.show(requireActivity());
        }
    }

    private void setNextActivity() {
        if (mCurrentTool.getIconResId() == R.drawable.ic_number_location) {
            mNextActivityClass = NumberLocationActivity.class;
        } else if (mCurrentTool.getIconResId() == R.drawable.ic_direction_map) {
            mNextActivityClass = DirectionMapActivity.class;
        } else if (mCurrentTool.getIconResId() == R.drawable.ic_street_view) {
            mNextActivityClass = StreetViewActivity.class;
        } else if (mCurrentTool.getIconResId() == R.drawable.ic_nearby_places) {
            mNextActivityClass = NearbyPlacesActivity.class;
        } else if (mCurrentTool.getIconResId() == R.drawable.ic_user_location) {
            mNextActivityClass = UserLocationActivity.class;
        } else if (mCurrentTool.getIconResId() == R.drawable.ic_recharge_plans) {
            mNextActivityClass = RechargeDetailsActivity.class;
        } else if (mCurrentTool.getIconResId() == R.drawable.ic_sim_info) {
            mNextActivityClass = SIMInfoActivity.class;
        } else if (mCurrentTool.getIconResId() == R.drawable.ic_bank_info) {
            mNextActivityClass = BankActivity.class;
        } else if (mCurrentTool.getIconResId() == R.drawable.ic_ussd_codes) {
            mNextActivityClass = USSDCodeActivity.class;
        }
    }

    private void startActivityByTool(Tool tool) {
        if (tool.getIconResId() == R.drawable.ic_number_location) {
            startActivity(new Intent(requireContext(), NumberLocationActivity.class));
        } else if (tool.getIconResId() == R.drawable.ic_direction_map) {
            startActivity(new Intent(requireContext(), DirectionMapActivity.class));
        } else if (tool.getIconResId() == R.drawable.ic_street_view) {
            startActivity(new Intent(requireContext(), StreetViewActivity.class));
        } else if (tool.getIconResId() == R.drawable.ic_nearby_places) {
            startActivity(new Intent(requireContext(), NearbyPlacesActivity.class));
        } else if (tool.getIconResId() == R.drawable.ic_user_location) {
            startActivity(new Intent(requireContext(), UserLocationActivity.class));
        } else if (tool.getIconResId() == R.drawable.ic_recharge_plans) {
            startActivity(new Intent(requireContext(), RechargeDetailsActivity.class));
        } else if (tool.getIconResId() == R.drawable.ic_sim_info) {
            startActivity(new Intent(requireContext(), SIMInfoActivity.class));
        } else if (tool.getIconResId() == R.drawable.ic_bank_info) {
            startActivity(new Intent(requireContext(), BankActivity.class));
        } else if (tool.getIconResId() == R.drawable.ic_ussd_codes) {
            startActivity(new Intent(requireContext(), USSDCodeActivity.class));
        }
    }

    private ArrayList<Tool> getTools() {
        return new ArrayList<>(List.of(
                new Tool(R.string.sim_info, R.drawable.ic_sim_info),
                new Tool(R.string.bank_info, R.drawable.ic_bank_info),
                new Tool(R.string.ussd_codes, R.drawable.ic_ussd_codes),
                new Tool(R.string.recharge_plans, R.drawable.ic_recharge_plans),
                new Tool(R.string.user_location, R.drawable.ic_user_location),
                new Tool(R.string.direction_map, R.drawable.ic_direction_map),
                new Tool(R.string.street_view, R.drawable.ic_street_view),
                new Tool(R.string.near_by_places, R.drawable.ic_nearby_places),
                new Tool(R.string.number_location, R.drawable.ic_number_location)
        ));
    }

    private void initializeMenu() {
        ArrayList<Tool> tools = getTools();
        ToolAdapter adapter = new ToolAdapter(requireContext(), tools, this);
        binding.toolGridView.setAdapter(adapter);
    }

    private void initializeInterstitialAd() {
        InterstitialAd.load(
                requireContext(),
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
}