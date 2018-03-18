package com.dt.wsjf.utils;

import android.content.Context;
import android.widget.Toast;

import com.dt.wsjf.bean.PriceConfigBean;

import c.b.BP;
import c.b.PListener;

/**
 * Created by wangxun on 2018/3/13.
 */

public class PayUtil {
    /**
     * 调用支付
     *
     * @param payType 支付类型，BP.PayType_Alipay、BP.PayType_Wechat、BP.PayType_QQ
     */
    public static void pay(final Context context, final PriceConfigBean.DataBean dataBean, int payType) {
        if (dataBean == null) {
            return;
        }
        final String name = dataBean.getTitle();
        LogUtil.d("pay dataBean.getPrice():" + dataBean.getPrice() + " dataBean.getVipCounts():" + dataBean.getVipCounts() + " dataBean.getVipDays():" + dataBean.getVipDays() + " dataBean.getVipLevel():" + dataBean.getVipLevel());

        // 仍然可以通过这种方式支付，其中true为支付宝，false为微信
        // BP.pay(name, getBody(), getPrice(), false, new PListener());
        //dataBean.getPrice()
        BP.pay(name, "微商快粉VIP升级", 0.02, payType, new PListener() {

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(context, "支付成功!请重新打开微商快粉立马生效！", Toast.LENGTH_SHORT).show();
                LogUtil.d("pay 支付成功");
                BmobUtil.updateInfoWhenPay(AppUtil.getIMEI(context), dataBean);
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                LogUtil.d("orderId --> " + orderId);
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                LogUtil.d("reason --> " + reason + " code --> " + code);
                if (code == 11201) {
                    Toast.makeText(context, "支付中断!请安装支付宝客户端", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "支付中断!请联系客服人员", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
