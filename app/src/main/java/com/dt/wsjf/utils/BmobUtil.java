package com.dt.wsjf.utils;

import android.content.Context;

import com.dt.wsjf.app.WsjfApplication;
import com.dt.wsjf.bean.CommConf;
import com.dt.wsjf.bean.PriceConfigBean;
import com.dt.wsjf.bean.UserInfo;
import com.dt.wsjf.sp.CommSetting;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by wangxun on 2018/3/13.
 */

public class BmobUtil {
    public interface BmobResultListener {
        void onSuccess(Object object);

        void onError(BmobException e);
    }

    /**
     * 根据deviceId去用户信息表中查有没有
     *
     * @param deviceId
     * @param bmobResultListener
     */
    public static void queryInfoById(String deviceId, final BmobResultListener bmobResultListener) {
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("deviceId", deviceId);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    LogUtil.d("queryInfoById 查到数据数:" + list.size());
                    if (list.size() > 0) {
                        if (bmobResultListener != null) {
                            bmobResultListener.onSuccess(list.get(0));
                        }
                    } else {
                        if (bmobResultListener != null) {
                            bmobResultListener.onSuccess(null);
                        }
                    }
                } else {
                    if (bmobResultListener != null) {
                        bmobResultListener.onError(e);
                    }
                    LogUtil.d("queryInfoById 失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 新用户，插入一条用户信息，其他的都是初始值
     *
     * @param deviceId
     * @param bmobResultListener
     */
    public static void addInfoFromId(final String deviceId, final BmobResultListener bmobResultListener) {
        UserInfo userInfo = new UserInfo();
        userInfo.setDeviceId(deviceId);
        userInfo.setNumsOfDay(100);
        userInfo.setVipShouldNums(0);
        userInfo.setVipLevel(0);
        userInfo.setVipEndTime(0);
        userInfo.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    if (bmobResultListener != null) {
                        bmobResultListener.onSuccess(null);
                    }
                    LogUtil.d("初始化数据成功 deviceId --> " + deviceId);
                } else {
                    if (bmobResultListener != null) {
                        bmobResultListener.onError(e);
                    }
                    LogUtil.d("初始化数据失败 deviceId --> " + deviceId);
                }
            }
        });
    }

    /**
     * 获得服务器时间
     */
    public static void getServerTime() {
        Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long aLong, BmobException e) {
                WsjfApplication.currentServerTime = aLong * 1000;
                LogUtil.d("serverTime : " + aLong * 1000);
            }
        });
    }

    /**
     * 根据VIP过期时间算出VIP剩余的天数
     *
     * @param vipEndTime
     */
    public static void getVipLastDay(final long vipEndTime, final BmobResultListener bmobResultListener) {
        if (vipEndTime == 0) {
            return;
        }
        long lastTime = vipEndTime - WsjfApplication.getCurrentTime();
        if (bmobResultListener != null) {
            bmobResultListener.onSuccess(lastTime / (1000 * 3600 * 24));
        }
    }

    /**
     * 获得今天应该还剩多少加粉数
     *
     * @param vipShouldNums
     * @param numsOfDay
     */
    public static int getTodayAddNums(Context context, int vipShouldNums, int numsOfDay) {
        //一天之内还没有比较过
        if (WsjfApplication.getCurrentTime() - CommSetting.getLastSyncTime() > 1000 * 60 * 60 * 24) {
            CommSetting.setLastSyncTime(WsjfApplication.getCurrentTime());
            if (vipShouldNums > numsOfDay) {
                syncNums(AppUtil.getIMEI(context));
                return vipShouldNums;
            }
            return numsOfDay;
        } else {
            return numsOfDay;
        }
    }

    /**
     * 获取公告，价格等配置信息
     */
    public static void getConfigInfo() {
        BmobQuery<CommConf> query = new BmobQuery<CommConf>();
        query.findObjects(new FindListener<CommConf>() {
            @Override
            public void done(List<CommConf> list, BmobException e) {
                if (e == null) {
                    LogUtil.d("CommConf 查到数据数:" + list.size());
                    if (list.size() > 0) {
                        WsjfApplication.adsStr = list.get(0).getAds();
                        WsjfApplication.priceInfoStr = list.get(0).getPriceConfig();
                        WsjfApplication.addNumPreShare = list.get(0).getAddNumPreShare();
                    }
                } else {
                    LogUtil.d("CommConf 失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * 根据支付信息去更新用户信息，支付完成后调用
     *
     * @param deviceId
     * @param dataBean
     */
    public static void updateInfoWhenPay(String deviceId, final PriceConfigBean.DataBean dataBean) {
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("deviceId", deviceId);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    LogUtil.d("updateInfoWhenPay 查到数据数:" + list.size());
                    if (list.size() > 0) {
                        LogUtil.d("updateInfoWhenPay  list.get(0).getObjectId():" + list.get(0).getObjectId());
                        UserInfo userInfo = new UserInfo();
                        userInfo.setVipLevel(dataBean.getVipLevel());
                        userInfo.setVipShouldNums(dataBean.getVipCounts());
                        userInfo.setNumsOfDay(dataBean.getVipDays());
                        userInfo.setVipEndTime(WsjfApplication.currentServerTime + dataBean.getVipDays() * 24 * 60 * 60 * 1000);
                        userInfo.update(list.get(0).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    LogUtil.d("updateInfoWhenPay 更新成功");
                                } else {
                                    LogUtil.d("updateInfoWhenPay 更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    } else {
                        LogUtil.d("updateInfoWhenPay 没有这个用户");
                    }
                } else {
                    LogUtil.d("updateInfoWhenPay 请求失败 " + e.getMessage());
                }
            }
        });
    }

    /**
     * 当一次同步完成时，需要扣除50个numsOfDay
     *
     * @param deviceId
     */
    public static void whenSync50End(String deviceId, final BmobResultListener listener) {
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("deviceId", deviceId);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    LogUtil.d("updateInfoWhenPay 查到数据数:" + list.size());
                    if (list.size() > 0) {
                        LogUtil.d("updateInfoWhenPay  list.get(0).getObjectId():" + list.get(0).getObjectId());
                        UserInfo userInfo = new UserInfo();
                        int newNum = list.get(0).getNumsOfDay();
                        userInfo.setNumsOfDay(newNum - 50);
                        userInfo.update(list.get(0).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    listener.onSuccess(null);
                                    LogUtil.d("whenSync50End 更新成功");
                                } else {
                                    listener.onError(null);
                                    LogUtil.d("whenSync50End 更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    } else {
                        listener.onError(null);
                        LogUtil.d("whenSync50End 没有这个用户");
                    }
                } else {
                    listener.onError(null);
                    LogUtil.d("whenSync50End 请求失败 " + e.getMessage());
                }
            }
        });
    }

    /**
     * 当VipShouldNums大于NumsOfDay时，需要远程也同步一下，一天一次
     *
     * @param deviceId
     */
    public static void syncNums(String deviceId) {
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("deviceId", deviceId);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    LogUtil.d("syncNums 查到数据数:" + list.size());
                    if (list.size() > 0) {
                        LogUtil.d("syncNums  list.get(0).getObjectId():" + list.get(0).getObjectId());
                        UserInfo userInfo = new UserInfo();
                        int newNum = list.get(0).getNumsOfDay();
                        userInfo.setNumsOfDay(list.get(0).getVipShouldNums());
                        userInfo.update(list.get(0).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    LogUtil.d("syncNums 更新成功");
                                } else {
                                    LogUtil.d("syncNums 更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    } else {
                        LogUtil.d("syncNums 没有这个用户");
                    }
                } else {
                    LogUtil.d("syncNums 请求失败 " + e.getMessage());
                }
            }
        });
    }

    /**
     * 当分享是增加的加粉数
     * @param deviceId
     * @param listener
     */
    public static void whenShareAddNum(String deviceId, final BmobResultListener listener) {
        if (WsjfApplication.addNumPreShare == 0) {
            return;
        }
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo("deviceId", deviceId);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    LogUtil.d("whenShareAddNum 查到数据数:" + list.size());
                    if (list.size() > 0) {
                        LogUtil.d("whenShareAddNum  list.get(0).getObjectId():" + list.get(0).getObjectId());
                        UserInfo userInfo = new UserInfo();
                        int newNum = list.get(0).getNumsOfDay();
                        userInfo.setNumsOfDay(newNum + WsjfApplication.addNumPreShare);
                        userInfo.update(list.get(0).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    listener.onSuccess(null);
                                    LogUtil.d("whenShareAddNum 更新成功");
                                } else {
                                    listener.onError(null);
                                    LogUtil.d("whenShareAddNum 更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    } else {
                        listener.onError(null);
                        LogUtil.d("whenShareAddNum 没有这个用户");
                    }
                } else {
                    listener.onError(null);
                    LogUtil.d("whenShareAddNum 请求失败 " + e.getMessage());
                }
            }
        });
    }
}
