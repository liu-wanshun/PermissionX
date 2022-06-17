package com.lws.permissionx

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.Gravity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.lws.permissionx.internal.DefaultRationaleFactory
import com.lws.permissionx.internal.OrientationHelper
import com.lws.permissionx.internal.RationaleController
import com.lws.permissionx.internal.RequestPermissionsContract

/**
 * @author lws
 */
class InvisibleFragment : Fragment(), RationaleController {
    private val permissions: MutableList<String> = mutableListOf()

    private var permissionBuilder: PermissionRationaleBuilder? = null

    private var rationaleDialog: Dialog? = null

    private val rationaleFactory
        get() = PermissionX.customRationaleFactory ?: DefaultRationaleFactory()


    private val appDetailsSetting = registerForActivityResult(StartActivityForResult()) {
        val result = buildMap {
            permissions.forEach {
                put(it, PermissionX.hasPermissions(requireContext(), it))
            }
        }
        callBackResult(result)
    }
    private val requestPermissions: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(RequestPermissionsContract(this)) { result ->
            val firstDeniedPermission = result.asSequence().firstOrNull { !it.value }?.key
            if (firstDeniedPermission != null && !ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    firstDeniedPermission
                )
            ) {
                showDeniedForeverRationale(result)
            } else {
                callBackResult(result)
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
        val orientationHelper = OrientationHelper(requireActivity())
        rationaleDialog =
            rationaleFactory.createRationale(
                requireContext(),
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
            requireContext(),
            result.keys.toTypedArray(),
            positive = {
                appDetailsSetting.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", requireContext().packageName, null)
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

        try {
            parentFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
        } catch (e: Throwable) {
            //Fragment not associated with a fragment manager.
        }
    }

    companion object {
        const val TAG = "InvisibleFragment"
    }
}