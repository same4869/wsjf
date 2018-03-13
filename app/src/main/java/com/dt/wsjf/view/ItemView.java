package com.dt.wsjf.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dt.wsjf.R;

/**
 * Created by wangxun on 2018/3/11.
 */

public class ItemView extends FrameLayout {
    private ImageView itemIcon;
    private TextView itemTitle;
    private TextView itemTip;
    private View rootView;

    public ItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.item_view_layout, this);
        itemIcon = (ImageView) rootView.findViewById(R.id.title_icon);
        itemTitle = (TextView) rootView.findViewById(R.id.item_title);
        itemTip = (TextView) rootView.findViewById(R.id.item_tip);
    }

    public void setItemContent(int ResId, String tilte, String tip, OnClickListener listener) {
        itemIcon.setImageResource(ResId);
        itemTitle.setText(tilte);
        itemTip.setText(tip);
        rootView.setOnClickListener(listener);
    }
}
