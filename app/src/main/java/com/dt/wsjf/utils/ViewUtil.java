package com.dt.wsjf.utils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;


public class ViewUtil {

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void setBackground(View view, Drawable drawable) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
