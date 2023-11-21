package com.lws.permissionapp;

import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lws.permissionrationale.PermissionRationale;

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //非安卓原生权限，需要提前判断是否存在该权限
                String permission = "com.android.permission.GET_INSTALLED_APPS";

                PermissionInfo permissionInfo = null;
                try {
                    permissionInfo = getPackageManager().getPermissionInfo(permission, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if (permissionInfo != null) {
                    PermissionRationale.with(JavaActivity.this)
                            .permissions(permission)
                            .request(result -> {
                                Toast.makeText(JavaActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                            });
                } else {
                    Toast.makeText(JavaActivity.this, "权限不存在", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
