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

    @CheckResult
    public static PermissionMediator init(@NonNull FragmentActivity activity) {
        return new PermissionMediator(activity);
    }

    @CheckResult
    public static PermissionMediator init(@NonNull Fragment fragment) {
        return new PermissionMediator(fragment);
    }

    public static boolean hasPermissions(@NonNull Context context, @NonNull String... permissions) {
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission) != PermissionChecker.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static DefaultConfig getDefaultConfig() {
        return DefaultConfig.getInstance();
    }
}
