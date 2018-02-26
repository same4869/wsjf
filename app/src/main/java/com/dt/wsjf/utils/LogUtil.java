package com.dt.wsjf.utils;

import android.util.Log;

import com.dt.wsjf.config.Constants;

import static com.dt.wsjf.config.Constants.IS_DEBUG;

/**
 * Created by xunwang on 2018/2/26.
 */

public class LogUtil {
    public static void d(String msg) {
        if (IS_DEBUG) {
            Log.d(Constants.TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, msg);
        }
    }
}
