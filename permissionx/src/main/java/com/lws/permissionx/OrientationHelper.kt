package com.lws.permissionx

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration

/**
 * @author lws
 */
internal class OrientationHelper(private val activity: Activity) {
    private var originRequestOrientation = 0
    fun restoreOrientation() {
        activity.requestedOrientation = originRequestOrientation
    }

    @SuppressLint("SourceLockedOrientationActivity")
    fun lockOrientation() {
        originRequestOrientation = activity.requestedOrientation
        val orientation = activity.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }
    }
}