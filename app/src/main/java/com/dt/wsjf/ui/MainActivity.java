package com.dt.wsjf.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dt.wsjf.R;
import com.dt.wsjf.base.BaseActivity;
import com.dt.wsjf.utils.LogUtil;
import com.dt.wsjf.utils.PhoneNumUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import c.b.BP;
import c.b.PListener;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        initView();
    }

    private void initView() {
        Button button = (Button) findViewById(R.id.share_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(MainActivity.this, R.mipmap.ic_launcher);//资源文件
                UMImage thumb = new UMImage(MainActivity.this, R.mipmap.ic_launcher);
                image.setThumb(thumb);
                image.compressStyle = UMImage.CompressStyle.SCALE;
                UMWeb web = new UMWeb("http://www.baidu.com");
                web.setTitle("This is music title");//标题
                web.setThumb(thumb);  //缩略图
                web.setDescription("my description");//描述
                new ShareAction(MainActivity.this).withText("hello").withExtra(image).withMedia(web).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.ALIPAY, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.SMS)
                        .setCallback(umShareListener).open();
            }
        });
        Button payButton = (Button) findViewById(R.id.pay_btn);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(BP.PayType_Wechat);
            }
        });
        Button addPhoneNum = (Button) findViewById(R.id.addPhoneNum_btn);
        addPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumUtil.addContact(getApplicationContext(),"测试数据1","13888888888");
            }
        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            LogUtil.d("umShareListener onError --> " + share_media.getName() + " throwable.getMessage() --> " + throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    };

    /**
     * 调用支付
     *
     * @param payType 支付类型，BP.PayType_Alipay、BP.PayType_Wechat、BP.PayType_QQ
     */
    void pay(final int payType) {
        final String name = "支付测试";

        // 仍然可以通过这种方式支付，其中true为支付宝，false为微信
        // BP.pay(name, getBody(), getPrice(), false, new PListener());

        BP.pay(name, "随便", 0.01, payType, new PListener() {

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(MainActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                LogUtil.d("reason --> " + reason);
                Toast.makeText(MainActivity.this, "支付中断!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
    }
}
