package com.lws.permissionx.internal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat


/**
 * 与[androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions]类似.
 *
 * 特别之处在于加入权限解释 [RationaleController]
 */
internal class RequestPermissionsContract(private val rationaleController: RationaleController) :
    ActivityResultContract<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>() {

    override fun createIntent(context: Context, input: Array<String>): Intent {
        rationaleController.showRationale(input)
        return Intent(ActivityResultContracts.RequestMultiplePermissions.ACTION_REQUEST_PERMISSIONS).putExtra(
            ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSIONS,
            input
        )
    }

    override fun getSynchronousResult(
        context: Context,
        input: Array<String>
    ): SynchronousResult<Map<String, Boolean>>? {
        if (input.isEmpty()) {
            return SynchronousResult(emptyMap())
        }
        val allGranted = input.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        return if (allGranted) {
            SynchronousResult(input.associateWith { true })
        } else null
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): Map<String, Boolean> {
        if (resultCode != Activity.RESULT_OK) return emptyMap()
        if (intent == null) return emptyMap()
        val permissions =
            intent.getStringArrayExtra(ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSIONS)
        val grantResults =
            intent.getIntArrayExtra(ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSION_GRANT_RESULTS)
        if (grantResults == null || permissions == null) return emptyMap()
        val grantState = grantResults.map { result ->
            result == PackageManager.PERMISSION_GRANTED
        }
        return permissions.filterNotNull().zip(grantState).toMap()
    }
}