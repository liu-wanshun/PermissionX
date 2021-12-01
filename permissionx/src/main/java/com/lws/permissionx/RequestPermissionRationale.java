package com.lws.permissionx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Supplier;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

public class RequestPermissionRationale implements Supplier<AlertDialog> {
    private final Supplier<AlertDialog.Builder> dialogSupplier;
    private final PermissionBuilder permissionBuilder;
    private AlertDialog dialog = null;
    @Nullable
    private final Lifecycle.State autoDismiss;


    RequestPermissionRationale(PermissionBuilder permissionBuilder, Supplier<AlertDialog.Builder> dialogSupplier,@Nullable Lifecycle.State autoDismiss) {
        this.permissionBuilder = permissionBuilder;
        this.dialogSupplier = dialogSupplier;
        this.autoDismiss = autoDismiss;
    }

    RequestPermissionRationale(PermissionBuilder permissionBuilder, CharSequence message, @StyleRes int alertDialogTheme, @Nullable Lifecycle.State autoDismiss) {
        this.permissionBuilder = permissionBuilder;
        this.dialogSupplier = () -> new AlertDialog.Builder(permissionBuilder.activity, alertDialogTheme)
                .setMessage(message);
        this.autoDismiss = autoDismiss;
    }


    void show() {
        get().getWindow().setGravity(PermissionX.getDefaultConfig().getGravity());
        get().show();
        if (autoDismiss != null) {
            new AutoDismissObserver(this).bind(permissionBuilder.activity);
        }

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

    static class AutoDismissObserver implements LifecycleEventObserver {


        private final RequestPermissionRationale rationale;


        public AutoDismissObserver(RequestPermissionRationale rationale) {
            this.rationale = rationale;

        }

        void bind(LifecycleOwner lifecycleOwner) {
            rationale.get().setOnDismissListener(dialog -> {
                lifecycleOwner.getLifecycle().removeObserver(this);
            });
            lifecycleOwner.getLifecycle().addObserver(this);
        }

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (source.getLifecycle().getCurrentState().compareTo(rationale.autoDismiss) <= 0) {
                rationale.dismiss();
                source.getLifecycle().removeObserver(this);
            }
        }
    }
}