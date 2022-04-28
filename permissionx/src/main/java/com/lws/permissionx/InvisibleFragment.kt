package com.lws.permissionx

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.fragment.app.Fragment

/**
 * @author lws
 */
class InvisibleFragment : Fragment() {
    private var permissionBuilder: PermissionBuilder? = null
    private val requestMultiplePermissions: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(RequestMultiplePermissions()) { result ->
            orientationHelper?.restoreOrientation()
            permissionBuilder?.handleResult(result, false)
            try {
                parentFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
            } catch (e: Throwable) {
                //Fragment not associated with a fragment manager.
            }
        }

    internal fun request(permissionBuilder: PermissionBuilder, permissions: Array<String>) {
        this.permissionBuilder = permissionBuilder
        orientationHelper?.lockOrientation()
        requestMultiplePermissions.launch(permissions)
    }


    private var orientationHelper: OrientationHelper? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        orientationHelper = OrientationHelper(requireActivity())
    }

    override fun onDetach() {
        super.onDetach()
        orientationHelper = null
    }

    companion object {
        const val TAG = "InvisibleFragment"
    }
}