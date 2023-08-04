package com.locationtracker.two;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Insets;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowInsets;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdSize;

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static String loadFileContent(String filepath, AssetManager assetManager) {
        try {
            InputStream stream = assetManager.open(filepath);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            return new String(buffer);
        } catch (IOException exception) {
            return null;
        }
    }

    private static int getScreenWidth(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }

    public static AdSize getAdSize(@NonNull Activity activity, float containerWidthPixels, float density) {
        int defaultWidth = getScreenWidth(activity);
        float adWidthPixels = containerWidthPixels;

        if (adWidthPixels == 0f) {
            adWidthPixels = defaultWidth;
        }

        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }
}
