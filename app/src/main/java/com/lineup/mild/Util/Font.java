package com.lineup.mild.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;

@SuppressLint("Registered")
public class Font {

    public static int CHAPAZA = 1;
    public static int NUNITO_SANS = 2;

    public Activity activity;

    public Font(Activity activity) {
        this.activity = activity;
    }

    public Typeface chapaza() {

        return Typeface.createFromAsset(activity.getAssets(), "fonts/Chapaza.ttf");
    }

    public Typeface nunitoSans() {

        return Typeface.createFromAsset(activity.getAssets(), "fonts/NunitoSans-Light.ttf");
    }
}
