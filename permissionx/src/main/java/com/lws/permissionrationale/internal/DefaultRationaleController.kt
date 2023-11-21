package com.lws.permissionrationale.internal

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.Gravity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.lws.permissionrationale.DefaultRationaleFactory
import com.lws.permissionrationale.PermissionRationaleBuilder
import com.lws.permissionrationale.PermissionResult
import com.lws.permissionrationale.PermissionRationale
import java.util.concurrent.atomic.AtomicInteger

private val mNextLocalRequestCode: AtomicInteger = AtomicInteger()

internal class DefaultRationaleController(
    private val activity: ComponentActivity,
    private val lifecycleOwner: LifecycleOwner
) : RationaleController {
    private val registry: ActivityResultRegistry = activity.activityResultRegistry
    private val permissions: MutableList<String> = mutableListOf()

    private var permissionBuilder: PermissionRationaleBuilder? = null

    private var rationaleDialog: Dialog? = null

    private val rationaleFactory
        get() = PermissionRationale.customRationaleFactory ?: DefaultRationaleFactory()


    private val appDetailsSetting =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val result = buildMap {
                permissions.forEach {
                    put(it, PermissionRationale.hasPermissions(activity, it))
                }
            }
            callBackResult(result)
        }
    private val requestPermissions: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(RequestPermissionsContract(this)) { result ->
            val firstDeniedPermission = result.asSequence().firstOrNull { !it.value }?.key
            if (firstDeniedPermission != null && !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    firstDeniedPermission
                )
            ) {
                showDeniedForeverRationale(result)
            } else {
                callBackResult(result)
            }
        }


    private fun <I, O> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ): ActivityResultLauncher<I> {
        return registry.register(
            "permission_rq#" + mNextLocalRequestCode.getAndIncrement(), contract, callback
        ).also {
            lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        it.unregister()
                        lifecycleOwner.lifecycle.removeObserver(this)
                    }
                }
            })
        }
    }


    internal fun request(
        permissionBuilder: PermissionRationaleBuilder,
        permissions: Array<String>
    ) {
        this.permissions.clear()
        this.permissions.addAll(permissions)

        this.permissionBuilder = permissionBuilder
        requestPermissions.launch(permissions)
    }

    override fun showRationale(permissions: Array<String>) {
        rationaleDialog?.dismiss()
        val orientationHelper = OrientationHelper(activity)
        rationaleDialog =
            rationaleFactory.createRationale(
                activity,
                permissions,
                permissionBuilder?.requestRationale
            ).apply {
                setCancelable(false)
                setOnShowListener {
                    orientationHelper.lockOrientation()
                }
                setOnDismissListener {
                    orientationHelper.restoreOrientation()
                }
                window?.setGravity(Gravity.TOP)
                show()
            }
    }

    override fun showDeniedForeverRationale(result: Map<String, Boolean>) {
        rationaleFactory.createDeniedForeverRationale(
            activity,
            result.keys.toTypedArray(),
            positive = {
                appDetailsSetting.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                })
            },
            negative = {
                callBackResult(result)
            },
        ).apply {
            setCanceledOnTouchOutside(false)
            setOnCancelListener {
                callBackResult(result)
            }
            show()
        }
    }

    private fun callBackResult(result: Map<String, @JvmSuppressWildcards Boolean>) {

        rationaleDialog?.cancel()
        rationaleDialog = null

        val permissionResult = PermissionResult(result)
        permissionBuilder?.callBackResult(permissionResult)
    }
}