package com.dt.wsjf.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by wangxun on 2018/3/15.
 */

public class CommConf extends BmobObject {
    private String ads;//公告
    private String priceConfig;//价格配置信息，json串
    private int addNumPreShare; //每次分享增加的数量
    private String shareInfo;//分享的内容配置

    public String getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(String shareInfo) {
        this.shareInfo = shareInfo;
    }

    public int getAddNumPreShare() {
        return addNumPreShare;
    }

    public void setAddNumPreShare(int addNumPreShare) {
        this.addNumPreShare = addNumPreShare;
    }

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    public String getPriceConfig() {
        return priceConfig;
    }

    public void setPriceConfig(String priceConfig) {
        this.priceConfig = priceConfig;
    }
}
