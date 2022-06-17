package com.lws.permissionx

import android.app.Dialog
import android.content.Context

interface RationaleFactory {

    /**
     * 请求权限时，该解释显示在上方
     */
    fun createRationale(
        context: Context,
        permissions: Array<String>,
        rationaleMsg: CharSequence?
    ): Dialog

    /**
     * 永久拒绝权限时，显示该引导
     *
     * @param positive 前往系统应用设置页面
     * @param negative 拒绝申请
     */
    fun createDeniedForeverRationale(
        context: Context,
        permissions: Array<String>,
        positive: Runnable,
        negative: Runnable
    ): Dialog
}