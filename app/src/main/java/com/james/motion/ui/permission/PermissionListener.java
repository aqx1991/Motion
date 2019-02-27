package com.james.motion.ui.permission;

public abstract class PermissionListener {
    /**
     * 权限通过
     */
    public abstract void onPassed();

    /**
     * 权限拒绝
     * neverAsk:  不再询问
     *
     * @return 如果要覆盖原有提示则返回true
     */
    public boolean onDenied(boolean neverAsk) {
        return false;
    }

}