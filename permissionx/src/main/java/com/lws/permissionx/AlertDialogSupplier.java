package com.lws.permissionx;


import android.app.AlertDialog;

/**
 * @author lws
 */
public interface AlertDialogSupplier {
    /**
     * 提供android平台 AlertDialog
     *
     * @return android平台 AlertDialog
     */
    AlertDialog.Builder getBuilder();
}
