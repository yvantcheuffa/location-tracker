package com.locationtracker.one;

import android.content.res.AssetManager;

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
}
