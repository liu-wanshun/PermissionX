package com.lws.permissionx

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

open class DefaultRationaleFactory : RationaleFactory {
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

        return AlertDialog.Builder(context, R.style.PermissionDialogTheme_Bottom)
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
}