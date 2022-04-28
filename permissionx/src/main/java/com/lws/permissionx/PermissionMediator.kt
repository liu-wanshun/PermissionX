package com.lws.permissionx

import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * @author lws
 */
class PermissionMediator {
    private val activity: FragmentActivity
    private var fragment: Fragment? = null

    internal constructor(activity: FragmentActivity) {
        this.activity = activity
    }

    internal constructor(fragment: Fragment) {
        this.fragment = fragment
        activity = fragment.requireActivity()
    }

    /**
     * 填写需要申请的权限
     *
     * @param permissions 需要申请的权限
     * @return MultiplePermissionBuilder
     */
    @CheckResult
    fun permissions(vararg permissions: String): PermissionBuilder {
        return PermissionBuilder(activity, fragment, permissions as Array<String>)
    }
}