package com.lws.permissionx

import java.util.*

/**
 * @author lws
 */
class PermissionResult internal constructor() {
    fun addGranted(permission: String) {
        grantedList.add(permission)
    }

    fun addDenied(permission: String) {
        deniedList.add(permission)
    }

    private val grantedList: MutableList<String> = ArrayList()
    private val deniedList: MutableList<String> = ArrayList()
    val isAllGranted: Boolean
        get() = deniedList.isEmpty()

    fun getGrantedList(): List<String> {
        return grantedList
    }

    fun getDeniedList(): List<String> {
        return deniedList
    }

    override fun toString(): String {
        return "PermissionResult{" +
                "grantedList=" + grantedList +
                ", deniedList=" + deniedList +
                '}'
    }
}