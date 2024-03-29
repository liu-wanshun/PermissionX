package com.lws.permissionapp

import android.content.Context

import com.lws.permissionx.DefaultRationaleFactory

class CustomRationaleFactory : DefaultRationaleFactory() {

    override fun fallbackPermissionName(context: Context, permission: String): String? {
        return when (permission) {
            "com.android.permission.GET_INSTALLED_APPS" -> {
                "读取应用列表"
            }
            else -> null
        }

    }
}