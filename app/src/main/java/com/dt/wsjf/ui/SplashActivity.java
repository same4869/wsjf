package com.dt.wsjf.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.dt.wsjf.R;

import commlib.xun.com.commlib.handler.CommWeakHandler;

public class SplashActivity extends Activity {
    private final int GOTO_NEXT_PAGE_WHAT = 1001;

    private CommWeakHandler handler = new CommWeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == GOTO_NEXT_PAGE_WHAT) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
                finish();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        handler.sendEmptyMessageDelayed(GOTO_NEXT_PAGE_WHAT, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
