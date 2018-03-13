package com.dt.wsjf.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dt.wsjf.R;
import com.dt.wsjf.base.BaseActivity;
import com.dt.wsjf.bean.TbContacts;
import com.dt.wsjf.utils.LogUtil;
import com.dt.wsjf.utils.PhoneNumUtil;
import com.dt.wsjf.view.BouncyBtnView;
import com.dt.wsjf.view.ItemView;
import com.dt.wsjf.view.VipRechargeDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;

import c.b.BP;
import c.b.PListener;
import commlib.xun.com.commlib.thread.CommThreadPool;

public class MainActivity extends BaseActivity {
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private static final int PRE_PHONE_NUM = 50;
    private static final int ADD_CONTACT_WHAT = 1001;
    private static final int DELETE_CONTACT_WHAT = 1002;

    private BouncyBtnView bouncyBtnView;
    private TextView adsTv;
    private ItemView vipItem, shuomingItem, saixuanItem, haoduanItem, qingchuItem, jiangshiItem, shareItem, aboutItem;
    private LinearLayout loadingLayout;

    private Boolean is2CallBack = false;// 是否双击退出

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_CONTACT_WHAT:
                    if (loadingLayout != null) {
                        loadingLayout.setVisibility(View.GONE);
                    }
                    Toast.makeText(getApplicationContext(), "已成功导入粉丝号码到通讯录", Toast.LENGTH_SHORT).show();
                    break;
                case DELETE_CONTACT_WHAT:
                    if (loadingLayout != null) {
                        loadingLayout.setVisibility(View.GONE);
                    }
                    Toast.makeText(getApplicationContext(), "已成功删除通讯录临时数据", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermission();

        initView();
    }

    private void initView() {
        bouncyBtnView = (BouncyBtnView) findViewById(R.id.jiafen_btn);
        adsTv = (TextView) findViewById(R.id.ads_tv);
        adsTv.setSelected(true);
        bouncyBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("开始添加联系人");
                loadingLayout.setVisibility(View.VISIBLE);
                CommThreadPool.poolExecute(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<TbContacts> list = new ArrayList<>();
                        for (int i = 0; i < PRE_PHONE_NUM; i++) {
                            TbContacts tbContacts = new TbContacts();
                            tbContacts.setName("测试数据" + i);
                            tbContacts.setNumber("138123456" + i);
                            list.add(tbContacts);
                        }
                        PhoneNumUtil.batchAddContact(getApplicationContext(), list);
                        LogUtil.d("结束添加联系人");
                        mHandler.sendEmptyMessage(ADD_CONTACT_WHAT);
                    }
                });
            }
        });

        vipItem = (ItemView) findViewById(R.id.vip_item);
        vipItem.setItemContent(R.drawable.vip_d, "升级VIP", "升级成VIP可以解除所有限制", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VipRechargeDialog vipRechargeDialog = new VipRechargeDialog(MainActivity.this);
                vipRechargeDialog.show();
            }
        });
        shuomingItem = (ItemView) findViewById(R.id.shuoming_item);
        shuomingItem.setItemContent(R.drawable.help_d, "使用帮助", "查看这里可以快速入门加粉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        saixuanItem = (ItemView) findViewById(R.id.saixuan_item);
        saixuanItem.setItemContent(R.drawable.saixuan, "筛选加粉", "可以让您更精准地加到需要的粉丝", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        haoduanItem = (ItemView) findViewById(R.id.haoduan_item);
        haoduanItem.setItemContent(R.drawable.haoduan_d, "号段加粉", "可以让您根据号段来添加粉丝", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        qingchuItem = (ItemView) findViewById(R.id.qingchu_item);
        qingchuItem.setItemContent(R.drawable.delete_d, "清除粉丝数据", "加粉后，可以通过这里一键还原通讯录数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("开始删除联系人");
                loadingLayout.setVisibility(View.VISIBLE);
                CommThreadPool.poolExecute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < PRE_PHONE_NUM; i++) {
                            PhoneNumUtil.deleteContact(getApplicationContext(), "测试数据" + i);
                        }
                        LogUtil.d("结束删除联系人");
                        mHandler.sendEmptyMessage(DELETE_CONTACT_WHAT);
                    }
                });
            }
        });
        jiangshiItem = (ItemView) findViewById(R.id.jiangshi_item);
        jiangshiItem.setItemContent(R.drawable.jiangshi, "清除死粉", "可以帮您迅速找到死粉与僵尸粉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        shareItem = (ItemView) findViewById(R.id.share_item);
        shareItem.setItemContent(R.drawable.share_d, "分享软件", "分享我们的软件，可以增加加粉名额哦~", new View.OnClickListener() {
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
        aboutItem = (ItemView) findViewById(R.id.about_item);
        aboutItem.setItemContent(R.drawable.about_d, "关于我们", "了解软件最新进展与版本，以及提出您的宝贵意见", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        loadingLayout = (LinearLayout) findViewById(R.id.loadingView);
        loadingLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Button payButton = (Button) findViewById(R.id.pay_btn);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(BP.PayType_Wechat);
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

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                bouncyBtnView.setVisibility(View.VISIBLE);
                bouncyBtnView.popAnimation(true);
            }
        }, 300);
    }

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

    private void checkAndRequestPermission() {
        boolean isAllGranted = checkPermissionAllGranted(
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
                this,
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

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                LogUtil.d("权限都同意了");
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                LogUtil.d("权限没有同意");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!is2CallBack) {
                is2CallBack = true;
                Toast.makeText(this, "再按一次退出微商快粉", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        is2CallBack = false;
                    }
                }, 2500);

            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
        return true;
    }
}
