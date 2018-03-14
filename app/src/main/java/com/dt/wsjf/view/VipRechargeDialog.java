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
    private VipRechargerItemCallBack vipRechargerItemCallBack;

    public VipRechargeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        initData();
    }

    private void initData() {
        VipRechargeBean data1 = new VipRechargeBean();
        data1.setTitle("(限时)体验会员");
        data1.setPrice(3);
        data1.setContent("体验会员每天可加100粉，可使用3天");
        data.add(data1);

        VipRechargeBean data2 = new VipRechargeBean();
        data2.setTitle("青铜会员");
        data2.setPrice(29);
        data2.setContent("青铜会员每天可加200粉，可使用30天");
        data.add(data2);

        VipRechargeBean data3 = new VipRechargeBean();
        data3.setTitle("白银会员");
        data3.setPrice(69);
        data3.setContent("白银会员每天可加300粉，可使用90天");
        data.add(data3);

        VipRechargeBean data4 = new VipRechargeBean();
        data4.setTitle("黄金会员");
        data4.setPrice(99);
        data4.setContent("黄金会员每天加粉无限制，使用时间无限制");
        data.add(data4);

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
            vipRechargerItemCallBack.onVipItemClick(data.get(position).getTitle(), data.get(position).getPrice(), position);
        }
    }

    public void setVipRechargerItemCallBack(VipRechargerItemCallBack vipRechargerItemCallBack) {
        this.vipRechargerItemCallBack = vipRechargerItemCallBack;
    }

    public interface VipRechargerItemCallBack {
        void onVipItemClick(String title, int price, int position);
    }
}
