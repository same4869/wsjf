package com.dt.wsjf.bean;

import java.io.Serializable;

/**
 * Created by wangxun on 2018/3/12.
 */

public class VipRechargeBean implements Serializable {
    private String title;
    private int price;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
