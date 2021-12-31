package com.lws.permissionx

import android.content.Context
import androidx.annotation.CheckResult
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * @author lws
 */
object PermissionX {
    @CheckResult
    @JvmStatic
    fun init(activity: FragmentActivity): PermissionMediator {
        return PermissionMediator(activity)
    }

    @CheckResult
    @JvmStatic
    fun init(fragment: Fragment): PermissionMediator {
        return PermissionMediator(fragment)
    }

    @JvmStatic
    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (PermissionChecker.checkSelfPermission(
                    context,
                    permission
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    val defaultConfig = Config()

}