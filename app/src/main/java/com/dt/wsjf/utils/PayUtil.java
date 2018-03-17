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
    public static void pay(final Context context, PriceConfigBean.DataBean dataBean, int payType) {
        if (dataBean == null) {
            return;
        }
        final String name = dataBean.getTitle();

        // 仍然可以通过这种方式支付，其中true为支付宝，false为微信
        // BP.pay(name, getBody(), getPrice(), false, new PListener());

        BP.pay(name, "微商快粉VIP升级", dataBean.getPrice(), payType, new PListener() {

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(context, "支付成功!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "支付中断!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
