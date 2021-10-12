package com.lws.permissionx;

import androidx.appcompat.app.AlertDialog;

/**
 * @author lws
 */
public interface AlertDialogSupplierX {
    /**
     * 提供androidX AlertDialog
     *
     * @return androidX AlertDialog
     */
    AlertDialog.Builder getBuilder();
}
