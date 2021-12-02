package com.lws.permissionapp;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lws.permissionx.PermissionX;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionX.init(this)
                .permission(Manifest.permission.CAMERA)
                .onRequestRationale("解释请求权限的原因")
                //在拒绝权限时进行解释（可选）
                .onDeniedRationale("拒绝了权限，进行解释，同意后将再次请求权限", () -> {
                    Log.e("ssss", "不认可拒绝解释，可以退出");
                })
                //在永久拒绝权限时进行解释（可选）
                .onDeniedForeverRationale("永久拒绝权限解释，同意将跳转设置界面让用户自己开启权限", () -> {
                    Log.e("ssss", "不认可永久拒绝解释,可以退出");
                })
                .request(result -> {
                    Log.e("ssss", "onResult: $result");
                });
    }
}