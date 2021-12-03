package com.lws.permissionx;

import android.content.Context;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
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

    final RationaleController<I, O> rationaleController = new RationaleController<>(this);
    private final I permission;
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

    void handleResult(O result) {
        permissionResultCallback.onActivityResult(result);
        //处理拒绝的情况
        if (!isGranted(activity, permission)) {
            if (shouldShowPermissionRationale()) {
                rationaleController.showDeniedRationale();
            } else {
                rationaleController.showDeniedForeverRationale();
            }
        }
    }

    I getPermission() {
        return permission;
    }


    /**
     * 建议请求权限前进行解释权限使用原因
     *
     * @param permissionResult 请求权限的结果回调
     */
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
            if (shouldShowPermissionRationale() && rationaleController.canShowRequestRationale()) {
                rationaleController.showRequestRationale();
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

    private boolean shouldShowPermissionRationale() {
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

    /**
     * @param rationaleMsg 解释使用权限的原因，同意后将进行请求权限
     * @return this
     */
    @CheckResult
    public PermissionBuilder<I, O> onRequestRationale(@NonNull CharSequence rationaleMsg) {
        rationaleController.requestRationale = rationaleMsg;
        return this;
    }

    /**
     * @param rationaleRes 解释使用权限的原因，同意后将进行请求权限
     * @return this
     */
    @CheckResult
    public PermissionBuilder<I, O> onRequestRationale(@StringRes int rationaleRes) {
        return onRequestRationale(activity.getText(rationaleRes));
    }

    /**
     * @param deniedRationale  用户拒绝权限，进行解释，同意将再次请求权限
     * @param negativeListener 用户不认可此解释时,执行此操作
     * @return this
     */
    @CheckResult
    public PermissionBuilder<I, O> onDeniedRationale(@NonNull CharSequence deniedRationale, @NonNull Runnable negativeListener) {
        rationaleController.deniedRationale = deniedRationale;
        rationaleController.deniedRationaleNegativeListener = negativeListener;
        return this;
    }


    /**
     * @param rationaleRes     用户拒绝权限，进行解释，同意将再次请求权限
     * @param negativeListener 用户不认可此解释时,执行此操作
     * @return this
     */
    @CheckResult
    public PermissionBuilder<I, O> onDeniedRationale(@StringRes int rationaleRes, @NonNull Runnable negativeListener) {
        return onDeniedRationale(activity.getText(rationaleRes), negativeListener);
    }

    /**
     * @param deniedForeverRationale 用户永久拒绝权限，进行解释，同意将跳转设置界面让用户自己开启权限
     * @param negativeListener       用户不认可此解释时,执行此操作
     * @return this
     */
    @CheckResult
    public PermissionBuilder<I, O> onDeniedForeverRationale(@NonNull CharSequence deniedForeverRationale, @NonNull Runnable negativeListener) {
        rationaleController.deniedForeverRationale = deniedForeverRationale;
        rationaleController.deniedForeverRationaleNegativeListener = negativeListener;
        return this;
    }


    /**
     * @param rationaleRes     用户永久拒绝权限，进行解释，同意将跳转设置界面让用户自己开启权限
     * @param negativeListener 用户不认可此解释时,执行此操作
     * @return this
     */
    @CheckResult
    public PermissionBuilder<I, O> onDeniedForeverRationale(@StringRes int rationaleRes, @NonNull Runnable negativeListener) {
        return onDeniedForeverRationale(activity.getText(rationaleRes), negativeListener);
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
