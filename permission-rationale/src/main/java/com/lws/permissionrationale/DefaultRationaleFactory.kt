package com.lws.permissionrationale

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentDialog
import androidx.core.content.ContextCompat
import java.util.function.Consumer

open class DefaultRationaleFactory : RationaleFactory {
    override fun createRationale(
        context: Context,
        permissions: Array<String>,
        rationaleMsg: CharSequence?
    ): Dialog {
        return object : ComponentDialog(context, R.style.PermissionRationaleDialogTheme) {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.permissionrationale_rationale)
                val namesString = StringBuilder().apply {
                    buildPermissionNames(permissions, context).joinTo(this)
                }
                findViewById<ImageView>(R.id.app_icon).setImageResource(context.applicationInfo.icon)
                findViewById<TextView>(R.id.title).text = context.getString(
                    R.string.permissionrationale_default_rationale_title,
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

        return object : ComponentDialog(context, R.style.PermissionRationaleDialogTheme_Bottom) {
            init {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isTelevision(context)) {
                    val window = window!!
                    window.attributes = window.attributes.apply {
                        flags = flags or WindowManager.LayoutParams.FLAG_BLUR_BEHIND
                        blurBehindRadius = 20
                        format = PixelFormat.TRANSPARENT
                        verticalMargin = 0.03f
                        gravity = Gravity.BOTTOM
                    }

                    val mBackgroundWithBlur = ContextCompat.getDrawable(
                        context,
                        R.drawable.permissionrationale_background_with_blur
                    )
                    val mBackgroundNoBlur = ContextCompat.getDrawable(
                        context,
                        R.drawable.permissionrationale_background
                    )
                    val mBackgroundBlurRadius = 80
                    val blurEnabledListener =
                        Consumer<Boolean> { enabled: Boolean ->
                            window.setBackgroundDrawable(if (enabled) mBackgroundWithBlur else mBackgroundNoBlur)
                            window.setBackgroundBlurRadius(if (enabled) mBackgroundBlurRadius else 0)
                        }
                    blurEnabledListener.accept(false)
                    window.decorView.addOnAttachStateChangeListener(object :
                        View.OnAttachStateChangeListener {


                        override fun onViewAttachedToWindow(v: View) {
                            window.windowManager.addCrossWindowBlurEnabledListener(
                                blurEnabledListener
                            )
                        }

                        override fun onViewDetachedFromWindow(v: View) {
                            window.windowManager.removeCrossWindowBlurEnabledListener(
                                blurEnabledListener
                            )
                        }
                    })

                }
            }

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.permissionrationale_denied_rationale)
                val namesString = StringBuilder().apply {
                    buildPermissionNames(permissions, context).joinTo(this)
                }
                findViewById<ImageView>(R.id.app_icon).setImageResource(context.applicationInfo.icon)
                findViewById<TextView>(R.id.app_name).text =
                    context.applicationInfo.loadLabel(context.packageManager)

                findViewById<TextView>(R.id.title).text = context.getString(
                    R.string.permissionrationale_default_denied_forever_rationale_title,
                    namesString
                )
                findViewById<TextView>(R.id.message).text =
                    context.getString(
                        R.string.permissionrationale_default_denied_forever_rationale_message,
                        namesString
                    )

                findViewById<Button>(android.R.id.button1).apply {
                    text =
                        context.getText(R.string.permissionrationale_default_denied_forever_rationale_positive)
                    setOnClickListener {
                        positive.run()
                        dismiss()
                    }
                }

                findViewById<Button>(android.R.id.button2).apply {
                    text =
                        context.getText(R.string.permissionrationale_default_denied_forever_rationale_negative)
                    setOnClickListener {
                        negative.run()
                        dismiss()
                    }
                }
            }
        }
    }


    private fun isTelevision(context: Context): Boolean {
        val uiMode = context.resources.configuration.uiMode
        return uiMode and Configuration.UI_MODE_TYPE_MASK == Configuration.UI_MODE_TYPE_TELEVISION
    }
}