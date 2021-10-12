package com.lws.permissionx;

import androidx.activity.result.ActivityResultCallback;
import androidx.arch.core.util.Function;
import androidx.fragment.app.FragmentActivity;

/**
 * @author lws
 */
public class PermissionRequester<I, O> {


    protected final PermissionBuilder<I, O> permissionBuilder;
    protected final FragmentActivity activity;
    protected final I permission;

    protected PermissionRequester(PermissionRequester<I, O> permissionRequester) {
        this(permissionRequester.permissionBuilder);
    }

    PermissionRequester(PermissionBuilder<I, O> permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
        this.activity = this.permissionBuilder.activity;
        this.permission = this.permissionBuilder.getPermission();
    }

    /**
     * 执行请求权限
     *
     * @param permissionResult 请求结果回调
     */
    @SuppressWarnings("deprecation")
    public void request(ActivityResultCallback<O> permissionResult) {
        permissionBuilder.request(permissionResult);
    }

    public <T extends PermissionRequester<I, O>> T transform(Function<PermissionRequester<I, O>, T> function) {
        return function.apply(this);
    }

}

