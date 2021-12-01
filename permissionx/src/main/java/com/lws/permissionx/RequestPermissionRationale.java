package com.lws.permissionx;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Supplier;

public class RequestPermissionRationale implements Supplier<AlertDialog> {
    private final Supplier<AlertDialog.Builder> dialogSupplier;
    private final PermissionBuilder permissionBuilder;

    private AlertDialog dialog = null;


    RequestPermissionRationale(PermissionBuilder permissionBuilder, Supplier<AlertDialog.Builder> dialogSupplier) {
        this.permissionBuilder = permissionBuilder;
        this.dialogSupplier = dialogSupplier;
    }

    RequestPermissionRationale(PermissionBuilder permissionBuilder, CharSequence message, @StyleRes int alertDialogTheme) {
        this.permissionBuilder = permissionBuilder;
        this.dialogSupplier = () -> new AlertDialog.Builder(permissionBuilder.activity, alertDialogTheme)
                .setMessage(message);
    }


    void show() {
        get().show();
        get().getWindow().setGravity(PermissionX.getDefaultConfig().getGravity());

    }

    void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    @NonNull
    public AlertDialog get() {
        if (dialog == null) {
            dialog = dialogSupplier.get()
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        permissionBuilder.getInvisibleFragment().request(permissionBuilder);
                        dismiss();
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        permissionBuilder.onCancelRationale();
                        dismiss();
                    })
                    .create();
        }
        return dialog;
    }
}