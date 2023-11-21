package com.lws.permissionrationale.internal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build

/**
 * @author lws
 */
internal class OrientationHelper(private val activity: Activity) {
    private var originRequestOrientation = 0
    fun restoreOrientation() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            return
        }
        activity.requestedOrientation = originRequestOrientation
    }

    @SuppressLint("SourceLockedOrientationActivity")
    fun lockOrientation() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            return
        }
        originRequestOrientation = activity.requestedOrientation
        val orientation = activity.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }
    }
}