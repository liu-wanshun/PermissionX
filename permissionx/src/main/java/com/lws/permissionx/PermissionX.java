package com.lws.permissionx;

import android.content.Context;

import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * @author lws
 */
public class PermissionX {

    public static PermissionMediator init(FragmentActivity activity) {
        return new PermissionMediator(activity);
    }

    public static PermissionMediator init(Fragment fragment) {
        return new PermissionMediator(fragment);
    }

    public static boolean hasPermission(Context context, String permission) {
        return PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    static <T> boolean isGranted(Context context, T permission) {
        if (permission instanceof String) {
            return hasPermission(context, (String) permission);
        } else if (permission instanceof String[]) {
            return hasPermissions(context, (String[]) permission);
        } else {
            return true;
        }
    }

}
