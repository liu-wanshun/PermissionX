package com.lws.permissionapp;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lws.permissionx.PermissionX;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button1).setOnClickListener(view -> {
                    PermissionX.init(this)
                            .permission(Manifest.permission.CAMERA)
                            .onRequestRationale("解释请求权限的原因")
                            .request(result -> {
                                Log.e("ssss", "onResult: " + result);
                            });
                }
        );


        findViewById(R.id.button2).setOnClickListener(view -> {
                    PermissionX.init(this)
                            .permission(Manifest.permission.CAMERA)
                            .onRequestRationale("解释请求权限的原因")
                            .transform(CustomCallback::new)
                            .request((allGranted, denyForever) -> {
                                Log.e("ssss", "onResult: " + allGranted + "  " + denyForever);
                            });
                }
        );
    }
}