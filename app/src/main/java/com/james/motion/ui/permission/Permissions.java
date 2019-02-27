package com.james.motion.ui.permission;

import android.Manifest;

public interface Permissions {
    /**
     * 权限申请CODE
     */
    int PERMISSION_REQUEST_CODE = 400;

    int PERMISSION_SETTING_CODE = 401;

    /**
     * 进入app请求的权限
     */
    String[] PERMISSIONS_FIRST = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    String[] PERMISSIONS_HOME = new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    String[] PERMISSIONS_BIOSPSY_AUDIO = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String[] PERMISSIONS_BIOSPSY = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String[] PERMISSIONS_PHONE_STATE = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    String[] PERMISSIONS_CAMERA = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String[] PERMISSIONS_GALLERY = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String[] PERMISSIONS_RECORD_AUDIO = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String[] PERMISSIONS_SMS = new String[]{
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS
    };
    String[] PERMISSIONS_CONTACTS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};
    String[] PERMISSIONS_LOCATION = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

}
