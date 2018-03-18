package com.dt.wsjf.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.dt.wsjf.app.WsjfApplication;

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
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
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
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                },
                MY_PERMISSION_REQUEST_CODE
        );
    }

    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        String imei;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            imei = "";
        }
        return imei;
    }


    /**
     * 判断网络情况
     *
     * @return false 表示没有网络 true 表示有网络
     */
    public static boolean isNetworkAvalible() {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) WsjfApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();
            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
