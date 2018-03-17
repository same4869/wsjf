package com.dt.wsjf.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangxun on 2018/3/16.
 */

public class PriceConfigBean implements Serializable {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * title : 黄金会员
         * content : 开通30天， 每天300个加粉名额
         * originPrice : 59.9
         * price : 39.9
         */

        private String title;
        private String content;
        private double originPrice;
        private double price;
        private int vipDays;
        private int vipCounts;
        private int vipLevel;

        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
        }

        public int getVipDays() {
            return vipDays;
        }

        public void setVipDays(int vipDays) {
            this.vipDays = vipDays;
        }

        public int getVipCounts() {
            return vipCounts;
        }

        public void setVipCounts(int vipCounts) {
            this.vipCounts = vipCounts;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public double getOriginPrice() {
            return originPrice;
        }

        public void setOriginPrice(double originPrice) {
            this.originPrice = originPrice;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
