package com.dt.wsjf.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.dt.wsjf.utils.AppUtil;
import com.dt.wsjf.utils.BmobUtil;
import com.dt.wsjf.utils.LogUtil;
import com.tencent.bugly.Bugly;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import c.b.BP;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by xunwang on 2018/2/26.
 */

public class WsjfApplication extends Application {
    private static WsjfApplication mApplication = null;
    public static long currentServerTime;//每次启动拉一下服务器时间作为参考，本地时间可能不靠谱
    public static String adsStr;//公告信息，每次启动拉一次
    public static String priceInfoStr;//VIP付款信息，每次启动拉一次
    public static int addNumPreShare;//每次分享获得的增加数

    public static WsjfApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        if (!checkSignature()) {
            System.exit(-1);
            return;
        }

        if (shouldInit()) {
            UMConfigure.init(this, "5a9388fda40fa35b4a000097", "Common", UMConfigure.DEVICE_TYPE_PHONE, null);

            UMShareAPI.get(this);
            PlatformConfig.setWeixin("wxc4a3cf33eb6cae9d", "35f3896d6a4905dbfd72ed5fb9162857");
            PlatformConfig.setQQZone("1106746082", "du89nf9AeX0xmvWd");
            PlatformConfig.setSinaWeibo("2490403713", "a06371a35a465c283b9fbfec658d9dec", "http://www.baidu.com");

            Bmob.initialize(this, "a314e168943a3645670039b26a2d3cc6");
            BP.init("a314e168943a3645670039b26a2d3cc6");

            Bugly.init(getApplicationContext(), "ea3afe01a5", false);

            // 使用推送服务时的初始化操作
            BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
                @Override
                public void done(BmobInstallation bmobInstallation, BmobException e) {
                    if (e == null) {
                        LogUtil.d(bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId());
                    } else {
                        LogUtil.d(e.getMessage());
                    }
                }
            });
            // 启动推送服务
            BmobPush.startWork(this);

            BmobUtil.getServerTime();
            BmobUtil.getConfigInfo();
        }
    }

    private boolean checkSignature() {
        byte[] sig = a.getSign(this);

        String hash = a.digest(sig, "MD5").toUpperCase();
        LogUtil.d("hash --> " + hash);

        if (hash.equals("8D4BEB0217F044ADF0A79B2F8FE0D13D")) {
            return true;
        }

        return false;
    }


    public SharedPreferences getCommSharedPreferences(String tbl) {
        return getSharedPreferences(tbl, Context.MODE_PRIVATE);
    }

    /**
     * 返回服务器时间，如果为0则返回当前时间
     * 如果启动一天以上不退出 可能会有点逻辑问题 但不确定
     *
     * @return
     */
    public static long getCurrentTime() {
        if (currentServerTime != 0) {
            return currentServerTime;
        } else {
            return System.currentTimeMillis() / 1000;
        }
    }

    /**
     * 判断是否是主进程，防止重复初始化
     *
     * @return
     */
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
