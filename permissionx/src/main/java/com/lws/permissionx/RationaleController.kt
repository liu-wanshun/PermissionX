package com.lws.permissionx

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.lws.permissionx.PermissionX.defaultConfig

internal class RationaleController(
    private val permissionBuilder: PermissionBuilder,
    private val context: Context,
    private val permissions: Array<out String>
) {
    var requestRationale: CharSequence? = null
    var deniedRationale: CharSequence? = null
    var deniedForeverRationale: CharSequence? = null
    var deniedRationaleNegativeListener: Runnable? = null
    var deniedForeverRationaleNegativeListener: Runnable? = null

    fun hasRequestRationale(): Boolean {
        return requestRationale != null
    }

    private fun hasDeniedRationale(): Boolean {
        return deniedRationale != null
    }

    private fun hasDeniedForeverRationale(): Boolean {
        return deniedForeverRationale != null
    }

    fun showRequestRationale() {
        AlertDialog.Builder(context, defaultConfig.alertDialogTheme)
            .setCancelable(false)
            .setMessage(requestRationale)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, which: Int ->
                permissionBuilder.requestPermissions()
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, which: Int ->
                val result: MutableMap<String, Boolean> = LinkedHashMap()
                for (permission in permissions) {
                    result[permission] = PermissionX.hasPermissions(context, permission)
                }
                permissionBuilder.handleResult(result, true)
            }
            .create()
            .showWithGravity()
    }

    fun showDeniedRationale() {
        if (!hasDeniedRationale()) {
            return
        }
        AlertDialog.Builder(context, defaultConfig.alertDialogTheme)
            .setCancelable(false)
            .setMessage(deniedRationale)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface?, which: Int ->
                permissionBuilder.requestPermissions()
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, which: Int ->
                deniedRationaleNegativeListener?.run()
            }
            .create()
            .showWithGravity()

    }

    fun showDeniedForeverRationale() {
        if (!hasDeniedForeverRationale()) {
            return
        }
        AlertDialog.Builder(context, defaultConfig.alertDialogTheme)
            .setCancelable(false)
            .setMessage(deniedForeverRationale)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, which: Int ->
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }.also {
                    context.startActivity(it)
                }
            }
            .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, which: Int ->
                deniedForeverRationaleNegativeListener?.run()
            }
            .create()
            .showWithGravity()

    }


    private fun AlertDialog.showWithGravity() {
        show()
        window?.setGravity(defaultConfig.gravity)
    }
}