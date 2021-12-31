package com.lws.permissionx;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * @author lws
 */
public class PermissionMediator {


    private final FragmentActivity activity;
    private Fragment fragment;

    PermissionMediator(FragmentActivity activity) {
        this.activity = activity;
    }

    PermissionMediator(Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.requireActivity();
    }

    /**
     * 填写需要申请的权限
     *
     * @param permissions 需要申请的权限
     * @return MultiplePermissionBuilder
     */
    @CheckResult
    public PermissionBuilder permissions(@NonNull String... permissions) {
        return new PermissionBuilder(activity, fragment, permissions);
    }
}
