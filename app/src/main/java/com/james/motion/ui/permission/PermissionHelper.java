package com.james.motion.ui.permission;

import android.app.Activity;

public class PermissionHelper {
    /**
     * 申请权限，固定重试提示
     */
    public static void requestPermissions(Activity mActivity, String[] permission, final PermissionListener callback) {
        APermission.builder(mActivity)
                .setPermissions(permission)
                .callBack(callback).request();
    }
    /**
     * 申请权限，自定义重试提示文字
     */
    public static void requestPermissions(Activity mActivity, String[] permission, String tipContent, final PermissionListener callback) {
        APermission.builder(mActivity)
                .setPermissions(permission)
                .setTipContent(tipContent)
                .callBack(callback).request();
    }

    public static boolean hasMustPermission(Activity activity) {
        return PermissionUtil.hasSelfPermissions(activity, Permissions.PERMISSIONS_FIRST);
    }
}
