package com.dt.wsjf.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dt.wsjf.R;
import com.dt.wsjf.base.BaseActivity;
import com.dt.wsjf.bean.CommConf;
import com.dt.wsjf.utils.AppUtil;
import com.dt.wsjf.utils.BmobUtil;
import com.dt.wsjf.utils.LogUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class DebugActivity extends BaseActivity {
    private Button addUserInfoBtn, initConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        addUserInfoBtn = (Button) findViewById(R.id.debug_add_userinfo);
        addUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUtil.addInfoFromId(AppUtil.getIMEI(getApplicationContext()), new BmobUtil.BmobResultListener() {
                    @Override
                    public void onSuccess(Object object) {
                        LogUtil.d("添加成功");
                    }

                    @Override
                    public void onError(BmobException e) {
                        LogUtil.d("添加失败");
                    }
                });
            }
        });
        initConfig = (Button) findViewById(R.id.debug_config);
        initConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initConfig();
            }
        });
    }

    private void initConfig() {
        CommConf commConf = new CommConf();
        commConf.setAds("热烈庆祝微商快粉1.0成功上线，从即日起，每分享本软件到微信或者朋友圈，都可以获得10名额外加粉数，多分享多得，更多活动请随时留言本公告栏。");
        String priceConfig = "{\n" +
                "    \"data\": [{\n" +
                "        \"title\": \"(限时) 体验会员 \",\n" +
                "        \"content\": \"体验会员每天可加100粉，可使用3天\",\n" +
                "        \"originPrice\": 9.9,\n" +
                "        \"price\": 3.9,\n" +
                "        \"vipDays\": 3,\n" +
                "        \"vipCounts\": 100,\n" +
                "        \"vipLevel\": 1\n" +
                "    }, {\n" +
                "        \"title\": \"青铜会员\",\n" +
                "        \"content\": \"青铜会员每天可加200粉，可使用30天 \",\n" +
                "        \"originPrice\": 59.9,\n" +
                "        \"price\": 29.9,\n" +
                "        \"vipDays\": 30,\n" +
                "        \"vipCounts\": 200,\n" +
                "        \"vipLevel\": 2\n" +
                "    }, {\n" +
                "        \"title\": \"白银会员 \",\n" +
                "        \"content\": \"白银会员每天可加300粉，可使用90天 \",\n" +
                "        \"originPrice\": 109.9,\n" +
                "        \"price\": 69.9,\n" +
                "        \"vipDays\": 90,\n" +
                "        \"vipCounts\": 300,\n" +
                "        \"vipLevel\": 3\n" +
                "    }, {\n" +
                "        \"title\": \"黄金会员 \",\n" +
                "        \"content\": \"黄金会员每天可加500粉，使用天数永久 \",\n" +
                "        \"originPrice\": 199.9,\n" +
                "        \"price\": 79.9,\n" +
                "        \"vipDays\": 9999,\n" +
                "        \"vipCounts\": 500,\n" +
                "        \"vipLevel\": 4\n" +
                "    }]\n" +
                "}";
        String shareInfo = "{\n" +
                "\t\"shareTilte\": \"微商快粉--十天加满你的微信好友\",\n" +
                "\t\"shareContent\": \"我在使用一款超好用的人脉管理与营销工具，让微信的操作批量自动化，加粉、清粉，上面有上百万的用户信息，让我的加粉效率大大提高，你也快来下载吧：http://www.baidu.com\",\n" +
                "\t\"shareUrl\": \"http://www.baidu.com\"\n" +
                "}";
        commConf.setPriceConfig(priceConfig);
        commConf.setAddNumPreShare(10);
        commConf.setShareInfo(shareInfo);
        commConf.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    LogUtil.d("config初始化数据成功");
                } else {
                    LogUtil.d("config初始化数据失败");
                }
            }
        });
    }
}
