package com.dt.wsjf.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dt.wsjf.R;
import com.dt.wsjf.adapter.VipRechargeAdapter;
import com.dt.wsjf.bean.PriceConfigBean;
import com.dt.wsjf.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxun on 2018/3/12.
 */

public class VipRechargeDialog extends Dialog implements VipRechargeAdapter.OnItemClickListener {
    private Context context;
    private TextView cancelTxt;
    private RecyclerView rechargeRecyclerView;
    private VipRechargeAdapter vipRechargeAdapter;
    private List<PriceConfigBean.DataBean> data = new ArrayList<>();
    private VipRechargerItemCallBack vipRechargerItemCallBack;

    public VipRechargeDialog(@NonNull Context context, List<PriceConfigBean.DataBean> data) {
        super(context);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉对话框顶部栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_vip_recharge, null);
        setContentView(view);
        init(view);
    }

    private void init(View view) {
        setCanceledOnTouchOutside(true);
        cancelTxt = view.findViewById(R.id.cancel_txt);
        rechargeRecyclerView = view.findViewById(R.id.recharge_recycler_view);
        rechargeRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        rechargeRecyclerView.addItemDecoration(new SpacesItemDecoration(30));
        vipRechargeAdapter = new VipRechargeAdapter(data);
        rechargeRecyclerView.setAdapter(vipRechargeAdapter);
        vipRechargeAdapter.setOnItemClickListener(this);
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        dismiss();
        LogUtil.d("onItemClick position --> " + position);
        if (vipRechargerItemCallBack != null) {
            vipRechargerItemCallBack.onVipItemClick(data.get(position), position);
        }
    }

    public void setVipRechargerItemCallBack(VipRechargerItemCallBack vipRechargerItemCallBack) {
        this.vipRechargerItemCallBack = vipRechargerItemCallBack;
    }

    public interface VipRechargerItemCallBack {
        void onVipItemClick(PriceConfigBean.DataBean dataBean, int position);
    }
}
