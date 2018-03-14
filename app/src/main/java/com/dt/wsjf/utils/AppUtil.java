package com.dt.wsjf.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by wangxun on 2018/3/13.
 */

public class AppUtil {
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(800);
    }

    public static boolean isFastDoubleClick(int minTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < minTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    public static void checkAndRequestPermission(Activity context, int MY_PERMISSION_REQUEST_CODE) {
        boolean isAllGranted = AppUtil.checkPermissionAllGranted(context,
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }
        );
        if (isAllGranted) {
            return;
        }

        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                context,
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                MY_PERMISSION_REQUEST_CODE
        );
    }
}
