package com.lws.permissionx

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.annotation.CheckResult
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

/**
 * @author lws
 */
object PermissionX {
    @CheckResult
    @JvmStatic
    fun with(activity: ComponentActivity): PermissionRationaleEntry {
        return PermissionRationaleEntry(activity)
    }

    @CheckResult
    @JvmStatic
    fun with(fragment: Fragment): PermissionRationaleEntry {
        return PermissionRationaleEntry(fragment)
    }

    @JvmStatic
    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }


    @JvmField
    @JvmSynthetic
    internal var customRationaleFactory: RationaleFactory? = null

    /**
     * @param rationaleFactory 自定义解释弹框的创建工厂，设置null时即使用sdk默认配置
     */
    @JvmStatic
    fun setRationaleFactory(rationaleFactory: RationaleFactory?) = this.apply {
        customRationaleFactory = rationaleFactory
    }

}