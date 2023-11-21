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
class PermissionRationale {

    private val activity: ComponentActivity
    private var fragment: Fragment? = null

    internal constructor(activity: ComponentActivity) {
        this.activity = activity
    }

    internal constructor(fragment: Fragment) {
        this.fragment = fragment
        activity = fragment.requireActivity()
    }


    /**
     * 填写需要申请的权限
     *
     * @param permissions 需要申请的权限,应该是 [android.Manifest.permission] 中的权限，如果填写国内ROM定制增加的权限，请务必在请求前检查权限是否存在，如果权限不存在，可能一直返回拒绝的结果
     * @return MultiplePermissionBuilder
     */
    @CheckResult
    fun permissions(vararg permissions: String): PermissionRationaleBuilder {
        return PermissionRationaleBuilder(activity, fragment, permissions as Array<String>)
    }

    companion object {

        @CheckResult
        @JvmStatic
        fun with(activity: ComponentActivity): PermissionRationale {
            return PermissionRationale(activity)
        }

        @CheckResult
        @JvmStatic
        fun with(fragment: Fragment): PermissionRationale {
            return PermissionRationale(fragment)
        }

        @JvmStatic
        fun hasPermissions(context: Context, vararg permissions: String): Boolean {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context, permission
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
}