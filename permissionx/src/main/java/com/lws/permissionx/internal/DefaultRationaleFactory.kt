package com.lws.permissionx.internal

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.lws.permissionx.*


internal object DefaultRationaleFactory : RationaleFactory {
    override fun createRationale(
        context: Context,
        permissions: Array<String>,
        rationaleMsg: CharSequence?
    ): Dialog {

        val namesString = StringBuilder().apply {
            buildPermissionNames(permissions, context).joinTo(this)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.permissionx_rationale, null)

        view.findViewById<ImageView>(R.id.app_icon).setImageResource(context.applicationInfo.icon)
        view.findViewById<TextView>(R.id.title).text = context.getString(
            R.string.permissionx_default_rationale_title,
            context.applicationInfo.loadLabel(context.packageManager),
            namesString
        )

        view.findViewById<TextView>(R.id.message).text = rationaleMsg
        return AlertDialog.Builder(context, R.style.PermissionDialogTheme)
            .setView(view)
            .create()
    }

    override fun createDeniedForeverRationale(
        context: Context,
        permissions: Array<String>,
        positive: Runnable,
        negative: Runnable
    ): Dialog {

        val namesString = StringBuilder().apply {
            buildPermissionNames(permissions, context).joinTo(this)
        }

        val view =
            LayoutInflater.from(context).inflate(R.layout.permissionx_denied_rationale, null)

        view.findViewById<ImageView>(R.id.app_icon).setImageResource(context.applicationInfo.icon)
        view.findViewById<TextView>(R.id.app_name).text =
            context.applicationInfo.loadLabel(context.packageManager)

        view.findViewById<TextView>(R.id.title).text = context.getString(
            R.string.permissionx_default_denied_forever_rationale_title,
            namesString
        )
        view.findViewById<TextView>(R.id.message).text =
            context.getString(
                R.string.permissionx_default_denied_forever_rationale_message,
                namesString
            )

        return AlertDialog.Builder(context, R.style.PermissionDialogTheme)
            .setView(view)
            .setNegativeButton(R.string.permissionx_default_denied_forever_rationale_negative) { dialog, which ->
                negative.run()
            }
            .setPositiveButton(R.string.permissionx_default_denied_forever_rationale_positive) { dialog, which ->
                positive.run()
            }.create().apply {
                window?.setGravity(Gravity.BOTTOM)
            }
    }

    private fun buildPermissionNames(
        permissions: Array<String>,
        context: Context
    ): Set<String> {
        val permissionNames = mutableSetOf<String>()
        val tempSet = HashSet<String>()
        val currentVersion = Build.VERSION.SDK_INT

        for (permission in permissions) {
            val permissionGroup = when {
                currentVersion < Build.VERSION_CODES.Q -> {
                    try {
                        val permissionInfo = context.packageManager.getPermissionInfo(permission, 0)
                        permissionInfo.group
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                        null
                    }
                }
                currentVersion == Build.VERSION_CODES.Q -> permissionMapOnQ[permission]
                currentVersion == Build.VERSION_CODES.R -> permissionMapOnR[permission]
                currentVersion == Build.VERSION_CODES.S -> permissionMapOnS[permission]
                else -> {
                    // If currentVersion is newer than the latest version we support, we just use
                    // the latest version for temp. Will upgrade in the next release.
                    permissionMapOnS[permission]
                }
            }
            if ((permissionGroup != null && !tempSet.contains(permissionGroup))
            ) {
                permissionNames.add(
                    context.getString(
                        context.packageManager
                            .getPermissionGroupInfo(permissionGroup, 0).labelRes
                    )
                )
                tempSet.add(permissionGroup)
            }
        }
        return permissionNames
    }
}