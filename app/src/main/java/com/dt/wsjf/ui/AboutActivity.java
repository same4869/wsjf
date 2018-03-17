package com.dt.wsjf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dt.wsjf.R;
import com.dt.wsjf.config.Constants;

public class AboutActivity extends com.dt.wsjf.base.BaseActivity {
    private TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        titleTv = (TextView) findViewById(R.id.copyright);
        if (Constants.IS_DEBUG) {
            titleTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AboutActivity.this, DebugActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
