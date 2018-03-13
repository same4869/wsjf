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
import com.dt.wsjf.bean.VipRechargeBean;
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
    private List<VipRechargeBean> data = new ArrayList<>();

    public VipRechargeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        initData();
    }

    private void initData() {
        VipRechargeBean data1 = new VipRechargeBean();
        data1.setTitle("VIP30天");
        data1.setPrice(10);
        data1.setContent("成为VIP后无人数限制，无功能限制");
        data.add(data1);

        VipRechargeBean data2 = new VipRechargeBean();
        data2.setTitle("VIP60天");
        data2.setPrice(20);
        data2.setContent("成为VIP后无人数限制，无功能限制");
        data.add(data2);

        VipRechargeBean data3 = new VipRechargeBean();
        data3.setTitle("VIP90天");
        data3.setPrice(30);
        data3.setContent("成为VIP后无人数限制，无功能限制");
        data.add(data3);

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
    }
}
