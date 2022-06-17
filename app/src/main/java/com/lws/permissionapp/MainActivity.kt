package com.lws.permissionapp

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.lws.permissionx.PermissionX

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.request).setOnClickListener {
            PermissionX.with(this)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onRequestRationale("授权通过后，方便在聊天中，提供发送手机相册中的媒体内容给对方的能力")
                .request { result ->
                    Log.e("ssss", "request   onResult: isAllGranted  ->  ${result.isAllGranted}")
                    Log.e("ssss", "request   onResult: Granted -> ${result.getGrantedList()}")
                    Log.e("ssss", "request   onResult: Denied  -> ${result.getDeniedList()}")
                }
        }

        findViewById<Button>(R.id.custom).setOnClickListener {
            PermissionX.setRationaleFactory(CustomRationaleFactory())
        }

        findViewById<Button>(R.id.cancel_custom).setOnClickListener {
            PermissionX.setRationaleFactory(null)
        }

    }
}