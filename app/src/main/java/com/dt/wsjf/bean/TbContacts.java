package com.dt.wsjf.bean;

import java.io.Serializable;

/**
 * Created by wangxun on 2018/3/7.
 */

public class TbContacts implements Serializable{
    private String name;
    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
