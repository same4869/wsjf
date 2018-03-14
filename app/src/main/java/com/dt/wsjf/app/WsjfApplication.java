package com.dt.wsjf.app;

import android.app.Application;

import com.dt.wsjf.utils.LogUtil;
import com.tencent.bugly.Bugly;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

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

        UMConfigure.init(this, "5a9388fda40fa35b4a000097", "Common", UMConfigure.DEVICE_TYPE_PHONE, null);

        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wxc4a3cf33eb6cae9d", "35f3896d6a4905dbfd72ed5fb9162857");
        PlatformConfig.setQQZone("1106746082", "du89nf9AeX0xmvWd");
        PlatformConfig.setSinaWeibo("2490403713", "a06371a35a465c283b9fbfec658d9dec", "http://www.baidu.com");

        Bmob.initialize(this, "7d7e3edbad217c02d94bf22e3123739c");
        BP.init("7d7e3edbad217c02d94bf22e3123739c");

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
}
