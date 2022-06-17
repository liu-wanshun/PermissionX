package com.lws.permissionx

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

interface RationaleFactory {

    /**
     * 请求权限时，该解释显示在上方
     */
    fun createRationale(
        context: Context,
        permissions: Array<String>,
        rationaleMsg: CharSequence?
    ): Dialog

    /**
     * 永久拒绝权限时，显示该引导
     *
     * @param positive 前往系统应用设置页面
     * @param negative 拒绝申请
     */
    fun createDeniedForeverRationale(
        context: Context,
        permissions: Array<String>,
        positive: Runnable,
        negative: Runnable
    ): Dialog


    /**
     * 未能从系统中读取到权限组名称时，可使用此方法自定义权限名称
     */
    fun fallbackPermissionName(context: Context, permission: String): String? {
        return null
    }


    /**
     * 获取权限名称集合，优先从系统中读取，未能读取时可通过 [fallbackPermissionName] 自定义
     */
    fun buildPermissionNames(
        permissions: Array<String>,
        context: Context
    ): Set<String> {
        val permissionNames = mutableSetOf<String>()

        val currentVersion = Build.VERSION.SDK_INT

        for (permission in permissions) {
            var permissionGroup = when {
                currentVersion < Build.VERSION_CODES.Q -> {
                    try {
                        val permissionInfo = context.packageManager.getPermissionInfo(permission, 0)
                        permissionInfo.group
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                        null
                    }
                }
                currentVersion == Build.VERSION_CODES.Q -> permissionMapOnQ[permission]
                currentVersion == Build.VERSION_CODES.R -> permissionMapOnR[permission]
                currentVersion == Build.VERSION_CODES.S -> permissionMapOnS[permission]
                else -> {
                    // If currentVersion is newer than the latest version we support, we just use
                    // the latest version for temp. Will upgrade in the next release.
                    permissionMapOnS[permission]
                }
            }
            if (permissionGroup == null && currentVersion >= Build.VERSION_CODES.Q) {
                permissionGroup = try {
                    val permissionInfo = context.packageManager.getPermissionInfo(permission, 0)
                    permissionInfo.group
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                    null
                }
            }

            var permissionName = if (permissionGroup != null) {
                try {
                    context.getString(
                        context.packageManager
                            .getPermissionGroupInfo(permissionGroup, 0).labelRes
                    )
                } catch (t: Throwable) {
                    null
                }

            } else null

            if (permissionName == null) {
                permissionName = fallbackPermissionName(context, permission)
            }
            if (!permissionName.isNullOrEmpty()) {
                permissionNames.add(permissionName)
            }
        }
        return permissionNames
    }
}