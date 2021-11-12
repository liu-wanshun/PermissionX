package com.lws.permissionx;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Map;

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
     * @param permission 需要申请的权限
     * @return PermissionBuilder
     */
    public PermissionBuilder<String, Boolean> permission(@NonNull String permission) {
        return new PermissionBuilder.SinglePermissionBuilderIml(activity, fragment, permission);
    }

    /**
     * 填写需要申请的权限
     *
     * @param permissions 需要申请的权限
     * @return MultiplePermissionBuilder
     */
    public PermissionBuilder<String[], Map<String, Boolean>> permissions(@NonNull String... permissions) {
        return new PermissionBuilder.MultiplePermissionBuilderIml(activity, fragment, permissions);
    }

}
