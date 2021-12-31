package com.lws.permissionx;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lws
 */
public class PermissionResult {

    PermissionResult() {
    }

    void addGranted(String permission) {
        grantedList.add(permission);
    }

    void addDenied(String permission) {
        deniedList.add(permission);
    }

    private final List<String> grantedList = new ArrayList<>();
    private final List<String> deniedList = new ArrayList<>();

    public boolean isAllGranted() {
        return deniedList.isEmpty();
    }

    public List<String> getGrantedList() {
        return grantedList;
    }

    public List<String> getDeniedList() {
        return deniedList;
    }

    @NonNull
    @Override
    public String toString() {
        return "PermissionResult{" +
                "grantedList=" + grantedList +
                ", deniedList=" + deniedList +
                '}';
    }
}
