package com.lws.permissionx;

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
public class PermissionBuilder {

    final RationaleController rationaleController = new RationaleController(this);
    private final String[] permissions;
    @NonNull
    FragmentActivity activity;
    @Nullable
    Fragment fragment;
    OrientationHelper orientationHelper;
    private PermissionResultCallback permissionResultCallback;

    PermissionBuilder(@NonNull FragmentActivity activity, @Nullable Fragment fragment, String[] permissions) {
        this.activity = activity;
        this.fragment = fragment;
        orientationHelper = new OrientationHelper(activity);
        this.permissions = permissions;
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


    void handleResult(Map<String, Boolean> result) {
        PermissionResult permissionResult = new PermissionResult();
        for (String permission : result.keySet()) {
            if (Boolean.TRUE.equals(result.get(permission))) {
                permissionResult.addGranted(permission);
            } else {
                permissionResult.addDenied(permission);
            }
        }
        permissionResultCallback.onPermissionResult(permissionResult);
        //处理拒绝的情况
        if (!PermissionX.hasPermissions(activity, permissions)) {
            if (shouldShowPermissionRationale()) {
                rationaleController.showDeniedRationale();
            } else {
                rationaleController.showDeniedForeverRationale();
            }
        }
    }

    String[] getPermissions() {
        return permissions;
    }


    /**
     * 建议请求权限前进行解释权限使用原因
     *
     * @param permissionResultCallback 请求权限的结果回调
     */
    public void request(@NonNull PermissionResultCallback permissionResultCallback) {
        this.permissionResultCallback = permissionResultCallback;
        if (PermissionX.hasPermissions(activity, permissions)) {
            PermissionResult permissionResult = new PermissionResult();
            for (String permission : permissions) {
                permissionResult.addGranted(permission);
            }
            permissionResultCallback.onPermissionResult(permissionResult);
        } else {
            if (shouldShowPermissionRationale() && rationaleController.canShowRequestRationale()) {
                rationaleController.showRequestRationale();
            } else {
                getInvisibleFragment().request(this);
            }
        }
    }

    private boolean shouldShowPermissionRationale() {
        String firstDeniedPermission = getFirstDeniedPermission(permissions);
        return firstDeniedPermission != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, firstDeniedPermission);
    }

    @Nullable
    private String getFirstDeniedPermission(String[] permissions) {
        for (String permission : permissions) {
            if (!PermissionX.hasPermissions(activity, permission)) {
                return permission;
            }
        }
        return null;
    }

    void onCancelRationale() {
        Map<String, Boolean> result = new LinkedHashMap<>();
        for (String permission : permissions) {
            result.put(permission, (PermissionX.hasPermissions(activity, permission)));
        }
        handleResult(result);
    }

    /**
     * @param rationaleMsg 解释使用权限的原因，同意后将进行请求权限
     * @return this
     */
    @CheckResult
    public PermissionBuilder onRequestRationale(@NonNull CharSequence rationaleMsg) {
        rationaleController.requestRationale = rationaleMsg;
        return this;
    }

    /**
     * @param rationaleRes 解释使用权限的原因，同意后将进行请求权限
     * @return this
     */
    @CheckResult
    public PermissionBuilder onRequestRationale(@StringRes int rationaleRes) {
        return onRequestRationale(activity.getText(rationaleRes));
    }

    /**
     * @param deniedRationale  用户拒绝权限，进行解释，同意将再次请求权限
     * @param negativeListener 用户不认可此解释时,执行此操作
     * @return this
     */
    @CheckResult
    public PermissionBuilder onDeniedRationale(@NonNull CharSequence deniedRationale, @NonNull Runnable negativeListener) {
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
    public PermissionBuilder onDeniedRationale(@StringRes int rationaleRes, @NonNull Runnable negativeListener) {
        return onDeniedRationale(activity.getText(rationaleRes), negativeListener);
    }

    /**
     * @param deniedForeverRationale 用户永久拒绝权限，进行解释，同意将跳转设置界面让用户自己开启权限
     * @param negativeListener       用户不认可此解释时,执行此操作
     * @return this
     */
    @CheckResult
    public PermissionBuilder onDeniedForeverRationale(@NonNull CharSequence deniedForeverRationale, @NonNull Runnable negativeListener) {
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
    public PermissionBuilder onDeniedForeverRationale(@StringRes int rationaleRes, @NonNull Runnable negativeListener) {
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
}
