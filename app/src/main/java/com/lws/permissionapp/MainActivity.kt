package com.lws.permissionapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.lws.permissionrationale.PermissionRationale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.request).setOnClickListener {

            val permissions = needRequestPermissions()
            PermissionRationale.with(this)
                .permissions(*permissions)
                .onRequestRationale("授权通过后，方便在聊天中，提供发送手机相册中的媒体内容给对方的能力")
                .request { result ->
                    Log.e("ssss", "request   onResult: isAllGranted  ->  ${result.isAllGranted}")
                    Log.e("ssss", "request   onResult: Granted -> ${result.getGrantedList()}")
                    Log.e("ssss", "request   onResult: Denied  -> ${result.getDeniedList()}")
                }
        }

        findViewById<Button>(R.id.custom).setOnClickListener {
            PermissionRationale.setRationaleFactory(CustomRationaleFactory())
        }

        findViewById<Button>(R.id.cancel_custom).setOnClickListener {
            PermissionRationale.setRationaleFactory(null)
        }

    }

    private fun needRequestPermissions(): Array<String> {
        //非安卓原生权限，需要提前判断是否存在该权限
        val permission = "com.android.permission.GET_INSTALLED_APPS"
        val permissionInfo = try {
            packageManager.getPermissionInfo(permission, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
        val permissions = if (permissionInfo != null) {
            arrayOf(
                "com.android.permission.GET_INSTALLED_APPS",
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        return permissions
    }
}