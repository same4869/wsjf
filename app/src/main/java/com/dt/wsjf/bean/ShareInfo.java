package com.dt.wsjf.bean;

import java.io.Serializable;

public class ShareInfo implements Serializable {
    /**
     * shareTilte : 微商快粉--十天加满你的微信好友
     * shareContent : 我在使用一款超好用的人脉管理与营销工具，让微信的操作批量自动化，加粉、清粉，上面有上百万的用户信息，让我的加粉效率大大提高，你也快来下载吧：http://www.baidu.com
     * shareUrl : http://www.baidu.com
     */

    private String shareTilte;
    private String shareContent;
    private String shareUrl;

    public String getShareTilte() {
        return shareTilte;
    }

    public void setShareTilte(String shareTilte) {
        this.shareTilte = shareTilte;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
