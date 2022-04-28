package com.lws.permissionx

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.lws.permissionx.PermissionX.hasPermissions

/**
 * @author lws
 */
class PermissionBuilder internal constructor(
    private val activity: FragmentActivity,
    private val fragment: Fragment?,
    private val permissions: Array<String>
) {
    private val rationaleController = RationaleController(this, activity, permissions)

    private var permissionResultCallback: PermissionResultCallback? = null

    private val fragmentManager: FragmentManager
        get() = fragment?.childFragmentManager ?: activity.supportFragmentManager


    private val invisibleFragment: InvisibleFragment
        get() {
            val fragment = fragmentManager.findFragmentByTag(InvisibleFragment.TAG)
            return if (fragment is InvisibleFragment) {
                fragment
            } else {
                val invisibleFragment = InvisibleFragment()
                fragmentManager.beginTransaction().add(invisibleFragment, InvisibleFragment.TAG)
                    .commitNowAllowingStateLoss()
                invisibleFragment
            }
        }

    /**
     * 建议请求权限前进行解释权限使用原因
     *
     * @param permissionResultCallback 请求权限的结果回调
     */
    fun request(permissionResultCallback: PermissionResultCallback) {
        this.permissionResultCallback = permissionResultCallback
        if (hasPermissions(activity, *permissions)) {
            val permissionResult = PermissionResult()
            for (permission in permissions) {
                permissionResult.addGranted(permission)
            }
            permissionResultCallback.onPermissionResult(permissionResult)
        } else {
            if (shouldShowPermissionRationale() && rationaleController.hasRequestRationale()) {
                rationaleController.showRequestRationale()
            } else {
                requestPermissions()
            }
        }
    }

    @JvmSynthetic
    internal fun requestPermissions() {
        invisibleFragment.request(this, permissions)
        getFirstDeniedPermission(permissions)?.let { markRequest(it) }
    }

    @JvmSynthetic
    internal fun handleResult(result: Map<String, Boolean>, onCancel: Boolean) {
        val permissionResult = PermissionResult()
        for (permission in result.keys) {
            if (java.lang.Boolean.TRUE == result[permission]) {
                permissionResult.addGranted(permission)
            } else {
                permissionResult.addDenied(permission)
            }
        }
        permissionResultCallback?.onPermissionResult(permissionResult)

        //处理拒绝的情况
        if (onCancel || hasPermissions(activity, *permissions)) {
            return
        }
        if (shouldShowPermissionRationale()) {
            rationaleController.showDeniedRationale()
        } else {
            rationaleController.showDeniedForeverRationale()
        }

    }

    /**
     * 第一次请求，或系统认为需要解释
     */
    private fun shouldShowPermissionRationale(): Boolean {
        val permission = getFirstDeniedPermission(permissions)
        return permission != null && (isFirstRequest(permission) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
    }

    private fun isFirstRequest(permission: String): Boolean {
        return activity.getPreferences().getBoolean(permission, true)
    }

    private fun markRequest(permission: String) {
        activity.getPreferences().edit {
            putBoolean(permission, false)
        }
    }


    private fun getFirstDeniedPermission(permissions: Array<String>): String? {
        for (permission in permissions) {
            if (!hasPermissions(activity, permission)) {
                return permission
            }
        }
        return null
    }

    /**
     * @param rationaleMsg 解释使用权限的原因，同意后将进行请求权限
     * @return this
     */
    @CheckResult
    fun onRequestRationale(rationaleMsg: CharSequence): PermissionBuilder {
        rationaleController.requestRationale = rationaleMsg
        return this
    }

    /**
     * @param rationaleRes 解释使用权限的原因，同意后将进行请求权限
     * @return this
     */
    @CheckResult
    fun onRequestRationale(@StringRes rationaleRes: Int): PermissionBuilder {
        return onRequestRationale(activity.getText(rationaleRes))
    }

    /**
     * @param rationale  用户拒绝权限，进行解释，同意将再次请求权限
     * @param negativeListener 用户不认可此解释时,执行此操作
     * @return this
     */
    @CheckResult
    @JvmOverloads
    fun onDeniedRationale(
        rationale: CharSequence,
        negativeListener: Runnable? = null
    ): PermissionBuilder {
        rationaleController.run {
            deniedRationale = rationale
            deniedRationaleNegativeListener = negativeListener
        }
        return this
    }

    /**
     * @param rationaleRes     用户拒绝权限，进行解释，同意将再次请求权限
     * @param negativeListener 用户不认可此解释时,执行此操作
     * @return this
     */
    @CheckResult
    @JvmOverloads
    fun onDeniedRationale(
        @StringRes rationaleRes: Int,
        negativeListener: Runnable? = null
    ): PermissionBuilder {
        return onDeniedRationale(activity.getText(rationaleRes), negativeListener)
    }

    /**
     * @param rationale 用户永久拒绝权限，进行解释，同意将跳转设置界面让用户自己开启权限
     * @param negativeListener       用户不认可此解释时,执行此操作
     * @return this
     */
    @CheckResult
    @JvmOverloads
    fun onDeniedForeverRationale(
        rationale: CharSequence,
        negativeListener: Runnable? = null
    ): PermissionBuilder {
        rationaleController.run {
            deniedForeverRationale = rationale
            deniedForeverRationaleNegativeListener = negativeListener
        }

        return this
    }

    /**
     * @param rationaleRes     用户永久拒绝权限，进行解释，同意将跳转设置界面让用户自己开启权限
     * @param negativeListener 用户不认可此解释时,执行此操作
     * @return this
     */
    @CheckResult
    @JvmOverloads
    fun onDeniedForeverRationale(
        @StringRes rationaleRes: Int,
        negativeListener: Runnable? = null
    ): PermissionBuilder {
        return onDeniedForeverRationale(activity.getText(rationaleRes), negativeListener)
    }

}

private fun Context.getPreferences(): SharedPreferences {
    return getSharedPreferences(PermissionX.javaClass.simpleName, Context.MODE_PRIVATE)
}