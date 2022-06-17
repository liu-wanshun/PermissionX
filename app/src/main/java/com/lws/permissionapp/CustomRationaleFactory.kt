package com.lws.permissionapp

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import com.lws.permissionx.RationaleFactory

class CustomRationaleFactory : RationaleFactory {
    override fun createRationale(
        context: Context,
        permissions: Array<String>,
        rationaleMsg: CharSequence?
    ): Dialog {

        return AlertDialog.Builder(context)
            .setMessage(rationaleMsg)
            .create()
    }

    override fun createDeniedForeverRationale(
        context: Context,
        permissions: Array<String>,
        positive: Runnable,
        negative: Runnable
    ): Dialog {
        return AlertDialog.Builder(context)
            .setMessage("为正常使用该功能，请前往系统应用设置中开启xxx权限")
            .setNegativeButton("不允许") { dialog, which ->
                negative.run()
            }
            .setPositiveButton("去设置") { dialog, which ->
                positive.run()
            }.create().apply {
                window?.setGravity(Gravity.BOTTOM)
            }
    }
}