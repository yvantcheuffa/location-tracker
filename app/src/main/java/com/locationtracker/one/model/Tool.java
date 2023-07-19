package com.locationtracker.one.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class Tool {
    private @StringRes Integer nameResId;
    private @DrawableRes Integer iconResId;

    public Tool(Integer nameResId, Integer iconResId) {
        this.nameResId = nameResId;
        this.iconResId = iconResId;
    }

    public Integer getNameResId() {
        return nameResId;
    }

    public void setNameResId(Integer nameResId) {
        this.nameResId = nameResId;
    }

    public Integer getIconResId() {
        return iconResId;
    }

    public void setIconResId(Integer iconResId) {
        this.iconResId = iconResId;
    }
}
