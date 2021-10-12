package com.lws.permissionx;

import android.content.DialogInterface;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lws
 */
public abstract class PermissionBuilder<I, O> {

    private final I permission;
    protected RequestPermissionRationale rationale;
    @NonNull
    FragmentActivity activity;
    @Nullable
    Fragment fragment;
    OrientationHelper orientationHelper;
    private ActivityResultCallback<O> permissionResultCallback;

    PermissionBuilder(@NonNull FragmentActivity activity, @Nullable Fragment fragment, I permission) {
        this.activity = activity;
        this.fragment = fragment;
        orientationHelper = new OrientationHelper(activity);
        this.permission = permission;

    }

    private FragmentManager getFragmentManager() {
        if (fragment != null) {
            return fragment.getChildFragmentManager();
        }
        return activity.getSupportFragmentManager();

    }

    private InvisibleFragment getInvisibleFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(InvisibleFragment.TAG);
        if (fragment instanceof InvisibleFragment) {
            return (InvisibleFragment) fragment;
        } else {
            InvisibleFragment invisibleFragment = new InvisibleFragment();
            getFragmentManager().beginTransaction().add(invisibleFragment, InvisibleFragment.TAG)
                    .commitNowAllowingStateLoss();
            return invisibleFragment;
        }
    }

    ActivityResultCallback<O> getPermissionResultCallback() {
        return permissionResultCallback;
    }

    I getPermission() {
        return permission;
    }

    /**
     * 应使用onRequestRationale解释权限
     *
     * @param permissionResult 权限结果回调
     */
    @Deprecated
    public void request(ActivityResultCallback<O> permissionResult) {
        this.permissionResultCallback = permissionResult;
        if (PermissionX.isGranted(activity, permission)) {
            if (permission instanceof String) {
                permissionResult.onActivityResult((O) Boolean.TRUE);
            } else {
                Map<String, Boolean> result = new LinkedHashMap<>();
                for (String permission : (String[]) permission) {
                    result.put(permission, true);
                }
                permissionResult.onActivityResult((O) result);
            }

        } else {
            if (shouldShowRequestPermissionRationale() && rationale != null) {
                rationale.showRationale();
            } else {
                getInvisibleFragment().request(this);
            }
        }
    }

    private boolean shouldShowRequestPermissionRationale() {
        String firstDeniedPermission;
        if (permission instanceof String) {
            firstDeniedPermission = (String) permission;
        } else {
            firstDeniedPermission = getFirstDeniedPermission((String[]) permission);
        }
        return firstDeniedPermission != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, firstDeniedPermission);
    }

    @Nullable
    private String getFirstDeniedPermission(String[] permissions) {
        for (String permission : permissions) {
            if (!PermissionX.hasPermission(activity, permission)) {
                return permission;
            }
        }
        return null;
    }

    private void cancelRationale() {
        if (permission instanceof String) {
            permissionResultCallback.onActivityResult((O) (Boolean.valueOf(PermissionX.hasPermission(activity, (String) permission))));
        } else {
            Map<String, Boolean> result = new LinkedHashMap<>();
            for (String permission : (String[]) permission) {
                result.put(permission, (PermissionX.hasPermission(activity, permission)));
            }
            permissionResultCallback.onActivityResult((O) result);
        }

    }

    /**
     * 权限解释
     *
     * @param rationale 权限解释字符串资源
     * @return PermissionBuilder
     */
    public PermissionRequester<I, O> onRequestRationale(@StringRes int rationale) {
        return onRequestRationale(activity.getText(rationale));

    }

    /**
     * 权限解释
     *
     * @param rationale 权限解释文字
     * @return PermissionBuilder
     */
    public PermissionRequester<I, O> onRequestRationale(CharSequence rationale) {
        if (PermissionDialogConfig.isAlertDialogAndroidX()) {
            this.rationale = () -> new AlertDialog.Builder(activity, PermissionDialogConfig.getDefaultDialogTheme())
                    .setMessage(rationale)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        getInvisibleFragment().request(this);
                        dialog.dismiss();
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        cancelRationale();
                        dialog.dismiss();
                    })
                    .show();
        } else {
            this.rationale = () -> new android.app.AlertDialog.Builder(activity, PermissionDialogConfig.getDefaultDialogTheme())
                    .setMessage(rationale)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        getInvisibleFragment().request(this);
                        dialog.dismiss();
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        cancelRationale();
                        dialog.dismiss();
                    })
                    .show();
        }
        return new PermissionRequester<>(this);
    }

    /**
     * 权限解释
     *
     * @param dialogSupplier 提供androidX AlertDialog
     * @return PermissionBuilder
     */
    public PermissionRequester<I, O> onRequestRationale(AlertDialogSupplierX dialogSupplier) {
        this.rationale = () -> {
            AlertDialog dialog = dialogSupplier.getBuilder().create();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
                getInvisibleFragment().request(this);
                dialog.dismiss();
            });
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(v -> {
                cancelRationale();
                dialog.dismiss();
            });
            dialog.show();
        };
        return new PermissionRequester<>(this);
    }

    /**
     * 权限解释
     *
     * @param dialogSupplier 提供android平台AlertDialog
     * @return PermissionBuilder
     */
    public PermissionRequester<I, O> onRequestRationale(AlertDialogSupplier dialogSupplier) {
        this.rationale = () -> {
            android.app.AlertDialog dialog = dialogSupplier.getBuilder().create();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
                getInvisibleFragment().request(this);
                dialog.dismiss();
            });
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(v -> {
                cancelRationale();
                dialog.dismiss();
            });
            dialog.show();
        };
        return new PermissionRequester<>(this);
    }


    void restoreOrientation() {
        orientationHelper.restoreOrientation();
    }

    void removeInvisibleFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(InvisibleFragment.TAG);
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commitNowAllowingStateLoss();
        }
    }

    void lockOrientation() {
        orientationHelper.lockOrientation();
    }


    private interface RequestPermissionRationale {

        /**
         * 向用户解释权限使用的目的
         */
        void showRationale();
    }

    public static class SinglePermissionBuilderIml extends PermissionBuilder<String, Boolean> {

        public SinglePermissionBuilderIml(@NonNull FragmentActivity activity, @Nullable Fragment fragment, String permission) {
            super(activity, fragment, permission);
        }
    }

    public static class MultiplePermissionBuilderIml extends PermissionBuilder<String[], Map<String, Boolean>> {

        public MultiplePermissionBuilderIml(@NonNull FragmentActivity activity, @Nullable Fragment fragment, String... permission) {
            super(activity, fragment, permission);
        }
    }
}
