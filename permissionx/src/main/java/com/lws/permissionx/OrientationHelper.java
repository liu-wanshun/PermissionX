package com.lws.permissionx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

/**
 * @author lws
 */
class OrientationHelper {

    private final Activity activity;
    private int originRequestOrientation;

    OrientationHelper(Activity activity) {
        this.activity = activity;
    }

    void restoreOrientation() {
        activity.setRequestedOrientation(originRequestOrientation);
    }


    @SuppressLint("SourceLockedOrientationActivity")
    void lockOrientation() {
        originRequestOrientation = activity.getRequestedOrientation();
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }
}
