package com.dt.wsjf.adapter;


import android.graphics.Paint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dt.wsjf.R;
import com.dt.wsjf.bean.PriceConfigBean;

import java.util.List;


/**
 * Created by wangxun on 2018/3/12.
 */

public class VipRechargeAdapter extends BaseQuickAdapter<PriceConfigBean.DataBean, BaseViewHolder> {

    public VipRechargeAdapter(List<PriceConfigBean.DataBean> data) {
        super(R.layout.adapter_vip_recharge_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, PriceConfigBean.DataBean item) {
        baseViewHolder.setText(R.id.recharge_title, item.getTitle());
        ((TextView) baseViewHolder.getView(R.id.recharge_origin_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        baseViewHolder.setText(R.id.recharge_origin_price, item.getOriginPrice() + "元");
        baseViewHolder.setText(R.id.recharge_price, item.getPrice() + "元");
        baseViewHolder.setText(R.id.recharge_content, item.getContent());
    }
}
