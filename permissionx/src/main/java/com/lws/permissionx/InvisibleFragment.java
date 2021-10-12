package com.lws.permissionx;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;


/**
 * @author lws
 */
public class InvisibleFragment extends Fragment {
    static final String TAG = "InvisibleFragment";

    private PermissionBuilder permissionBuilder;
    private final ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), this::handleResult);

    private final ActivityResultLauncher<String[]> requestMultiplePermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::handleResult);

    private <O> void handleResult(O result) {
        if (permissionBuilder != null) {
            permissionBuilder.getPermissionResultCallback().onActivityResult(result);
            permissionBuilder.removeInvisibleFragment();
            permissionBuilder.restoreOrientation();
        }
    }

    public <I, O> void request(PermissionBuilder<I, O> permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
        this.permissionBuilder.lockOrientation();

        if (permissionBuilder.getPermission() instanceof String) {
            requestPermission.launch((String) permissionBuilder.getPermission());
        } else {
            requestMultiplePermissions.launch((String[]) permissionBuilder.getPermission());
        }
    }
}