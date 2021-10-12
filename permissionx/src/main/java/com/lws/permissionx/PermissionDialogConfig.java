package com.lws.permissionx;

import android.view.Gravity;

import androidx.annotation.StyleRes;

/**
 * @author lws
 */
public class PermissionDialogConfig {


    @StyleRes
    private static int defaultDialogTheme;
    private static boolean alertDialogAndroidX = true;
    private static int gravity = Gravity.CENTER;

    public static int getDefaultDialogTheme() {
        return defaultDialogTheme;
    }

    public static void setDefaultDialogTheme(@StyleRes int dialogTheme) {
        defaultDialogTheme = dialogTheme;
    }

    public static boolean isAlertDialogAndroidX() {
        return alertDialogAndroidX;
    }

    public static void setAlertDialogAndroidX(boolean androidX) {
        alertDialogAndroidX = androidX;
    }

    public static int getGravity() {
        return gravity;
    }

    public static void setGravity(int gravity) {
        PermissionDialogConfig.gravity = gravity;
    }
}
