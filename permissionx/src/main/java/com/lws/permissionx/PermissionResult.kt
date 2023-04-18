package com.lws.permissionx

/**
 * @author lws
 */
class PermissionResult internal constructor(result: Map<String, Boolean>) {
    private val grantedList: MutableList<String> = ArrayList()
    private val deniedList: MutableList<String> = ArrayList()

    init {
        for (permission in result.keys) {
            if (java.lang.Boolean.TRUE == result[permission]) {
                grantedList.add(permission)
            } else {
                deniedList.add(permission)
            }
        }
    }

    val isAllGranted: Boolean
        get() = deniedList.isEmpty() && grantedList.isNotEmpty()

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

fun interface PermissionResultCallback {
    fun onPermissionResult(result: PermissionResult)
}