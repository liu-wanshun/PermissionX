package com.lws.permissionx.internal

internal interface RationaleController {
    fun showRationale(permissions: Array<String>)

    fun showDeniedForeverRationale(result: Map<String, Boolean>)
}