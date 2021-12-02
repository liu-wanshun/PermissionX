package com.lws.permissionx;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

class RationaleController<I, O> {
    private final PermissionBuilder<I, O> permissionBuilder;


    CharSequence requestRationale;

    CharSequence deniedRationale;

    CharSequence deniedForeverRationale;

    Runnable deniedRationaleNegativeListener;

    Runnable deniedForeverRationaleNegativeListener;


    RationaleController(PermissionBuilder<I, O> permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
    }

    boolean canShowRequestRationale() {
        return requestRationale != null && !PermissionX.inRequesting;
    }

    boolean canShowDeniedRationale() {
        return deniedRationale != null && deniedRationaleNegativeListener != null && !PermissionX.inRequesting;
    }

    boolean canShowDeniedForeverRationale() {
        return deniedForeverRationale != null && deniedForeverRationaleNegativeListener != null && !PermissionX.inRequesting;
    }


    void showRequestRationale() {
        AlertDialog rationaleDialog = new AlertDialog.Builder(permissionBuilder.activity, PermissionX.getDefaultConfig().getAlertDialogTheme())
                .setCancelable(false)
                .setMessage(requestRationale)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> permissionBuilder.getInvisibleFragment().request(permissionBuilder))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> permissionBuilder.onCancelRationale())
                .create();
        show(rationaleDialog);
    }

    void showDeniedRationale() {
        if (!canShowDeniedRationale()) {
            return;
        }
        AlertDialog rationaleDialog = new AlertDialog.Builder(permissionBuilder.activity, PermissionX.getDefaultConfig().getAlertDialogTheme())
                .setCancelable(false)
                .setMessage(deniedRationale)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> permissionBuilder.getInvisibleFragment().request(permissionBuilder))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> deniedRationaleNegativeListener.run())
                .create();
        show(rationaleDialog);
    }

    void showDeniedForeverRationale() {
        if (!canShowDeniedForeverRationale()) {
            return;
        }
        AlertDialog rationaleDialog = new AlertDialog.Builder(permissionBuilder.activity, PermissionX.getDefaultConfig().getAlertDialogTheme())
                .setCancelable(false)
                .setMessage(deniedForeverRationale)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> forwardToSettings(permissionBuilder.activity))
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> deniedForeverRationaleNegativeListener.run())
                .create();
        show(rationaleDialog);
    }


    private void show(AlertDialog alertDialog) {
        PermissionX.inRequesting = true;
        alertDialog.show();
        alertDialog.setOnDismissListener(dialog -> PermissionX.inRequesting = false);
        alertDialog.getWindow().setGravity(PermissionX.getDefaultConfig().getGravity());
    }

    private void forwardToSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

}