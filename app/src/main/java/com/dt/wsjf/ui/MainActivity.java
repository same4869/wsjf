package com.dt.wsjf.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dt.wsjf.R;
import com.dt.wsjf.app.WsjfApplication;
import com.dt.wsjf.base.BaseActivity;
import com.dt.wsjf.bean.PriceConfigBean;
import com.dt.wsjf.bean.UserInfo;
import com.dt.wsjf.bean.phonelist;
import com.dt.wsjf.json.JSONFormatExcetion;
import com.dt.wsjf.json.JSONToBeanHandler;
import com.dt.wsjf.utils.AppUtil;
import com.dt.wsjf.utils.BmobUtil;
import com.dt.wsjf.utils.LogUtil;
import com.dt.wsjf.utils.PayUtil;
import com.dt.wsjf.utils.PhoneNumUtil;
import com.dt.wsjf.view.BouncyBtnView;
import com.dt.wsjf.view.CommDialog;
import com.dt.wsjf.view.ItemView;
import com.dt.wsjf.view.VipRechargeDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

import c.b.BP;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import commlib.xun.com.commlib.thread.CommThreadPool;

import static com.dt.wsjf.config.Constants.HELP_INDEX_URL;
import static com.dt.wsjf.ui.CommWebActivity.COMM_WEB_URL;
import static com.dt.wsjf.ui.SelectAddActivity.RESULT_CITY_KEY;
import static com.dt.wsjf.ui.SelectAddActivity.RESULT_SEX_KEY;

public class MainActivity extends BaseActivity implements VipRechargeDialog.VipRechargerItemCallBack {
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private static final int PRE_PHONE_NUM = 50;
    private static final int ADD_CONTACT_WHAT = 1001;
    private static final int DELETE_CONTACT_WHAT = 1002;
    private static final int REQUEST_SELECT_ADD_CODE = 1003;

    private BouncyBtnView bouncyBtnView;
    private TextView adsTv;
    private ItemView vipItem, shuomingItem, saixuanItem, haoduanItem, qingchuItem, jiangshiItem, shareItem, aboutItem;
    private LinearLayout loadingLayout;
    private TextView jiaFenNumTip;
    private TextView vipTv, vipEndTimeTv;
    private TextView filterModeTv;

    private List<phonelist> phonelists = new ArrayList<>();
    private String deviceId;
    private Boolean is2CallBack = false;// 是否双击退出
    private UserInfo userInfo;
    private String city;
    private int sex;
    private int totleCount;
    private int filterMode = 0; //0是正常，1是筛选，2是号段

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_CONTACT_WHAT:
                    if (loadingLayout != null) {
                        loadingLayout.setVisibility(View.GONE);
                    }
                    BmobUtil.whenSync50End(AppUtil.getIMEI(getApplicationContext()), new BmobUtil.BmobResultListener() {
                        @Override
                        public void onSuccess(Object object) {
                            initData();
                            getPhoneListInfo((int) (totleCount * Math.random()), city, sex);
                        }

                        @Override
                        public void onError(BmobException e) {

                        }
                    });
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

        AppUtil.checkAndRequestPermission(this, MY_PERMISSION_REQUEST_CODE);

