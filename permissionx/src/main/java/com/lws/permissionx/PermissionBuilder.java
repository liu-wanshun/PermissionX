package com.lws.permissionx;

import android.content.Context;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Supplier;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

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

    InvisibleFragment getInvisibleFragment() {
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
    public void request(@NonNull ActivityResultCallback<O> permissionResult) {
        this.permissionResultCallback = permissionResult;
        if (isGranted(activity, permission)) {
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
                rationale.show();
            } else {
                getInvisibleFragment().request(this);
            }
        }
    }

    private <T> boolean isGranted(Context context, T permission) {
        if (permission instanceof String) {
            return PermissionX.hasPermissions(context, (String) permission);
        } else if (permission instanceof String[]) {
            return PermissionX.hasPermissions(context, ((String[]) permission)[0], (String[]) permission);
        } else {
            return true;
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

    void onCancelRationale() {
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


    public PermissionRequester<I, O> onRequestRationale(@NonNull CharSequence rationaleMsg, @StyleRes int alertDialogTheme, @Nullable Lifecycle.Event autoDismiss) {
        this.rationale = new RequestPermissionRationale(this, rationaleMsg, alertDialogTheme, autoDismiss);
        return new PermissionRequester<>(this);
    }

    public PermissionRequester<I, O> onRequestRationale(@NonNull CharSequence rationaleMsg, @StyleRes int alertDialogTheme) {
        return onRequestRationale(rationaleMsg, alertDialogTheme, PermissionX.getDefaultConfig().getAutoDismiss());
    }

    public PermissionRequester<I, O> onRequestRationale(@NonNull CharSequence rationaleMsg, @Nullable Lifecycle.Event autoDismiss) {
        return onRequestRationale(rationaleMsg, PermissionX.getDefaultConfig().getAlertDialogTheme(), autoDismiss);
    }

    public PermissionRequester<I, O> onRequestRationale(@NonNull CharSequence rationaleMsg) {
        return onRequestRationale(rationaleMsg, PermissionX.getDefaultConfig().getAlertDialogTheme(), PermissionX.getDefaultConfig().getAutoDismiss());
    }


    public PermissionRequester<I, O> onRequestRationale(@StringRes int rationaleRes) {
        return onRequestRationale(activity.getText(rationaleRes));
    }

    public PermissionRequester<I, O> onRequestRationale(@StringRes int rationaleRes, @StyleRes int alertDialogTheme) {
        return onRequestRationale(activity.getText(rationaleRes), alertDialogTheme);
    }

    public PermissionRequester<I, O> onRequestRationale(@StringRes int rationaleRes, @StyleRes int alertDialogTheme, @Nullable Lifecycle.Event autoDismiss) {
        return onRequestRationale(activity.getText(rationaleRes), alertDialogTheme, autoDismiss);
    }


    public PermissionRequester<I, O> onRequestRationale(@NonNull Supplier<AlertDialog.Builder> alertDialogSupplier, @Nullable Lifecycle.Event autoDismiss) {
        this.rationale = new RequestPermissionRationale(this, alertDialogSupplier, autoDismiss);
        return new PermissionRequester<>(this);
    }

    public PermissionRequester<I, O> onRequestRationale(@NonNull Supplier<AlertDialog.Builder> alertDialogSupplier) {
        return onRequestRationale(alertDialogSupplier, null);
    }


    void restoreOrientation() {
        orientationHelper.restoreOrientation();
    }

    void removeInvisibleFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(InvisibleFragment.TAG);
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    void lockOrientation() {
        orientationHelper.lockOrientation();
    }

    static class SinglePermissionBuilderIml extends PermissionBuilder<String, Boolean> {

        SinglePermissionBuilderIml(@NonNull FragmentActivity activity, @Nullable Fragment fragment, String permission) {
            super(activity, fragment, permission);
        }
    }

    static class MultiplePermissionBuilderIml extends PermissionBuilder<String[], Map<String, Boolean>> {

        MultiplePermissionBuilderIml(@NonNull FragmentActivity activity, @Nullable Fragment fragment, String... permission) {
            super(activity, fragment, permission);
        }
    }
}
