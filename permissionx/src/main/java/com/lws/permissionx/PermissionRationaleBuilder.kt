package com.lws.permissionx

import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * @author lws
 */
class PermissionRationaleBuilder internal constructor(
    private val activity: FragmentActivity,
    private val fragment: Fragment?,
    private val permissions: Array<String>
) {

    @JvmSynthetic
    @JvmField
    internal var requestRationale: CharSequence? = null

    @JvmSynthetic
    @JvmField
    internal var deniedForeverRationale: CharSequence? = null

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
        invisibleFragment.request(this, permissions)
    }


    @JvmSynthetic
    internal fun callBackResult(result: PermissionResult) {
        permissionResultCallback?.onPermissionResult(result)
    }

    /**
     * @param rationaleMsg 解释使用权限的原因，同意后将进行请求权限
     * @return this
     */
    @CheckResult
    fun onRequestRationale(rationaleMsg: CharSequence): PermissionRationaleBuilder {
        requestRationale = rationaleMsg
        return this
    }

    /**
     * @param rationaleRes 解释使用权限的原因，同意后将进行请求权限
     * @return this
     */
    @CheckResult
    fun onRequestRationale(@StringRes rationaleRes: Int): PermissionRationaleBuilder {
        return onRequestRationale(activity.getText(rationaleRes))
    }

}