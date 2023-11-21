package com.lws.permissionx

import androidx.activity.ComponentActivity
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.lws.permissionx.internal.DefaultRationaleController
import java.util.WeakHashMap

/**
 * @author lws
 */

private val weakHashMap = WeakHashMap<Any, DefaultRationaleController>()
class PermissionRationaleBuilder internal constructor(
    private val activity: ComponentActivity,
    private val fragment: Fragment?,
    private val permissions: Array<String>
) {

    @JvmSynthetic
    @JvmField
    internal var requestRationale: CharSequence? = null

    private var permissionResultCallback: PermissionResultCallback? = null


    private val rationaleController: DefaultRationaleController
        get() {
            var controller = weakHashMap[fragment ?: activity]
            if (controller == null) {
                controller = DefaultRationaleController(activity, fragment ?: activity)
            }
            weakHashMap[fragment ?: activity] = controller
            return controller
        }

    /**
     * 建议请求权限前进行解释权限使用原因
     *
     * @param permissionResultCallback 请求权限的结果回调
     */
    fun request(permissionResultCallback: PermissionResultCallback) {
        this.permissionResultCallback = permissionResultCallback
        rationaleController.request(this, permissions)
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