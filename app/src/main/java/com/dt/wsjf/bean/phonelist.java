package com.dt.wsjf.bean;

import cn.bmob.v3.BmobObject;

/**
 * 联系人类，这里没遵循命名规范是因为bmob上的表已经建好需要对应
 * Created by wangxun on 2018/3/7.
 */

public class phonelist extends BmobObject {
    private String nick_name;
    private String mobile;
    private int sex; //女2男1
    private String city;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
