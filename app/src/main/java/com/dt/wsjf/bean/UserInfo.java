package com.dt.wsjf.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by wangxun on 2018/3/15.
 */

public class UserInfo extends BmobObject {
    private String deviceId;
    private int numsOfDay;
    private int vipShouldNums;
    private int vipLevel; //非会员0，青铜1，白银2，黄金3
    private String vipEndTime;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getNumsOfDay() {
        return numsOfDay;
    }

    public void setNumsOfDay(int numsOfDay) {
        this.numsOfDay = numsOfDay;
    }

    public int getVipShouldNums() {
        return vipShouldNums;
    }

    public void setVipShouldNums(int vipShouldNums) {
        this.vipShouldNums = vipShouldNums;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(String vipEndTime) {
        this.vipEndTime = vipEndTime;
    }
}