        initView();
        initData();
        getPhoneListCount();
    }

    private void initData() {
        deviceId = AppUtil.getIMEI(getApplicationContext());
        LogUtil.d("devicesId --> " + deviceId);
        BmobUtil.queryInfoById(deviceId, new BmobUtil.BmobResultListener() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {//已经有该用户的信息了
                    userInfo = (UserInfo) object;
                    setVipStatus(userInfo.getVipLevel());
                    jiaFenNumTip.setText("您今天还有" + BmobUtil.getTodayAddNums(getApplicationContext(), userInfo.getVipShouldNums(), userInfo.getNumsOfDay()) + "个加粉名额");
                    BmobUtil.getVipLastDay(Long.parseLong(userInfo.getVipEndTime()), new BmobUtil.BmobResultListener() {
                        @Override
                        public void onSuccess(Object object) {
                            vipEndTimeTv.setText("VIP剩余时间:" + object + "天");
                        }

                        @Override
                        public void onError(BmobException e) {
                            LogUtil.d("getVipLastDay失败");
                        }
                    });

                } else {//没有该用户的信息，创建一条默认的
                    if (!TextUtils.isEmpty(deviceId)) {
                        BmobUtil.addInfoFromId(deviceId, new BmobUtil.BmobResultListener() {
                            @Override
                            public void onSuccess(Object object) {
                                initData();
                            }

                            @Override
                            public void onError(BmobException e) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "数据初始化失败,请同意权限请求并重启APP", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(BmobException e) {
                Toast.makeText(getApplicationContext(), "数据初始化失败，请检查网络并重新进入app", Toast.LENGTH_SHORT).show();
                LogUtil.d("queryInfoById onError e --> " + e.getMessage());
            }
        });
    }

    private void setVipStatus(int level) {
        switch (level) {
            case 0:
                vipTv.setVisibility(View.VISIBLE);
                vipTv.setText("普通用户");
                break;
            case 1:
                vipTv.setVisibility(View.VISIBLE);
                vipTv.setText("体验会员");
                break;
            case 2:
                vipTv.setVisibility(View.VISIBLE);
                vipTv.setText("青铜会员");
                break;
            case 3:
                vipTv.setVisibility(View.VISIBLE);
                vipTv.setText("白银会员");
                break;
            case 4:
                vipTv.setVisibility(View.VISIBLE);
                vipTv.setText("黄金会员");
                break;
        }
    }

    private void initView() {
        bouncyBtnView = (BouncyBtnView) findViewById(R.id.jiafen_btn);
        jiaFenNumTip = (TextView) findViewById(R.id.jiafen_left_tip);
        adsTv = (TextView) findViewById(R.id.ads_tv);
        adsTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (WsjfApplication.adsStr != null) {
                    adsTv.setText(WsjfApplication.adsStr);
                    adsTv.setSelected(true);
                }
            }
        }, 2000);
        bouncyBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo != null) {
                    if (userInfo.getNumsOfDay() >= 50) {
                        showAddOrDeleteDialog(0);
                    } else {
                        Toast.makeText(getApplicationContext(), "您今天的加粉名额不足", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "数据初始化失败，请检查网络并重新进入app", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vipItem = (ItemView) findViewById(R.id.vip_item);
        vipItem.setItemContent(R.drawable.vip_d, "升级VIP", "升级成VIP可以解除所有限制", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WsjfApplication.priceInfoStr != null) {
                    try {
                        PriceConfigBean priceConfigBean = JSONToBeanHandler.fromJsonString(WsjfApplication.priceInfoStr, PriceConfigBean.class);

                        VipRechargeDialog vipRechargeDialog = new VipRechargeDialog(MainActivity.this, priceConfigBean.getData());
                        vipRechargeDialog.setVipRechargerItemCallBack(MainActivity.this);
                        vipRechargeDialog.show();
                    } catch (JSONFormatExcetion jsonFormatExcetion) {
                        jsonFormatExcetion.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "数据初始化失败，请检查网络并重新进入app", Toast.LENGTH_SHORT).show();
                }
            }
        });
        shuomingItem = (ItemView) findViewById(R.id.shuoming_item);
        shuomingItem.setItemContent(R.drawable.help_d, "使用帮助", "查看这里可以快速入门加粉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommWebActivity.class);
                intent.putExtra(COMM_WEB_URL, HELP_INDEX_URL);
                startActivity(intent);
            }
        });
        saixuanItem = (ItemView) findViewById(R.id.saixuan_item);
        saixuanItem.setItemContent(R.drawable.saixuan, "筛选加粉", "可以让您更精准地加到需要的粉丝", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo != null && userInfo.getVipLevel() > 0) {
                    Intent intent = new Intent(MainActivity.this, SelectAddActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_ADD_CODE);
                } else {
                    Toast.makeText(getApplicationContext(), "筛选功能所有会员均可免费试用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        haoduanItem = (ItemView) findViewById(R.id.haoduan_item);
        haoduanItem.setItemContent(R.drawable.haoduan_d, "号段加粉", "可以让您根据号段来添加粉丝", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "即将上线，上线后所有会员均可免费使用", Toast.LENGTH_SHORT).show();
            }
        });
        qingchuItem = (ItemView) findViewById(R.id.qingchu_item);
        qingchuItem.setItemContent(R.drawable.delete_d, "清除粉丝数据", "加粉后，可以通过这里一键还原通讯录数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddOrDeleteDialog(1);
            }
        });
        jiangshiItem = (ItemView) findViewById(R.id.jiangshi_item);
        jiangshiItem.setItemContent(R.drawable.jiangshi, "清除死粉", "可以帮您迅速找到死粉与僵尸粉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "即将上线，上线后所有会员均可免费使用", Toast.LENGTH_SHORT).show();
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
                web.setTitle("微商快粉--十天加满你的微信好友");//标题
                web.setThumb(thumb);  //缩略图
                web.setDescription("我在使用一款超好用的人脉管理与营销工具，让微信的操作批量自动化，加粉、清粉，上面有上百万的用户信息，让我的加粉效率大大提高，你也快来下载吧：");//描述
                new ShareAction(MainActivity.this).withText("微商快粉").withExtra(image).withMedia(web).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA, SHARE_MEDIA.ALIPAY, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.SMS)
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
        vipTv = (TextView) findViewById(R.id.vip_tx);
        vipEndTimeTv = (TextView) findViewById(R.id.vip_end_time_tx);
        filterModeTv = (TextView) findViewById(R.id.filter_mode_tx);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            LogUtil.d("umShareListener onStart");
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            LogUtil.d("umShareListener onResult");
            BmobUtil.whenShareAddNum(AppUtil.getIMEI(getApplicationContext()), new BmobUtil.BmobResultListener() {
                @Override
                public void onSuccess(Object object) {
                    Toast.makeText(getApplicationContext(), "分享成功，成功获得额外加粉名额" + WsjfApplication.addNumPreShare + "个", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(BmobException e) {

                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            LogUtil.d("umShareListener onError --> " + share_media.getName() + " throwable.getMessage() --> " + throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            LogUtil.d("umShareListener onCancel");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_ADD_CODE && data != null && resultCode == RESULT_OK) {
            city = data.getStringExtra(RESULT_CITY_KEY);
            sex = data.getIntExtra(RESULT_SEX_KEY, 0);
            getPhoneListInfo((int) (totleCount * Math.random()), city, sex);
            filterMode = 1;
            filterModeTv.setText("筛选模式:筛选");
        }
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

    private void startAddContact() {
        if (phonelists.size() < 1) {
            Toast.makeText(getApplicationContext(), "请重新启动app再次尝试", Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtil.d("开始添加联系人");
        loadingLayout.setVisibility(View.VISIBLE);
        CommThreadPool.poolExecute(new Runnable() {
            @Override
            public void run() {
                PhoneNumUtil.batchAddContact(getApplicationContext(), phonelists);
                LogUtil.d("结束添加联系人");
                mHandler.sendEmptyMessage(ADD_CONTACT_WHAT);
            }
        });
    }

    private void startClearContact() {
        LogUtil.d("开始删除联系人");
        loadingLayout.setVisibility(View.VISIBLE);
        CommThreadPool.poolExecute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < PRE_PHONE_NUM; i++) {
                    PhoneNumUtil.deleteContact(getApplicationContext(), "微商快粉");
                }
                LogUtil.d("结束删除联系人");
                mHandler.sendEmptyMessage(DELETE_CONTACT_WHAT);
            }
        });
    }

    /**
     * 添加删除联系人时二次确认
     *
     * @param type 0是添加，1是删除
     */
    private void showAddOrDeleteDialog(final int type) {
        if (type == 0) {
            commDialog = new CommDialog(this, "温馨提示", "确认要开始加粉吗？", false);
        } else {
            commDialog = new CommDialog(this, "温馨提示", "确认要开始清理通讯录吗(不会影响原来数据)？", false);
        }
        commDialog.setCanceledOnTouchOutside(false);
        commDialog.show();
        commDialog.hideTitle();
        commDialog.setLeftButtonText("取消");
        commDialog.setRightButtonText("确认");
        commDialog.setLeftButtonPositive(false);
        commDialog.setRightButtonPositive(true);
        commDialog.setRightListener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelDialog();
                if (type == 0) {
                    startAddContact();
                } else {
                    startClearContact();
                }
            }
        });
        commDialog.setLeftListener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelDialog();
            }
        });
    }

    private void getPhoneListCount() {
        BmobQuery<phonelist> query = new BmobQuery<phonelist>();
        query.count(phonelist.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if (e == null) {
                    LogUtil.d("getPhoneListCount count对象个数为：" + count);
                    totleCount = count;
                    getPhoneListInfo((int) (count * Math.random()), null, 0);
                } else {
                    LogUtil.d("getPhoneListCount 失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void getPhoneListInfo(int max, String city, int sex) {
        LogUtil.d("getPhoneListInfo city:" + city + " sex:" + sex + " indexmax:" + max);
        BmobQuery<phonelist> query = new BmobQuery<phonelist>();
        query.addWhereGreaterThan("id", max);
        if (!TextUtils.isEmpty(city)) {
            query.addWhereEqualTo("city", city);
        }
        if (sex == 1 || sex == 2) {
            query.addWhereEqualTo("sex", sex);
        }
        query.setLimit(50);
        query.findObjects(new FindListener<phonelist>() {
            @Override
            public void done(List<phonelist> object, BmobException e) {
                if (e == null) {
                    phonelists = object;
                    LogUtil.d("getPhoneListInfo phonelists初始化 size --> " + phonelists.size());
                } else {
                    LogUtil.d("getPhoneListInfo 失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onVipItemClick(PriceConfigBean.DataBean dataBean, int position) {
        PayUtil.pay(getApplicationContext(), dataBean, BP.PayType_Alipay);
    }
}
