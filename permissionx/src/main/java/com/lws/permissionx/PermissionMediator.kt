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
     * @param permissions 需要申请的权限,应该是 [android.Manifest.permission] 中的权限，如果填写国内ROM定制增加的权限，请务必在请求前检查权限是否存在，如果权限不存在，可能一直返回拒绝的结果
     * @return MultiplePermissionBuilder
     */
    @CheckResult
    fun permissions(vararg permissions: String): PermissionRationaleBuilder {
        return PermissionRationaleBuilder(activity, fragment, permissions as Array<String>)
    }
}