package com.lws.permissionx;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.util.Map;


/**
 * @author lws
 */
public class InvisibleFragment extends Fragment {
    static final String TAG = "InvisibleFragment";

    private PermissionBuilder permissionBuilder;

    private final ActivityResultLauncher<String[]> requestMultiplePermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::handleResult);

    private void handleResult(Map<String, Boolean> result) {
        if (permissionBuilder != null) {
            permissionBuilder.handleResult(result);
            permissionBuilder.removeInvisibleFragment();
            permissionBuilder.restoreOrientation();
        }
    }

    public  void request(PermissionBuilder permissionBuilder) {
        this.permissionBuilder = permissionBuilder;
        this.permissionBuilder.lockOrientation();
        requestMultiplePermissions.launch(permissionBuilder.getPermissions());
    }
}