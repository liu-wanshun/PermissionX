package com.lws.permissionx;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * @author lws
 */
public class PermissionX {

    public static PermissionMediator init(@NonNull FragmentActivity activity) {
        return new PermissionMediator(activity);
    }

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
