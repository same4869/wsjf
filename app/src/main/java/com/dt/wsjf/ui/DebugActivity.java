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
                //initConfig();
            }
        });
    }

    private void initConfig() {
        CommConf commConf = new CommConf();
        commConf.setAds("微商快粉公告微商快粉公告微商快粉公告微商快粉公告微商快粉公告");
        String priceConfig = "{\n" +
                "\t\"data\": [{\n" +
                "\t\t\"title\": \"(限时) 体验会员 \",\n" +
                "\t\t\"content\": \"体验会员每天可加100粉，可使用3天\",\n" +
                "\t\t\"originPrice\": 9.9,\n" +
                "\t\t\"price\": 3.9\n" +
                "\t}, {\n" +
                "\t\t\"title\": \"青铜会员\",\n" +
                "\t\t\"content\": \"青铜会员每天可加200粉，可使用30天 \",\n" +
                "\t\t\"originPrice\": 59.9,\n" +
                "\t\t\"price\": 29.9\n" +
                "\t}, {\n" +
                "\t\t\"title\": \"白银会员 \",\n" +
                "\t\t\"content\": \"白银会员每天可加300粉，可使用90天 \",\n" +
                "\t\t\"originPrice\": 109.9,\n" +
                "\t\t\"price\": 69.9\n" +
                "\t}, {\n" +
                "\t\t\"title\": \"黄金会员 \",\n" +
                "\t\t\"content\": \"黄金会员每天可加500粉，使用天数永久 \",\n" +
                "\t\t\"originPrice\": 199.9,\n" +
                "\t\t\"price\": 79.9\n" +
                "\t}]\n" +
                "}";
        commConf.setPriceConfig(priceConfig);
        commConf.setAddNumPreShare(10);
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
