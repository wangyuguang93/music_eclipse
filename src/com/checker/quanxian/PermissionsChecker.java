package com.checker.quanxian;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by Xiamin on 2016/10/23.
 * 权限检查工具类，可复用
 */
public class PermissionsChecker {
    private final Context mContext;

    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 判断该数组中的权限是否均获取了，没有的话 打印出来
     * @param permissions
     * @return
     */
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                Log.i("PermissionsChecker","lacksPermission" + permission);
                return true;
            }
        }
        return false;
    }

    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
    public static int checkSelfPermission( Context context, String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }

        return context.checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid());
    }
}
