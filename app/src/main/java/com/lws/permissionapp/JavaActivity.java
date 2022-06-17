package com.lws.permissionapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lws.permissionx.PermissionCompat;
import com.lws.permissionx.PermissionX;

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.request).setOnClickListener(v -> PermissionX.with(this)
                .permissions(PermissionCompat.GET_INSTALLED_APPS)
                .request(result -> {

                }));
    }
}
