package com.example.jaredfranze.hellow;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class EventIcon {

    //
    // Static
    //

    // Weather Icons

    public final int EIC_WEATHER_SUNNY = 0;
    public final int EIC_WEATHER_RAINY = 1;
    public final int EIC_WEATHER_STORM = 2;

    // Event Icons

    public final int EIC_EVENT_FOOD = 101;
    public final int EIC_EVENT_CART = 102;
    public final int EIC_EVENT_TICKET = 103;

    // Colors

    public final int EIC_COLOR_RED = Color.RED;
    public final int EIC_COLOR_GREEN = Color.GREEN;
    public final int EIC_COLOR_BLUE = Color.BLUE;
    public final int EIC_COLOR_WEATHER = Color.YELLOW;

    //
    // Instance
    //

    public boolean hasIcon;
    public int icon;
    public int color;

    public EventIcon(boolean hasIcon, int icon, int color) {
        this.hasIcon = hasIcon;
        this.icon = icon;
        this.color = color;
    }

    public Drawable getIconDrawable() {
        return null;
    }
}
