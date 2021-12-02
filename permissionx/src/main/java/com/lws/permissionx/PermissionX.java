package com.lws.permissionx;

import android.content.Context;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * @author lws
 */
public class PermissionX {

    /**
     * 限制同一时间最多有一个权限请求/弹框
     */
    static boolean inRequesting;

    @CheckResult
    public static PermissionMediator init(@NonNull FragmentActivity activity) {
        return new PermissionMediator(activity);
    }

    @CheckResult
    public static PermissionMediator init(@NonNull Fragment fragment) {
        return new PermissionMediator(fragment);
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        return PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
    }

    public static boolean hasPermissions(@NonNull Context context, @NonNull String permission, @NonNull String... permissions) {
        if (!hasPermission(context, permission)) {
            return false;
        }
        for (String tempPermission : permissions) {
            if (!hasPermission(context, tempPermission)) {
                return false;
            }
        }
        return true;
    }

    public static DefaultConfig getDefaultConfig() {
        return DefaultConfig.getInstance();
    }
}
