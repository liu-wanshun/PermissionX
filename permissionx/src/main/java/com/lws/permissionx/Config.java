package com.lws.permissionx;

import android.view.Gravity;

import androidx.annotation.IntDef;
import androidx.annotation.StyleRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author lws
 */
public class Config {

    @StyleRes
    private int alertDialogTheme;
    @GravityFlags
    private int gravity = Gravity.CENTER;

    Config() {
    }


    @StyleRes
    public int getAlertDialogTheme() {
        return alertDialogTheme;
    }

    public Config setAlertDialogTheme(@StyleRes int alertDialogTheme) {
        this.alertDialogTheme = alertDialogTheme;
        return this;
    }

    @GravityFlags
    public int getGravity() {
        return gravity;
    }

    public Config setGravity(@GravityFlags int gravity) {
        this.gravity = gravity;
        return this;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(flag = true, value = {
            Gravity.FILL,
            Gravity.FILL_HORIZONTAL,
            Gravity.FILL_VERTICAL,
            Gravity.START,
            Gravity.END,
            Gravity.LEFT,
            Gravity.RIGHT,
            Gravity.TOP,
            Gravity.BOTTOM,
            Gravity.CENTER,
            Gravity.CENTER_HORIZONTAL,
            Gravity.CENTER_VERTICAL,
            Gravity.DISPLAY_CLIP_HORIZONTAL,
            Gravity.DISPLAY_CLIP_VERTICAL,
            Gravity.CLIP_HORIZONTAL,
            Gravity.CLIP_VERTICAL,
            Gravity.NO_GRAVITY
    })
    public @interface GravityFlags {
    }
}
