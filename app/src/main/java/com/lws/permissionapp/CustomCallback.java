package com.lws.permissionapp;

import androidx.core.app.ActivityCompat;

import com.lws.permissionx.PermissionRequester;

public class CustomCallback extends PermissionRequester<String, Boolean> {


    public CustomCallback(PermissionRequester<String, Boolean> permissionRequester) {
        super(permissionRequester);
    }

    public void request(ICustomCallback permissionResult) {
        super.request(result -> {
            boolean denyForever = false;
            if (!result) {
                denyForever = !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            }
            permissionResult.onResult(result, denyForever);
        });
    }


    public interface ICustomCallback {
        void onResult(boolean allGranted, boolean denyForever);
    }
}
