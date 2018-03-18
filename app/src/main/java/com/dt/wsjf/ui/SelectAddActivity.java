package com.dt.wsjf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dt.wsjf.R;
import com.dt.wsjf.adapter.GirdDropDownAdapter;
import com.dt.wsjf.adapter.ListDropDownAdapter;
import com.dt.wsjf.base.BaseActivity;
import com.dt.wsjf.utils.BmobUtil;
import com.dt.wsjf.utils.LogUtil;
import com.dt.wsjf.view.BouncyBtnView;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

public class SelectAddActivity extends BaseActivity {
    public static String RESULT_CITY_KEY = "result_city_key";
    public static String RESULT_SEX_KEY = "result_sex_key";

    private ArrayList<String> citys = new ArrayList<>();
    private String sexs[] = {"不限", "男", "女"};
    private String headers[] = {"城市", "性别"};

    private List<View> popupViews = new ArrayList<>();
    private GirdDropDownAdapter cityAdapter;
    private ListDropDownAdapter sexAdapter;
    private DropDownMenu mDropDownMenu;
    private TextView loadingView;
    private String city;
    private int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_add);
        loadingView = (TextView) findViewById(R.id.select_add_loading);

        initData();
    }

    private void initData() {
        BmobUtil.selectAddGroupByCitys(new BmobUtil.BmobResultListener() {
            @Override
            public void onSuccess(Object object) {
                citys = (ArrayList<String>) object;
                initView();
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public void onError(BmobException e) {
                Toast.makeText(getApplicationContext(), "初始化数据失败，请检查网络再重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mDropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);

        //init city menu
        final ListView cityView = new ListView(this);
        cityAdapter = new GirdDropDownAdapter(this, citys);
        cityView.setDividerHeight(0);
        cityView.setAdapter(cityAdapter);

        //init sex menu
        final ListView sexView = new ListView(this);
        sexView.setDividerHeight(0);
        sexAdapter = new ListDropDownAdapter(this, Arrays.asList(sexs));
        sexView.setAdapter(sexAdapter);

        //init popupViews
        popupViews.add(cityView);
        popupViews.add(sexView);

        //add item click event
        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (citys.size() > 0) {
                    cityAdapter.setCheckItem(position);
                    mDropDownMenu.setTabText(position == 0 ? headers[0] : citys.get(position));
                    mDropDownMenu.closeMenu();
                    city = position == 0 ? null : citys.get(position);
                } else {
                    Toast.makeText(getApplicationContext(), "初始化数据失败，请检查网络再重试", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sexView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sexAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : sexs[position]);
                mDropDownMenu.closeMenu();
                sex = position;
            }
        });

        View rootView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.select_add_content, null);

        BouncyBtnView bouncyBtnView = (BouncyBtnView) rootView.findViewById(R.id.select_add_btn);
        bouncyBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "模式更改成功，现在可以根据您指定的条件同步了", Toast.LENGTH_LONG).show();
                Intent data = new Intent();
                LogUtil.d("selectedAdd city:" + city + " sex:" + sex);
                data.putExtra(RESULT_CITY_KEY, city);
                data.putExtra(RESULT_SEX_KEY, sex);
                setResult(RESULT_OK, data);
                finish();
            }
        });

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, rootView);
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
}
