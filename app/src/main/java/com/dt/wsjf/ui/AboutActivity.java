package com.dt.wsjf.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dt.wsjf.R;
import com.dt.wsjf.config.Constants;
import com.dt.wsjf.utils.AppUtil;

import org.w3c.dom.Text;

public class AboutActivity extends com.dt.wsjf.base.BaseActivity {
    private TextView titleTv, kfId, versionNameTv, copyIdTv;

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
        kfId = (TextView) findViewById(R.id.my_id_tx);
        kfId.setText("快粉id:" + AppUtil.getIMEI(getApplicationContext()));
        versionNameTv = (TextView) findViewById(R.id.version_name);
        versionNameTv.setText("Version " + AppUtil.getLocalVersion(getApplicationContext()));
        copyIdTv = (TextView) findViewById(R.id.copy_id_tx);
        copyIdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.onClickCopy(getApplicationContext(), AppUtil.getIMEI(getApplicationContext()));
            }
        });
    }
}
