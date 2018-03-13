package com.dt.wsjf.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dt.wsjf.R;
import com.dt.wsjf.bean.VipRechargeBean;

import java.util.List;


/**
 * Created by wangxun on 2018/3/12.
 */

public class VipRechargeAdapter extends BaseQuickAdapter<VipRechargeBean, BaseViewHolder> {

    public VipRechargeAdapter(List<VipRechargeBean> data) {
        super(R.layout.adapter_vip_recharge_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, VipRechargeBean item) {
        baseViewHolder.setText(R.id.recharge_title, item.getTitle());
        baseViewHolder.setText(R.id.recharge_price, item.getPrice() + "元");
        baseViewHolder.setText(R.id.recharge_content, item.getContent());
    }
}
