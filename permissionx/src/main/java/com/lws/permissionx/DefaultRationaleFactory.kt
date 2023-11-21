package com.lws.permissionx

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentDialog

open class DefaultRationaleFactory : RationaleFactory {
    override fun createRationale(
        context: Context,
        permissions: Array<String>,
        rationaleMsg: CharSequence?
    ): Dialog {
        return object : ComponentDialog(context, R.style.PermissionDialogTheme) {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.permissionx_rationale)
                val namesString = StringBuilder().apply {
                    buildPermissionNames(permissions, context).joinTo(this)
                }
                findViewById<ImageView>(R.id.app_icon).setImageResource(context.applicationInfo.icon)
                findViewById<TextView>(R.id.title).text = context.getString(
                    R.string.permissionx_default_rationale_title,
                    context.applicationInfo.loadLabel(context.packageManager),
                    namesString
                )
                findViewById<TextView>(R.id.message).text = rationaleMsg
            }
        }
    }

    override fun createDeniedForeverRationale(
        context: Context,
        permissions: Array<String>,
        positive: Runnable,
        negative: Runnable
    ): Dialog {

        return object : ComponentDialog(context, R.style.PermissionDialogTheme_Bottom) {
            init {
                window?.setGravity(Gravity.BOTTOM)
            }

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.permissionx_denied_rationale)
                val namesString = StringBuilder().apply {
                    buildPermissionNames(permissions, context).joinTo(this)
                }
                findViewById<ImageView>(R.id.app_icon).setImageResource(context.applicationInfo.icon)
                findViewById<TextView>(R.id.app_name).text =
                    context.applicationInfo.loadLabel(context.packageManager)

                findViewById<TextView>(R.id.title).text = context.getString(
                    R.string.permissionx_default_denied_forever_rationale_title,
                    namesString
                )
                findViewById<TextView>(R.id.message).text =
                    context.getString(
                        R.string.permissionx_default_denied_forever_rationale_message,
                        namesString
                    )

                findViewById<Button>(android.R.id.button1).apply {
                    text =
                        context.getText(R.string.permissionx_default_denied_forever_rationale_positive)
                    setOnClickListener {
                        positive.run()
                        dismiss()
                    }
                }

                findViewById<Button>(android.R.id.button2).apply {
                    text =
                        context.getText(R.string.permissionx_default_denied_forever_rationale_negative)
                    setOnClickListener {
                        negative.run()
                        dismiss()
                    }
                }
            }
        }
    }
}