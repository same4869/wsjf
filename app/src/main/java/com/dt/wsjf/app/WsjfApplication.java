package com.dt.wsjf.app;

import android.app.Application;

import com.tencent.bugly.Bugly;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import c.b.BP;
import cn.bmob.v3.Bmob;

/**
 * Created by xunwang on 2018/2/26.
 */

public class WsjfApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        UMConfigure.init(this, "5a9388fda40fa35b4a000097", "Common", UMConfigure.DEVICE_TYPE_PHONE, null);

        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("1106746082", "du89nf9AeX0xmvWd");
        PlatformConfig.setSinaWeibo("2490403713", "a06371a35a465c283b9fbfec658d9dec", "http://www.baidu.com");

        Bmob.initialize(this, "7d7e3edbad217c02d94bf22e3123739c");
        BP.init("7d7e3edbad217c02d94bf22e3123739c");

        Bugly.init(getApplicationContext(), "ea3afe01a5", false);
    }
}
